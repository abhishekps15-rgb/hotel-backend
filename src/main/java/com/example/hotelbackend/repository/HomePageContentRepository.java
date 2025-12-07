// src/main/java/com/example/hotelbackend/repository/HomePageContentRepository.java
package com.example.hotelbackend.repository;

import com.example.hotelbackend.model.HomePageContent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface HomePageContentRepository extends MongoRepository<HomePageContent, String> {

    // We always have at most one document
    Optional<HomePageContent> findFirstBy();
}
