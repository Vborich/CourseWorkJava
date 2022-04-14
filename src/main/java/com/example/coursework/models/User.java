package com.example.coursework.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private String username;

    private String password;

    private boolean active;

    private String email;

    private String avatarUrl;

    private boolean emailConfirmed;

    private String activationCode;

    private String recoveringPasswordCode;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="company_id", nullable = true)
    private Company company;

    @OneToOne(mappedBy = "userOwner", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Company ownCompany;

    public User(){}

    public User(String username, String password, boolean active, String email, String activationCode, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.active = active;
        this.email = email;
        this.activationCode = activationCode;
        this.roles = roles;
        this.emailConfirmed = false;
        avatarUrl = "https://res.cloudinary.com/ds2evqh9b/image/upload/v1624311123/avatar7_gnqxpd.png";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEmailConfirmed();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getRecoveringPasswordCode() {
        return recoveringPasswordCode;
    }

    public void setRecoveringPasswordCode(String recoveringPasswordCode) {
        this.recoveringPasswordCode = recoveringPasswordCode;
    }

    public boolean isEmailConfirmed() {
        return emailConfirmed;
    }

    public void setEmailConfirmed(boolean emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Company getOwnCompany() {
        return ownCompany;
    }

    public void setOwnCompany(Company ownCompany) {
        this.ownCompany = ownCompany;
    }
}
