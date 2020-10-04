package com.lukian.chatspring.session;

import com.lukian.chatspring.entity.data.User;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;
import java.util.logging.Logger;

@Component
@SessionScope
public class UserSession implements Serializable {

    private transient OAuth2AuthenticatedPrincipal principal;

    public UserSession() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        principal = (OAuth2AuthenticatedPrincipal) authentication
                .getPrincipal();
    }

    public User getUser() {
        User user = new User(principal.getAttribute("name"),
                principal.getAttribute("email"),
                principal.getAttribute("aud"),
                VaadinIcon.USER);
        Logger.getAnonymousLogger("user nick:" + user.getNick());
        Logger.getAnonymousLogger(principal.getAttributes().toString());
        return user;
    }

    public OAuth2AuthenticatedPrincipal getPrincipal() {
        return principal;
    }
}
