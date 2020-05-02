package com.lukian.ChatSpring.views.chat;

import com.lukian.ChatSpring.entity.Message;
import com.lukian.ChatSpring.entity.User;
import com.lukian.ChatSpring.repo.MessageRepo;
import com.lukian.ChatSpring.repo.UserRepo;
import com.lukian.ChatSpring.service.MessageService;
import com.lukian.ChatSpring.service.UserService;
import com.lukian.ChatSpring.views.components.*;
import com.lukian.ChatSpring.views.components.MessageComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Route("chat")
@Transactional
@PageTitle("Vaadin ChatView")
@CssImport("./static/styles/styles.css")
public class ChatView extends HorizontalLayout {

    private TextField area = new TextField();
    private Button pic = new Button(VaadinIcon.PICTURE.create());
    private Button send = new Button(VaadinIcon.PLAY.create());
    private VerticalLayout allMessagesLayout = new VerticalLayout();//collect all message components

    private MessageService messageService;
    private UserService userService;
    private User curentUser;
    private Dialog dialog;
    private ExampleUpload exampleUpload = new ExampleUpload();

    private Logger logger = Logger.getAnonymousLogger();

    @Autowired
    public ChatView(MessageRepo messageRepo, UserRepo userRepo) {
        VerticalLayout placeForChatComponents = new VerticalLayout();
        getStyle().set("background-color","#f5f5f5");

        messageService = new MessageService(messageRepo);
        userService = new UserService(userRepo);
        curentUser= userService.getCurentUser();
        dialog = new Dialog();
        dialog.add(exampleUpload);


        placeForChatComponents.add(new TopBarComponent());
        try{
            placeForChatComponents.add(allMessagesLayout);
        } catch (NullPointerException e){ logger.log(Level.SEVERE, "an exception was thrown: ",e); }
        placeForChatComponents.add(sendLineComponent());
        add(new LeftBarComponent());
        add(placeForChatComponents);

        //fill list of messages
        fillList(null);
        setStyles();
    }

    public HorizontalLayout sendLineComponent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth("75%");
        layout.add(area,pic,send);

        send.addClickListener(e -> {
            messageService.saveMessage(new Message(area.getValue(), curentUser, new Date()));//save to data
            fillList(new Message(area.getValue(), curentUser, new Date()));//add to the allMessagesLayout new message
            area.setValue("");//clear input area
        });

        pic.addClickListener(e -> {
            dialog.open();
            //At the moment, get the file name, put it in the area and thus make sure that it works
            dialog.addDialogCloseActionListener(ev ->{
                area.setValue(exampleUpload.getFilename());
                dialog.close();
            });
        });

        return layout;
    }

    private void fillList(Message message) {
        if (message == null) {
            //add all messages from the db
            messageService.findAllMessages().forEach(m -> allMessagesLayout.add(new MessageComponent(m)));
        } else {
            //add single message
            allMessagesLayout.add(new MessageComponent(message));
        }
    }

    //in future versions, remove this function and transfer it to the css file
    private void setStyles() {
        setSizeFull();
        allMessagesLayout.getStyle().set("overflow", "auto");//you can scroll through messages using this
        allMessagesLayout.getStyle().set("border", "1px solid");
        allMessagesLayout.setWidth("65%");
        allMessagesLayout.setHeight("90%");
        area.setWidth("75%");
        area.setHeight("auto");
        dialog.setWidth("400px");
        dialog.setHeight("150px");
    }

}