package com.example.coursework.models;

import javax.persistence.*;
import javax.servlet.http.PushBuilder;
import java.util.Date;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private int status;

    private Date orderDate;

    private Date confirmationDate;

    private int countUnits;

    private double totalPrice;

    public Order() {}

    public Order(int status, Date confirmationDate, double totalPrice, AdvertisingSubtype advertisingSubtype) {
        this.status = status;
        this.confirmationDate = confirmationDate;
        this.totalPrice = totalPrice;
        this.advertisingSubtype = advertisingSubtype;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="advertisingSubtype_id", nullable = false)
    private AdvertisingSubtype advertisingSubtype;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(Date confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public int getCountUnits() {
        return countUnits;
    }

    public void setCountUnits(int countUnits) {
        this.countUnits = countUnits;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public AdvertisingSubtype getAdvertisingSubtype() {
        return advertisingSubtype;
    }

    public void setAdvertisingSubtype(AdvertisingSubtype advertisingSubtype) {
        this.advertisingSubtype = advertisingSubtype;
    }
}
