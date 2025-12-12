package com.example.hotelbackend.service;

import com.example.hotelbackend.model.OurHotels;
import com.example.hotelbackend.repository.OurHotelsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OurHotelsService {

    private final OurHotelsRepository repository;

    public OurHotelsService(OurHotelsRepository repository) {
        this.repository = repository;
    }

    public OurHotels save(OurHotels hotels) {
        return repository.save(hotels);
    }

    public List<OurHotels> getAll() {
        return repository.findAll();
    }

    public OurHotels getOne(String id) {
        return repository.findById(id).orElse(null);
    }
}
