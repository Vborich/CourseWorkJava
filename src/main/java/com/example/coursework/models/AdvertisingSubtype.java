package com.example.coursework.models;

import javax.persistence.*;

@Entity
public class AdvertisingSubtype {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private String typeAdvertising;

    private String unit;

    private double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="advertising_id", nullable = false)
    private Advertising advertising;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeAdvertising() {
        return typeAdvertising;
    }

    public void setTypeAdvertising(String typeAdvertising) {
        this.typeAdvertising = typeAdvertising;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Advertising getAdvertising() {
        return advertising;
    }

    public void setAdvertising(Advertising advertising) {
        this.advertising = advertising;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
