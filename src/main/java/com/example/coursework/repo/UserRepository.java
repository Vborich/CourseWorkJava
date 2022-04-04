package com.example.coursework.repo;

import com.example.coursework.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    User findByActivationCode(String code);
    User findByRecoveringPasswordCode(String code);
}
