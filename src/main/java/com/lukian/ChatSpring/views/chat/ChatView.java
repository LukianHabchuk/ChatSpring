package com.lukian.ChatSpring.views.chat;

import com.lukian.ChatSpring.entity.Message;
import com.lukian.ChatSpring.entity.MessageType;
import com.lukian.ChatSpring.entity.User;
import com.lukian.ChatSpring.repo.MessageRepo;
import com.lukian.ChatSpring.repo.UserRepo;
import com.lukian.ChatSpring.service.MessageService;
import com.lukian.ChatSpring.service.UserService;
import com.lukian.ChatSpring.views.components.*;
import com.lukian.ChatSpring.views.components.MessageComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Route("chat")
@Transactional
@PageTitle("Vaadin ChatView")
@CssImport("./static/styles/styles.css")
@Push
public class ChatView extends HorizontalLayout {

    private final UnicastProcessor<Message> publisher;//message listener
    private final Flux<Message> messageFlux;//catch signal from message listener
    private final TextField area = new TextField();
    private final Button pic = new Button(VaadinIcon.PICTURE.create());
    private final Button send = new Button(VaadinIcon.PLAY.create());
    private final Button add = new Button("Add");
    private final Button cancel = new Button("Cancel");
    private final VerticalLayout allMessagesLayout = new VerticalLayout();//collect all message components

    private final MessageService messageService;
    private final UserService userService;
    private final User actualUser;
    private final Dialog dialog;
    private final ExampleUpload exampleUpload;

    private final Logger logger = Logger.getAnonymousLogger();

    public ChatView(MessageRepo messageRepo, UserRepo userRepo,
                    UnicastProcessor<Message> publisher, Flux<Message> messageFlux) {
        this.publisher = publisher;
        this.messageFlux = messageFlux;
        messageService = new MessageService(messageRepo);
        userService = new UserService(userRepo);
        actualUser = userService.getCurentUser();
        exampleUpload = new ExampleUpload();
        dialog = new Dialog();
        dialog.add(exampleUpload);
        //chat view
        add(chatView());
        //fill list of messages
        fillList();
        addNewMessage();
        setStyles();
    }

    private Component chatView() {
        VerticalLayout placeForChatComponents = new VerticalLayout();

        placeForChatComponents.add(new TopBarComponent());
        try{
            placeForChatComponents.add(allMessagesLayout);
        } catch (NullPointerException e){ logger.log(Level.SEVERE, "an exception was thrown: ",e); }
        placeForChatComponents.add(sendLineComponent());

        HorizontalLayout horizontalLayout = new HorizontalLayout(new LeftBarComponent(), placeForChatComponents);
        horizontalLayout.setSizeFull();
        return horizontalLayout;
    }

    public HorizontalLayout sendLineComponent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth("75%");
        layout.add(area,pic,send);
        buttonListeners();
        return layout;
    }

    public void buttonListeners() {
        send.addClickListener(e -> {
            //add to the allMessagesLayout new message
            if(!area.getValue().isEmpty()){
                try {
                    publisher.onNext(new Message(area.getValue().getBytes(), MessageType.Text, actualUser, new Date()));
                } catch (Exception exc) {
                    logger.log(Level.SEVERE, "an exception was thrown: ",exc);
                }
            }
            area.clear();
            area.focus();
        });

        pic.addClickListener(e -> {
            dialog.open();
            HorizontalLayout dialoglayout = new HorizontalLayout();
            dialoglayout.addAndExpand(add,cancel);
            exampleUpload.add(dialoglayout);

            add.addClickListener(ee->{
                if(exampleUpload.getFile()!=null){
                    try{
                        publisher.onNext(new Message(exampleUpload.getByteCode(), MessageType.Image, actualUser, new Date()));
                    } catch (Exception exc) {
                        logger.log(Level.SEVERE, "an exception was thrown: ",exc);
                    }
                }
                dialog.close();
            });

            cancel.addClickListener(ev -> dialog.close());

            area.focus();
        });
    }

    private void addNewMessage() {
        messageFlux.subscribe(message -> {
            getUI().ifPresent(ui ->
                    ui.access(() ->
                            {
                                //save to data
                                messageService.saveMessage(message);
                                //add single message
                                allMessagesLayout.add(new MessageComponent(message));
                                scrollToBottom();
                            })
                    );
        });
    }

    private void fillList() {
        //add all messages from the db
        messageService.findAllMessages().forEach(m -> allMessagesLayout.add(new MessageComponent(m)));
        scrollToBottom();
    }

    private void scrollToBottom() {
        //autoscroll to bottom
        allMessagesLayout.getElement().getChild(allMessagesLayout.getComponentCount()-1).callJsFunction("scrollIntoView");
    }

    //in future versions, remove this function and transfer it to the css file
    private void setStyles() {
        setSizeFull();
        getStyle().set("background-color","#f5f5f5");
        allMessagesLayout.getStyle().set("overflow", "auto");//you can scroll through messages using this
        allMessagesLayout.getStyle().set("border", "1px solid");
        allMessagesLayout.getStyle().set("border-radius","5px");
        allMessagesLayout.getStyle().set("background-color","white");
        allMessagesLayout.setWidth("65%");
        allMessagesLayout.setHeight("90%");
        area.setWidth("75%");
        area.setHeight("auto");
        dialog.setWidth("400px");
        dialog.setHeight("150px");
    }

}