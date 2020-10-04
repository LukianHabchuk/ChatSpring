package com.lukian.chatspring.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity // turns on Spring Security for the application.
@Configuration // tells Spring Boot to use this class for configuring security.
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_FAILURE_URL = "/login?error";
    private static final String LOGIN_URL = "/login";

    private UserPrincipalDetailsService userPrincipalDetailsService;

    public SecurityConfiguration(UserPrincipalDetailsService userPrincipalDetailsService) {
        this.userPrincipalDetailsService = userPrincipalDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() // Disables cross-site request forgery (CSRF) protection, as Vaadin already has
                .requestCache().requestCache(new CustomRequestCache()) // track unauthorized requests so that users are
                .and().authorizeRequests()
                .antMatchers("/chat").authenticated()// Turns on authorization.
                .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll() // Allows all internal traffic
                .anyRequest()
                .authenticated() // Allows all authenticated traffic.
                .and()
                .formLogin() // Enables form-based login and permits unauthenticated access to it
                .loginPage(LOGIN_URL).permitAll()
                .defaultSuccessUrl("/", true)
                .loginProcessingUrl(LOGIN_URL) // Configures the login page URLs.
                .failureUrl(LOGIN_FAILURE_URL)
                .defaultSuccessUrl("/")
                .and().oauth2Login().loginPage("/login/oauth2/code/google").permitAll()//auth with google
                .and().logout().logoutSuccessUrl(LOGIN_URL);// Configures the logout URL.
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/VAADIN/**", "/favicon.ico",
                "/robots.txt", "/manifest.webmanifest", "/sw.js",
                "/offline.html", "/icons/**", "/images/**", "/styles/**",
                "/frontend/**", "/h2-console/**",
                "/frontend-es5/**", "/frontend-es6/**");
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.userPrincipalDetailsService);
        return daoAuthenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}