package com.lukian.ChatSpring.service;

import com.lukian.ChatSpring.entity.Message;
import com.lukian.ChatSpring.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private MessageRepo messageRepo;

    public MessageService(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    public List<Message> findAllMessages(){
        return messageRepo.findAll();
    }

    public Message getMessageById(Long id){
        return this.messageRepo.findById(id).get();
    }

    public void saveMessage(Message message){
        this.messageRepo.save(message);
    }

    public void deleteMessage(Long id){
        this.messageRepo.deleteById(id);
    }

}
