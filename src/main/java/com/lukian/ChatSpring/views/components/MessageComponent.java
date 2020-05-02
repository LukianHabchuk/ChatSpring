package com.lukian.ChatSpring.views.components;

import com.lukian.ChatSpring.entity.Message;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.text.SimpleDateFormat;

public class MessageComponent extends HorizontalLayout {

    public MessageComponent(Message message) {
        VerticalLayout layout = new VerticalLayout();
        setSizeFull();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        String body = message.getBody();
        String format = formatter.format(message.getDate());

        Label bodyL = new Label(body);
        Label formatL = new Label(format);

        layout.add(bodyL);
        layout.add(formatL);

        add(new UserComponent(message.getUser()),layout);

        formatL.getStyle().set("color", "blue");
        layout.getStyle().set("overflow", "auto");
        layout.getStyle().set("border", "1px solid");
        layout.setWidth("85%");
        setHeight("auto");
    }
}
