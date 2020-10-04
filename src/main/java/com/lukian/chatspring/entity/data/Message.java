package com.lukian.chatspring.entity.data;

import com.lukian.chatspring.entity.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "msg")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private byte[] body;
    @NotNull
    private MessageType type;
    @NotNull
    private Date date;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Message(byte[] body, MessageType type, User user, Date date) {
        this.body = body;
        this.type = type;
        this.date = date;
        this.user = user;
    }
}
