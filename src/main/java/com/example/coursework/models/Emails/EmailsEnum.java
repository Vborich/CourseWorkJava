package com.example.coursework.models.Emails;

public enum EmailsEnum {

    SignUpBody("Привет, %s! Добро пожаловать в Борич. Кликните по следующей ссылке чтобы заверишть регистрацию: http://localhost:8080/activate/%s"),
    SignUpSubject("Регистрация"),

    RecoverPasswordBody("Привет, %s! Кликните по следующей ссылке чтобы восстановить пароль: http://localhost:8080/recover-password/%s"),
    RecoverPasswordSubject("Восстановление пароля");

    public final String label;

    private EmailsEnum(String label) {
        this.label = label;
    }
}
