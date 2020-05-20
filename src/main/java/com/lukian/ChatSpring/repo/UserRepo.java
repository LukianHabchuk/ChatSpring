package com.lukian.ChatSpring.repo;

import com.lukian.ChatSpring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    public User findByNick(String nick);
    public User findByEmail(String email);
}
