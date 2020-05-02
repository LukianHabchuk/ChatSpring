package com.lukian.ChatSpring;

import com.lukian.ChatSpring.entity.Message;
import com.lukian.ChatSpring.entity.Role;
import com.lukian.ChatSpring.entity.User;
import com.lukian.ChatSpring.repo.MessageRepo;
import com.lukian.ChatSpring.repo.UserRepo;
import com.lukian.ChatSpring.service.UserService;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

import javax.annotation.PostConstruct;
import java.util.*;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class ChatSpringApplication {

//	@Autowired
//	UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(ChatSpringApplication.class, args);
	}

//	@PostConstruct
//	void init() {
//		Set<Role> roles = new HashSet<>();
//		roles.add(Role.USER);
//		roles.add(Role.ADMIN);
//		User user = new User("user","email1","pass",VaadinIcon.PICTURE.create());
//		User user1 = new User("nick","email1","pass",VaadinIcon.USER.create());
//		user.setRoles(roles);
//
//		userService.addUser(user);
//		userService.addUser(user1);
//	}

}
