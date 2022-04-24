package com.example.coursework.models.AddressBuilder;

import com.example.coursework.models.Address;

public class AddressBuilder implements IAdressBuilder{

    private Address address;

    public AddressBuilder()
    {
        reset();
    }

    @Override
    public void reset() {
        address = new Address();
    }

    @Override
    public void setCountry(String country) {
        address.setCountry(country);
    }

    @Override
    public void setCity(String city) {
        address.setCity(city);
    }

    @Override
    public void setStreet(String street) {
        address.setStreet(street);
    }

    @Override
    public void setBuilding(String building) {
        address.setBuilding(building);
    }

    @Override
    public Address getResult() {
        Address result = this.address;
        reset();
        return result;
    }
}
