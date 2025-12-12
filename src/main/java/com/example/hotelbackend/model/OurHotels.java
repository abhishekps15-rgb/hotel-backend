package com.example.hotelbackend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("ourHotels")
public class OurHotels {

    @Id
    private String id;

    private String title;
    private String text;
    private String image;

    private Section mice;
    private Section wedding;
    private Section pilgrim;
    private Section longWeekends;

    private Corporate corporate;

    private Leisure leisure;

    @Data
    public static class Section {
        private String title;
        private String iconUrl;
        private java.util.List<String> locations;
    }

    @Data
    public static class Corporate {
        private String title;
        private String iconUrl;

        private java.util.List<String> staycation;
        private java.util.List<String> workcation;
        private java.util.List<String> city;
    }

    @Data
    public static class Leisure {
        private LeisureSection cultural;
        private LeisureSection beach;
        private LeisureSection hillside;
        private LeisureSection royal;
    }

    @Data
    public static class LeisureSection {
        private String title;
        private String iconUrl;
        private java.util.List<String> locations;
    }
}
