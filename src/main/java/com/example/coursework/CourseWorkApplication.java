package com.example.coursework;

import com.example.coursework.models.Role;
import com.example.coursework.models.User;
import com.example.coursework.repo.UserRepository;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@SpringBootApplication
public class CourseWorkApplication implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(CourseWorkApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("admin") == null) {
            User admin = new User();
            admin.setEmail("admin@gmail.com");
            admin.setRoles(Collections.singleton(Role.Admin));
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setActive(true);
            admin.setEmailConfirmed(true);
            admin.setAvatarUrl("https://res.cloudinary.com/ds2evqh9b/image/upload/v1624311123/avatar7_gnqxpd.png");
            userRepository.save(admin);
        }
    }
}
