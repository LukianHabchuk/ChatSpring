package com.lukian.ChatSpring.repo;

import com.lukian.ChatSpring.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {
    public List<Message> findByDate(Date date);
    public List<Message> findByUserNick(String nick);
}
