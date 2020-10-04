package com.lukian.chatspring.security;

import com.lukian.chatspring.entity.enums.Role;
import com.lukian.chatspring.entity.data.User;
import com.lukian.chatspring.repo.UserRepo;
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
    public UserDetails loadUserByUsername(String s) {
        User user = this.userRepository.findByNick(s);

        if (user != null) {
            List<GrantedAuthority> grupa = new ArrayList<>();
            grupa.add(new SimpleGrantedAuthority(Role.USER.name()));

            if (user.getRoles().contains(Role.ADMIN)) {
                grupa.add(new SimpleGrantedAuthority(Role.ADMIN.name()));
            }
            new UserPrincipal(user);
            return new org.springframework.security.core.userdetails
                    .User(user.getNick(), user.getPassword(),
                    true, true, true, true, grupa);
        } else {
            throw new UsernameNotFoundException("incorrect login or password");
        }
    }
}