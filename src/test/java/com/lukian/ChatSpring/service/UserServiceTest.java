package com.lukian.ChatSpring.service;

import com.lukian.ChatSpring.entity.Role;
import com.lukian.ChatSpring.entity.User;
import com.lukian.ChatSpring.repo.UserRepo;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Autowired
    private UserRepo userRepo;
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        this.userService = new UserService(userRepo);
        user = new User("nickname1","uniqueemail1","password", VaadinIcon.STAR.create());
        Set<Role> roles = new HashSet<Role>();
        roles.add(Role.USER);
        user.setRoles(roles);
        user.setActive(true);
    }

    @Test
    void findAllUsers() {
        assertNotNull(userService.findAllUsers());
    }

    @Test
    void getUserById() {
        assertNotNull(userService.getUserById(1L));
    }

    @Test
    void getUserByNick() {
        assertNotNull(userService.getUserByNick("nick"));
    }

    @Test
    void addUser() {
        long before = userService.findAllUsers().size();
        userService.addUser(user);
        assertEquals(before+1,userService.findAllUsers().size());
    }

    @Test
    void addExistingUser() {
        userService.addUser(user);
        long before = userService.findAllUsers().size();
        userService.addUser(user);
        assertEquals(before,userService.findAllUsers().size());
    }

    @Test
    void deleteUser() {
        long before = userService.findAllUsers().size();
        userService.deleteUser(user.getId());
        assertEquals(before-1,userService.findAllUsers().size());
    }
}