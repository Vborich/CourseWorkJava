package com.example.coursework.dto;

import com.example.coursework.models.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Set;

public class UserDto {
    private Long id;

    @Size(min = 3, max = 30, message = "Имя должно содержать от 3 до 30 символов")
    private String username;

    @Size(min = 5, max = 30, message = "Пароль должен содержать от 5 до 30 символов")
    private String password;

    @Email(message = "Неверный формат почты")
    private String email;

    @Size(min = 5, max = 30, message = "Пароль должен содержать от 5 до 30 символов")
    private String againPassword;

    private Set<Role> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAgainPassword() {
        return againPassword;
    }

    public void setAgainPassword(String againPassword) {
        this.againPassword = againPassword;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
