package com.example.coursework.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CompanyDto {

    private Long id;

    @NotEmpty(message = "Поле должно быть заполнено")
    @Size(min = 3, max = 50, message = "Имя компании должно содержать от 3 до 50 символов")
    private String companyName;

    @Email(message = "Неверный формат почты")
    private String centralEmail;

    @Pattern(regexp="(^$|[0-9]{9})", message = "Неверный формат телефона")
    @NotEmpty(message = "Поле должно быть заполнено")
    private String centralPhoneNumber;

    @Size(min = 3, max = 30, message = "Поле страны должно содержать от 3 до 30 символов")
    @NotEmpty(message = "Поле должно быть заполнено")
    private String country;

    @Size(min = 3, max = 30, message = "Поле города должно содержать от 3 до 30 символов")
    @NotEmpty(message = "Поле должно быть заполнено")
    private String city;

    @Size(min = 3, max = 30, message = "Поле улицы должно содержать от 3 до 30 символов")
    @NotEmpty(message = "Поле должно быть заполнено")
    private String street;

    @Size(min = 1, max = 10, message = "Поле дома должно содержать от 1 до 10 символов")
    @NotEmpty(message = "Поле должно быть заполнено")
    private String building;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCentralEmail() {
        return centralEmail;
    }

    public void setCentralEmail(String centralEmail) {
        this.centralEmail = centralEmail;
    }

    public String getCentralPhoneNumber() {
        return centralPhoneNumber;
    }

    public void setCentralPhoneNumber(String centralPhoneNumber) {
        this.centralPhoneNumber = centralPhoneNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
