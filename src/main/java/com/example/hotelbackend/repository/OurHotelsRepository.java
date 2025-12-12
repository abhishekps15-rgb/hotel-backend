package com.example.hotelbackend.repository;

import com.example.hotelbackend.model.OurHotels;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OurHotelsRepository extends MongoRepository<OurHotels, String> {
}
