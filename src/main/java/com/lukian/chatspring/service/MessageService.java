package com.lukian.chatspring.service;

import com.lukian.chatspring.entity.data.Message;
import com.lukian.chatspring.repo.MessageRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private MessageRepo messageRepo;

    public MessageService(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    public List<Message> findAllMessages() {
        return messageRepo.findAll();
    }

    public Message getMessageById(Long id) {
        Optional<Message> message = this.messageRepo.findById(id);
        if (message.isPresent())
            return message.get();
        else throw new NullPointerException();
    }

    public void saveMessage(Message message) {
        this.messageRepo.save(message);
    }

    public void deleteMessage(Long id) {
        this.messageRepo.deleteById(id);
    }

}
