package com.example.coursework.services;

import com.example.coursework.models.Advertising;
import com.example.coursework.models.Company;
import com.example.coursework.repo.AdvertisingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdvertisingService {

    @Autowired
    AdvertisingRepository advertisingRepository;

    public Iterable<Advertising> getAdvertisings()
    {
        return advertisingRepository.findAll();
    }

    public Advertising getAdvertisingById(long id)
    {
        if (!advertisingRepository.existsById(id))
            return null;
        return  advertisingRepository.findById(id).get();
    }

    public boolean addAdvertising(Advertising advertising)
    {
        Advertising advertisingDb = advertisingRepository.findByAdvertisingName(advertising.getAdvertisingName());
        if (advertisingDb != null)
            return false;

        advertisingRepository.save(advertising);
        return true;
    }

    public boolean removeAdvertising(long id)
    {
        Advertising advertising = getAdvertisingById(id);
        if (advertising == null)
            return false;

        advertisingRepository.delete(advertising);
        return true;
    }

    public boolean editAdvertising(Advertising advertising)
    {
        Advertising advertisingDb = advertisingRepository.findByAdvertisingName(advertising.getAdvertisingName());
        if (advertisingDb != null && advertisingDb.getId() != advertising.getId())
            return false;

        advertisingDb = getAdvertisingById(advertising.getId());
        advertisingDb.setAdvertisingName(advertising.getAdvertisingName());
        advertisingDb.setDescription(advertising.getDescription());
        advertisingRepository.save(advertising);
        return true;
    }
}
