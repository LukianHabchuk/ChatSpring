package com.lukian.ChatSpring.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Entity
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

    public Message() {
    }

    public Message(byte[] body, MessageType type, User user, Date date) {
        this.body = body;
        this.type = type;
        this.date = date;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public byte[] getBody() {
        return body;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message = (Message) o;
        return getId().equals(message.getId()) &&
                getBody().equals(message.getBody()) &&
                getDate().equals(message.getDate()) &&
                getUser().equals(message.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getBody(), getDate(), getUser());
    }
}
