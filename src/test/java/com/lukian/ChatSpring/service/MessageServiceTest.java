package com.lukian.ChatSpring.service;

import com.lukian.ChatSpring.entity.Message;
import com.lukian.ChatSpring.entity.User;
import com.lukian.ChatSpring.repo.MessageRepo;
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
        messageService.saveMessage(new Message("body",user, new Date()));
        assertEquals(before+1, messageService.findAllMessages().size());
    }

    @Test
    void deleteMessage() {
        long before = messageService.findAllMessages().size();
        messageService.deleteMessage(before);
        assertEquals(before-1, messageService.findAllMessages().size());
    }
}