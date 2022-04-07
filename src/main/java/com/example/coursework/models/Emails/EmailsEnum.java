package com.example.coursework.models.Emails;

public enum EmailsEnum {

    SignUpBody("Привет, %s! Добро пожаловать в Борич. Кликните по следующей ссылке чтобы заверишть регистрацию: %s/activate/%s"),
    SignUpSubject("Регистрация"),

    ConfirmEmailBody("Привет, %s! Вы пытаетесь изменить почту. Кликните по следующей ссылке чтобы подтвердить почту: %s/confirmEmail/%s/%s"),
    ConfirmEmailSubject("Подтверждения почты"),

    RecoverPasswordBody("Привет, %s! Кликните по следующей ссылке чтобы восстановить пароль: %s/recover-password/%s"),
    RecoverPasswordSubject("Восстановление пароля");

    public final String label;

    private EmailsEnum(String label) {
        this.label = label;
    }
}
