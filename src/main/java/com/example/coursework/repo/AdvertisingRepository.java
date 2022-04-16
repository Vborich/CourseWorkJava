package com.example.coursework.repo;

import com.example.coursework.models.Advertising;
import org.springframework.data.repository.CrudRepository;

public interface AdvertisingRepository extends CrudRepository<Advertising, Long> {
    Advertising findByAdvertisingName(String advertisingName);
}
