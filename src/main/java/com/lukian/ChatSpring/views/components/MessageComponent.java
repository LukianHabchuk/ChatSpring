package com.lukian.ChatSpring.views.components;

import com.lukian.ChatSpring.entity.Message;
import com.lukian.ChatSpring.entity.MessageType;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Iterator;

public class MessageComponent extends HorizontalLayout {

    public MessageComponent(Message message) {
        VerticalLayout layout = new VerticalLayout();
        setSizeFull();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        String format = formatter.format(message.getDate());
        Label formatL = new Label(format);

        if(message.getType()== MessageType.Image)
        layout.add(addElement(message.getBody()));
        else layout.add(new String(message.getBody()));
        layout.add(formatL);

        add(new UserComponent(message.getUser()),layout);

        formatL.getStyle().set("color", "blue");
        layout.getStyle().set("overflow", "auto");
        layout.getStyle().set("border", "1px solid");
        layout.getStyle().set("border-radius","5px");
        layout.setWidth("85%");
        setHeight("auto");
    }

    public Image addElement(byte[] decodedBytes){
        Image image = new Image();

        StreamResource resource = new StreamResource("name", () -> new ByteArrayInputStream(decodedBytes));
        try {
            image.getElement().setAttribute("src", resource);
            ImageInputStream in = ImageIO.createImageInputStream(new ByteArrayInputStream(decodedBytes));

            Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                try {
                    reader.setInput(in);
                } finally {
                    reader.dispose();
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return image;
    }

}
