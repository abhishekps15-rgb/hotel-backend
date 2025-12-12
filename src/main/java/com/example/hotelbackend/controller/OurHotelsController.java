package com.example.hotelbackend.controller;

import com.example.hotelbackend.model.OurHotels;
import com.example.hotelbackend.model.OurHotelsWrapper;
import com.example.hotelbackend.service.OurHotelsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/our-hotels")
public class OurHotelsController {

    private final OurHotelsService service;

    public OurHotelsController(OurHotelsService service) {
        this.service = service;
    }

    // ⭐ CREATE (POST)
    @PostMapping("/")
    public ResponseEntity<OurHotels> create(@RequestBody OurHotelsWrapper wrapper) {
        return ResponseEntity.ok(service.save(wrapper.getOurHotels()));
    }

    // ⭐ GET ALL
    @GetMapping("/")
    public ResponseEntity<List<OurHotels>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // ⭐ GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<OurHotels> getOne(@PathVariable String id) {
        return ResponseEntity.ok(service.getOne(id));
    }
}
