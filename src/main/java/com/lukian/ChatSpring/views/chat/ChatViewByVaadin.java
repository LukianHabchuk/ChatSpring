package com.lukian.ChatSpring.views.chat;

import com.lukian.ChatSpring.entity.User;
import com.lukian.ChatSpring.repo.MessageRepo;
import com.lukian.ChatSpring.repo.UserRepo;
import com.lukian.ChatSpring.service.MessageService;
import com.lukian.ChatSpring.service.UserService;
import com.lukian.ChatSpring.views.components.LeftBarComponent;
import com.lukian.ChatSpring.views.components.TopBarComponent;
import com.vaadin.componentfactory.model.Message;
import com.vaadin.componentfactory.Chat;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Route("chat1")
@Transactional
@PageTitle("Vaadin ChatView")
@CssImport("./static/styles/styles.css")
public class ChatViewByVaadin extends VerticalLayout {

    private MessageService messageService;
    private UserService userService;
    private User curentUser;
    private Chat chat;

    @Autowired
    public ChatViewByVaadin(MessageRepo messageRepo, UserRepo userRepo) {
        setSizeFull();
        messageService = new MessageService(messageRepo);
        userService = new UserService(userRepo);
        curentUser= userService.getCurentUser();

//        VerticalLayout placeForChatComponents = new VerticalLayout();
        chat = new Chat();
        chat.setMessages(loadMessages());
        chat.setDebouncePeriod(200);
        chat.setLazyLoadTriggerOffset(2500);
        chat.scrollToBottom();


        add(new TopBarComponent());
//        add(new LeftBarComponent());
//        add(placeForChatComponents);
        add(chat);

        chat.addChatNewMessageListener(event -> {
            event.getSource().addNewMessage(
                    new Message(event.getMessage(), curentUser.getIcon().toString(),
                            curentUser.getNick(),curentUser.isActive()));
            event.getSource().clearInput();
            event.getSource().scrollToBottom();
        });

        chat.addLazyLoadTriggerEvent(e -> {
//            messageStartNum2 += MESSAGE_LOAD_NUMBER;
            List<Message> list = loadMessages();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {}
            chat.setLoading(false);
            chat.addMessagesToTop(list);
        });

    }

    private List<Message> loadMessages() {
        List<Message> list = new ArrayList<>();
        messageService.findAllMessages().forEach(m -> list.add(
                new Message(m.getBody(), m.getUser().getIcon().toString(),
                        m.getUser().getNick(),m.getUser().isActive())
        ));
        return list;
    }
}
