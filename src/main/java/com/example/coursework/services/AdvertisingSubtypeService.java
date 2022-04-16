package com.example.coursework.services;

import com.example.coursework.models.Advertising;
import com.example.coursework.models.AdvertisingSubtype;
import com.example.coursework.repo.AdvertisingRepository;
import com.example.coursework.repo.AdvertisingSubtypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdvertisingSubtypeService {

    @Autowired
    AdvertisingSubtypeRepository advertisingSubtypeRepository;

    @Autowired
    AdvertisingRepository advertisingRepository;

    public AdvertisingSubtype getAdvertisingSubtype(long id)
    {
        if (!advertisingSubtypeRepository.existsById(id))
            return null;
        return advertisingSubtypeRepository.findById(id).get();
    }

    public boolean addAdvertisingSubtype(AdvertisingSubtype advertisingSubtype, long advertisingId)
    {
        Advertising advertising = advertisingRepository.findById(advertisingId).get();
        if (advertising == null)
            return false;

        advertisingSubtype.setAdvertising(advertising);
        advertisingSubtypeRepository.save(advertisingSubtype);
        return true;
    }

    public boolean removeAdvertisingSubtype(long id)
    {
        AdvertisingSubtype advertisingSubtype = getAdvertisingSubtype(id);
        if (advertisingSubtype == null)
            return false;

        advertisingSubtypeRepository.delete(advertisingSubtype);
        return true;
    }

    public void editAdvertising(AdvertisingSubtype advertisingSubtype)
    {
        AdvertisingSubtype advertisingSubtypeDb = getAdvertisingSubtype(advertisingSubtype.getId());
        advertisingSubtypeDb.setTypeAdvertising(advertisingSubtype.getTypeAdvertising());
        advertisingSubtypeDb.setUnit(advertisingSubtype.getUnit());
        advertisingSubtypeDb.setPrice(advertisingSubtype.getPrice());
        advertisingSubtypeRepository.save(advertisingSubtypeDb);
    }
}
