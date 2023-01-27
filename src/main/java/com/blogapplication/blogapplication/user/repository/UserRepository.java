package com.blogapplication.blogapplication.user.repository;

import com.blogapplication.blogapplication.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    public Optional<User> findByProfileId(String profileId);

    public Optional<User> findByEmail(String emailId);
}
