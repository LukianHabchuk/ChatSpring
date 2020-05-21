package com.lukian.ChatSpring;

import com.lukian.ChatSpring.entity.Message;
import com.lukian.ChatSpring.entity.MessageType;
import com.lukian.ChatSpring.entity.Role;
import com.lukian.ChatSpring.entity.User;
import com.lukian.ChatSpring.service.MessageService;
import com.lukian.ChatSpring.service.UserService;
import com.vaadin.flow.component.html.Label;
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
		return publisher().replay(30).autoConnect();
	}

	@PostConstruct
	void init() {
		Set<Role> roles = new HashSet<>();
		roles.add(Role.USER);
		roles.add(Role.ADMIN);
		User user = new User("user","email","pass",VaadinIcon.USER.create());
		User user1 = new User("nick","email1","pass",VaadinIcon.USER.create());
		user.setRoles(roles);
		userService.addUser(user);
		userService.addUser(user1);

		String s = "message label";
		Message m1 = new Message(s.getBytes(), MessageType.Text,user, new Date());
		try{
			messageService.saveMessage(m1);
		}
		catch (Exception e) {
			System.out.println("erroor: "+e);
		}
	}

}
