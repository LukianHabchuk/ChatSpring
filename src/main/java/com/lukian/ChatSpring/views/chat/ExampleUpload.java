package com.lukian.ChatSpring.views.chat;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

@Route("upload")
public class ExampleUpload extends VerticalLayout {

    private static final int MAXHEIGHT = 549;
    private static final int MAXWIDTH = 549;
    private byte[] byteCode;
    private Component file;
    private final Div output;

    public ExampleUpload(){
        output = new Div();
        output.setWidth("auto");
        output.setHeight("auto");
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);

        upload.addSucceededListener(event -> {
            Component component = createComponent(event.getMIMEType(),
                    event.getFileName(), buffer.getInputStream());
            showOutput(event.getFileName(), component, output);
        });

        upload.setMaxFileSize(500 * 1024);
        add(upload,output);
    }

    private Component createComponent(String mimeType, String fileName, InputStream stream) {
        Image image = new Image();
        try {

            byte[] bytes = IOUtils.toByteArray(stream);
            image.getElement().setAttribute("src", new StreamResource(fileName, () -> new ByteArrayInputStream(bytes)));

            ImageInputStream in = ImageIO.createImageInputStream(new ByteArrayInputStream(bytes));
            final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                try {
                    reader.setInput(in);
                    image.setWidth(reader.getWidth(0) + "px");
                    image.setHeight(reader.getHeight(0) + "px");
                    if(reader.getWidth(0)>MAXWIDTH)
                        image.setWidth(MAXWIDTH + "px");
                    if(reader.getHeight(0)>MAXHEIGHT)
                        image.setHeight(MAXHEIGHT + "px");
                } finally {
                    reader.dispose();
                }
            }

            setByteCode(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    private void showOutput(String text, Component content,
                            HasComponents outputContainer) {
        HtmlComponent p = new HtmlComponent(Tag.P);
        p.getElement().setText(text);
        outputContainer.add(p);
        outputContainer.add(content);
        setFile(content);
    }

    public Component getFile() {
        return file;
    }

    public void setFile(Component file) {
        this.file = file;
    }

    public byte[] getByteCode() {
        return byteCode;
    }

    public void setByteCode(byte[] byteCode) {
        this.byteCode = byteCode;
    }
}