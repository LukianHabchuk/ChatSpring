package com.lukian.chatspring.views.chat;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

@Route("upload")
class PictureUploader extends VerticalLayout {

    private static final int MAX_FILE_SIZE = 500 * 1024;
    private byte[] byteCode;

    PictureUploader() {
        Div output = new Div();
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setMaxFileSize(MAX_FILE_SIZE);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        upload.addSucceededListener(event -> {
            createComponent(buffer.getInputStream());
            showOutput(event.getFileName(), output);
        });

        add(upload, output);
    }

    private void createComponent(InputStream stream) {
        try {
            byte[] bytes = IOUtils.toByteArray(stream);
            setByteCode(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showOutput(String text,
                            HasComponents outputContainer) {
        HtmlComponent p = new HtmlComponent(Tag.P);
        p.getElement().setText("File with name: " + text + " is ready to send");
        outputContainer.add(p);
    }

    byte[] getByteCode() {
        return byteCode;
    }

    private void setByteCode(byte[] byteCode) {
        this.byteCode = byteCode;
    }
}