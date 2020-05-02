package com.lukian.ChatSpring.service;

import com.lukian.ChatSpring.entity.Role;
import com.lukian.ChatSpring.entity.User;
import com.lukian.ChatSpring.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    List<User> findAllUsers() {
        return new ArrayList<>(this.userRepo.findAll());
    }

    public User getUserById(Long id){
        return this.userRepo.findById(id).get();
    }

    public User getUserByNick(String nick){
        return this.userRepo.findByNick(nick);
    }

    public void addUser(User user){
        User userFromDb = userRepo.findByNick(user.getNick());

        if (userFromDb == null) {
            user.setActive(true);
            user.setRoles(Collections.singleton(Role.USER));
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            this.userRepo.save(user);
        }

    }

    public void updatePassword(Long id, String password){
        User user = this.userRepo.findById(id).get();
        user.setPassword(password);
        this.userRepo.save(user);
    }

    public User getCurentUser() {
        String username = "";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        username = principal instanceof UserDetails ?
                ((UserDetails)principal).getUsername() :
                principal.toString();
        return getUserByNick(username);
    }

    public void deleteUser(Long id){
        this.userRepo.deleteById(id);
    }
}
