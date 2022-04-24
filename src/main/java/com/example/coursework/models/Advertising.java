package com.example.coursework.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
public class Advertising {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @NotEmpty(message = "Поле должно быть заполнено")
    @Size(min = 3, max = 50, message = "Название рекламы должно содержать от 3 до 50 символов")
    private String advertisingName;

    @NotEmpty(message = "Поле должно быть заполнено")
    @Size(min = 50, max = 500, message = "Описание рекламы должно содержать от 50 до 500 символов")
    @Column(columnDefinition="TEXT")
    private String description;

    @OneToMany(mappedBy = "advertising", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<AdvertisingSubtype> advertisingSubtypes;

    public Advertising(){}

    public Advertising(String advertisingName, String description, Set<AdvertisingSubtype> advertisingSubtypes) {
        this.advertisingName = advertisingName;
        this.description = description;
        this.advertisingSubtypes = advertisingSubtypes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdvertisingName() {
        return advertisingName;
    }

    public void setAdvertisingName(String advertisingName) {
        this.advertisingName = advertisingName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<AdvertisingSubtype> getAdvertisingSubtypes() {
        return advertisingSubtypes;
    }

    public void setAdvertisingSubtypes(Set<AdvertisingSubtype> advertisingSubtypes) {
        this.advertisingSubtypes = advertisingSubtypes;
    }
}
