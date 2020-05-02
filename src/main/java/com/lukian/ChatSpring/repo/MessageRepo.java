package com.lukian.ChatSpring.repo;

import com.lukian.ChatSpring.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface MessageRepo extends JpaRepository<Message, Long> {
    public List<Message> findByDate(Date date);
    public List<Message> findByUserNick(String nick);
}
