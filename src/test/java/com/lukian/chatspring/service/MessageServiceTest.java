package com.lukian.chatspring.service;

import com.lukian.chatspring.entity.data.Message;
import com.lukian.chatspring.entity.enums.MessageType;
import com.lukian.chatspring.entity.data.User;
import com.lukian.chatspring.repo.MessageRepo;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class MessageServiceTest {

    @Autowired
    private MessageRepo messageRepo;
    private MessageService messageService;
    private User user;

    @Before("")
    void setup() {
        this.messageService = new MessageService(this.messageRepo);
        this.user = messageService.getMessageById(1L).getUser();
    }

    @Test
    void findAllMessages() {
        assertNotNull(messageService.findAllMessages());
    }

    @Test
    void getMessageById() {
        assertNotNull(messageService.getMessageById(1L));
    }

    @Test
    void saveMessage() {
        int before = messageService.findAllMessages().size();
        String s = "body";
        messageService.saveMessage(new Message(s.getBytes(), MessageType.TEXT,user, new Date()));
        assertEquals(before+1, messageService.findAllMessages().size());
    }

    @Test
    void deleteMessage() {
        long before = messageService.findAllMessages().size();
        messageService.deleteMessage(before);
        assertEquals(before-1, messageService.findAllMessages().size());
    }
}