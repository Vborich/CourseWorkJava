package com.example.coursework.models.AddressBuilder;

import com.example.coursework.models.Address;

public interface IAdressBuilder {
    void reset();
    void setCountry(String country);
    void setCity(String city);
    void setStreet(String street);
    void setBuilding(String building);
    Address getResult();
}
