package com.lukian.chatspring.views.chat;

import com.lukian.chatspring.entity.data.Message;
import com.lukian.chatspring.entity.enums.MessageType;
import com.lukian.chatspring.entity.data.User;
import com.lukian.chatspring.repo.MessageRepo;
import com.lukian.chatspring.repo.UserRepo;
import com.lukian.chatspring.service.MessageService;
import com.lukian.chatspring.service.UserService;
import com.lukian.chatspring.views.components.*;
import com.lukian.chatspring.views.components.MessageComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
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
@StyleSheet("frontend://styles/styles.css")
@Push
public class ChatView extends HorizontalLayout {

    private final transient UnicastProcessor<Message> publisher; //message listener
    private final transient Flux<Message> messageFlux; //catch signal from message listener
    private final TextField areaToEnterTextMessage = new TextField();
    private final Button selectPictureFromDisk = new Button(VaadinIcon.PICTURE.create());
    private final Button sendTextMessage = new Button(VaadinIcon.PLAY.create());
    private final Button sendPicture = new Button("Add");
    private final Button cancelSendingPicture = new Button("Cancel");
    private final VerticalLayout allMessageComponents = new VerticalLayout(); //collect all message components

    private final transient MessageService messageService;
    private final User currentUser;
    private Dialog dialogWithPictureUploader;
    private PictureUploader pictureUploader;

    private static final String LOG_MSG = "an exception was thrown: ";

    private final transient Logger logger = Logger.getAnonymousLogger();

    public ChatView(MessageRepo messageRepo, UserRepo userRepo,
                    UnicastProcessor<Message> publisher, Flux<Message> messageFlux) {
        this.publisher = publisher;
        this.messageFlux = messageFlux;
        messageService = new MessageService(messageRepo);
        currentUser = new UserService(userRepo).getCurentUser();
        pictureUploader = new PictureUploader();
        dialogWithPictureUploader = new Dialog();
        dialogWithPictureUploader.add(pictureUploader);
        //chat view
        add(chatView());
        setStyles();
    }

    private Component chatView() {
        VerticalLayout placeForCentralComponents = new VerticalLayout();

        placeForCentralComponents.add(new TopBarComponent());
        try {
            //fill list of messages
            fillAllMessageComponents();
            listenPublisher();
            placeForCentralComponents.add(allMessageComponents);
        } catch (NullPointerException e) { logger.log(Level.SEVERE, LOG_MSG, e); }
        placeForCentralComponents.add(sendLineComponent());

        HorizontalLayout allViewsLayout = new HorizontalLayout(new LeftBarComponent(), placeForCentralComponents);
        allViewsLayout.setSizeFull();
        return allViewsLayout;
    }

    public HorizontalLayout sendLineComponent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth("75%");
        layout.add(areaToEnterTextMessage, selectPictureFromDisk, sendTextMessage);
        buttonListeners();
        return layout;
    }

    public void buttonListeners() {
        sendTextMessage.addClickListener(e -> sendTextMessage());
        selectPictureFromDisk.addClickListener(e -> sendPicture());
        areaToEnterTextMessage.focus();
    }

    public void sendTextMessage() {
        /* add to the allMessagesLayout new message */
        if (!areaToEnterTextMessage.getValue().isEmpty()) {
            try {
                publish(areaToEnterTextMessage.getValue());
            } catch (Exception exc) {
                logger.log(Level.SEVERE, LOG_MSG, exc);
            }
        }
        areaToEnterTextMessage.clear();
    }

    public void sendPicture() {
        pictureUploader = new PictureUploader();
        dialogWithPictureUploader = new Dialog();
        dialogWithPictureUploader.add(pictureUploader);
        dialogWithPictureUploader.open();
        HorizontalLayout dialoglayout = new HorizontalLayout();
        dialoglayout.addAndExpand(sendPicture, cancelSendingPicture);
        pictureUploader.add(dialoglayout);

        listenDialogButtons();
    }

    private void listenDialogButtons() {
        sendPicture.addClickListener(ee -> {
            if (pictureUploader.getByteCode() != null) {
                try {
                    publish(pictureUploader.getByteCode());
                } catch (Exception exc) {
                    logger.log(Level.WARNING, LOG_MSG, exc);
                }
            }
            dialogWithPictureUploader.close();
        });
        //close the dialog if cancel sending picture
        cancelSendingPicture.addClickListener(ev -> dialogWithPictureUploader.close());
    }

    //publisher for text sender
    public void publish(String textOfMessage) {
        publisher.onNext(new Message(
                textOfMessage.getBytes(),
                MessageType.TEXT,
                currentUser,
                new Date())
        );
    }

    //publisher for picture sender
    public void publish(byte[] bytes) {
        publisher.onNext(new Message(
                bytes,
                MessageType.IMAGE,
                currentUser,
                new Date())
        );
    }

    private void listenPublisher() {
        messageFlux.subscribe(message -> getUI().ifPresent(ui ->
                ui.access(() -> {
                            //save new message to database
                            messageService.saveMessage(message);
                            //add single message to previous messages
                            allMessageComponents.add(new MessageComponent(message));
                            scrollToBottom();
                        })
                ));
    }

    private void fillAllMessageComponents() {
        //get all messages from the db and put in to the MessageComponent and show it on chat
        messageService.findAllMessages()
                .forEach(m -> allMessageComponents.add(new MessageComponent(m)));
        scrollToBottom();
    }

    private void scrollToBottom() {
        //autoscroll to bottom
        if (allMessageComponents.getComponentCount() > 1) {
            allMessageComponents.getElement()
                    .getChild(allMessageComponents.getComponentCount() - 1)
                    .callJsFunction("scrollIntoView");
        }
    }

    //in future versions, remove this function and transfer it to the css file
    private void setStyles() {
        setSizeFull();
        setClassName("chatView");
        allMessageComponents.setClassName("allMessagesLayout");
        allMessageComponents.setWidth("65%");
        allMessageComponents.setHeight("90%");
        areaToEnterTextMessage.setWidth("75%");
        areaToEnterTextMessage.setHeight("auto");
        dialogWithPictureUploader.setWidth("400px");
        dialogWithPictureUploader.setHeight("150px");
    }

}