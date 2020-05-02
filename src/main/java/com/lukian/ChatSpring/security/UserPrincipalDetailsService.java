package com.lukian.ChatSpring.security;

import com.lukian.ChatSpring.entity.Role;
import com.lukian.ChatSpring.entity.User;
import com.lukian.ChatSpring.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserPrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = this.userRepository.findByNick(s);

        if(user != null){
            List<GrantedAuthority> grupa = new ArrayList<>();
            grupa.add(new SimpleGrantedAuthority(Role.USER.name()));

            if(user.getRoles().contains(Role.ADMIN.name())){ grupa.add(new SimpleGrantedAuthority(Role.ADMIN.name())); }
            new UserPrincipal(user);
            return new org.springframework.security.core.userdetails
                    .User(user.getNick(),user.getPassword(),
                    true,true,true,true,grupa);
        } else {
            throw new UsernameNotFoundException("incorrect login or password");
        }

//        return new UserPrincipal(user);
    }
}