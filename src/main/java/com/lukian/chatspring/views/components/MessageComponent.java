package com.lukian.chatspring.views.components;

import com.lukian.chatspring.entity.data.Message;
import com.lukian.chatspring.entity.enums.MessageType;
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
import java.util.Iterator;
import java.util.logging.Logger;

public class MessageComponent extends HorizontalLayout {

    public MessageComponent(Message message) {
        setClassName("MessageComponent");
        setSizeFull();
        VerticalLayout layout = new VerticalLayout();
        layout.setClassName("layout");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        String format = formatter.format(message.getDate());
        Label dateFormat = new Label(format);
        dateFormat.setClassName("dateFormat");

        if (message.getType() == MessageType.IMAGE) {
            layout.add(addElement(message.getBody()));
        } else {
            layout.add(new String(message.getBody()));
        }
        layout.add(dateFormat);

        add(new UserComponent(message.getUser()), layout);

        layout.setWidth("85%");
        setHeight("auto");
    }

    private Image addElement(byte[] decodedBytes) {
        try {
            Image image = new Image();
            StreamResource resource = new StreamResource("name", () -> new ByteArrayInputStream(decodedBytes));
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

            return image;
        } catch (Exception e) {
            Logger.getAnonymousLogger(e.getMessage());
            return null;
        }
    }

}
