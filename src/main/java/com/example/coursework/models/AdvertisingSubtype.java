package com.example.coursework.models;

import javax.persistence.*;
import java.util.Set;

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

    @OneToMany(mappedBy = "advertisingSubtype", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Order> orders;

    public  AdvertisingSubtype()
    {

    }

    public AdvertisingSubtype(String typeAdvertising, String unit, double price, Advertising advertising) {
        this.typeAdvertising = typeAdvertising;
        this.unit = unit;
        this.price = price;
        this.advertising = advertising;
    }

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

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
}
