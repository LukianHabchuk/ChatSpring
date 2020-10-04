package com.lukian.chatspring.service;

import com.lukian.chatspring.entity.enums.Role;
import com.lukian.chatspring.entity.data.User;
import com.lukian.chatspring.repo.UserRepo;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

@Service
public class UserService {

    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List<User> findAllUsers() {
        return new ArrayList<>(this.userRepo.findAll());
    }

    public User getUserById(Long id) {
        Optional<User> user = this.userRepo.findById(id);
        if(user.isPresent())
            return user.get();
        else throw new NullPointerException();
    }

    public User getUserByNick(String nick) {
        return this.userRepo.findByNick(nick);
    }

    public void addUser(User user) {
        User userFromDb = userRepo.findByNick(user.getNick());

        if (userFromDb == null) {
            user.setActive(true);
            user.setRoles(Collections.singleton(Role.USER));
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            this.userRepo.save(user);
        }

    }

    public void updatePassword(Long id, String password) {
        User user = getUserById(id);
        user.setPassword(password);
        this.userRepo.save(user);
    }

    public User getCurentUser() {
        User user = new User();
        try {
            String username = "";
            Object principal = SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            username = principal instanceof UserDetails
                    ? ((UserDetails) principal).getUsername()
                    : principal.toString();
            user = getUserByNick(username);

            if (user == null) {
                user = createUser(username);
            }
        } catch (Exception e) {
            Logger.getAnonymousLogger(e.getMessage());
        }
        return user;
    }

    private User createUser(String principal) {
        String[] atributes = principal.substring(
                principal.indexOf("at_hash"), principal.length()-2).split(",");
        String nick = atributes[10].replace("name=","");
        String email = atributes[atributes.length-1].replace("email=","");
        String password = atributes[8].replace("aud=","");
        HashSet<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        User user = new User(nick, email, password, VaadinIcon.USER);
        user.setRoles(roles);
        userRepo.save(user);
        return userRepo.findByEmail(email);
    }

    public void deleteUser(Long id) {
        this.userRepo.deleteById(id);
    }
}
