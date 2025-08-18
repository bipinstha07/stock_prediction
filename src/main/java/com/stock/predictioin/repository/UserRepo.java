package com.stock.predictioin.repository;

import com.stock.predictioin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,String> {
    User findByEmail(String email);
}
