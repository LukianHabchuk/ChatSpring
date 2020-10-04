package com.lukian.chatspring;

import com.lukian.chatspring.entity.data.Message;
import com.lukian.chatspring.entity.enums.MessageType;
import com.lukian.chatspring.entity.enums.Role;
import com.lukian.chatspring.entity.data.User;
import com.lukian.chatspring.service.MessageService;
import com.lukian.chatspring.service.UserService;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.logging.Logger;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class ChatSpringApplication {

	@Autowired
	UserService userService;
	@Autowired
	MessageService messageService;

	public static void main(String[] args) {
		SpringApplication.run(ChatSpringApplication.class, args);
	}

	@Bean
	UnicastProcessor<Message> publisher() {
		return UnicastProcessor.create();
	}

	@Bean
	Flux<Message> messageFlux(UnicastProcessor<Message> publicher) {
		return publicher.replay(30).autoConnect();
	}

	@PostConstruct
	void init() {
		Set<Role> roles = new HashSet<>();
		roles.add(Role.USER);
		roles.add(Role.ADMIN);
		User user = new User("user", "email", "pass", VaadinIcon.HOME);
		User user1 = new User("nick", "email1", "pass", VaadinIcon.USER);
		user.setRoles(roles);
		userService.addUser(user);
		userService.addUser(user1);

		String s = "message label";
		Message m1 = new Message(s.getBytes(), MessageType.TEXT, user, new Date());
		try {
			messageService.saveMessage(m1);
		} catch (Exception e) {
			Logger.getAnonymousLogger("error: " + e);
		}
	}

}
