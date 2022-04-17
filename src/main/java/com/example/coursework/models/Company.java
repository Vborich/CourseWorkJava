package com.example.coursework.models;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private String companyName;

    private String centralEmail;

    private String centralPhoneNumber;

    @OneToOne(optional = false, mappedBy = "company", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Address address;

    @OneToMany(mappedBy = "company", cascade={CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private Set<User> users;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_owner_id", referencedColumnName = "id")
    private User userOwner;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Order> orders;

    @PreRemove
    private void preRemove() {
        users.forEach( user -> user.setCompany(null));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public User getUserOwner() {
        return userOwner;
    }

    public void setUserOwner(User userOwner) {
        this.userOwner = userOwner;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
}
