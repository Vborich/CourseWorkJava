package com.example.coursework.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public class UserDto {

    @Size(min = 3, max = 30, message = "Имя должно содержать от 3 до 30 сиволов")
    private String username;

    @Size(min = 5, max = 30, message = "Пароль должен содержать от 5 до 30 сиволов")
    private String password;

    @Email(message = "Неверный формат почты")
    private String email;

    @Size(min = 5, max = 30, message = "Пароль должен содержать от 5 до 30 сиволов")
    private String againPassword;

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
}
