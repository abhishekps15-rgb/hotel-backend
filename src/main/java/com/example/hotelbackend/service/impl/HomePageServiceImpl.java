package com.example.hotelbackend.service.impl;

import com.example.hotelbackend.dto.HomePageData;
import com.example.hotelbackend.model.HomePageContent;
import com.example.hotelbackend.repository.HomePageContentRepository;
import com.example.hotelbackend.service.HomePageService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomePageServiceImpl implements HomePageService {

    private final HomePageContentRepository repository;

    /* ===========================================================
       INITIALIZE DEFAULT DATA ON FIRST RUN
    ============================================================ */
    @PostConstruct
    public void init() {
        if (repository.count() > 0) return;

        HomePageContent content = new HomePageContent();

        // HERO IMAGES (match EXACT Home.jsx)
        content.setHeroImages(List.of(
                "/assets/hero1.png",
                "/assets/hero2.png",
                "/assets/brand-2.png",
                "/assets/hero2.png",
                "/assets/brand-2.png"
        ));

        // BRAND SECTION (dynamic blocks)
        HomePageContent.BrandDynamicSection bs = new HomePageContent.BrandDynamicSection();
        bs.setTitle("OUR BRANDS");

        HomePageContent.BrandBlock b1 = new HomePageContent.BrandBlock();
        b1.setLayout("text-left-image-right");
        b1.setText("Experience the pinnacle of refined Indian luxury at Pride Plaza...");
        b1.setImageUrl("/assets/brand-1.png");

        HomePageContent.BrandBlock b2 = new HomePageContent.BrandBlock();
        b2.setLayout("image-left-text-right");
        b2.setText("Dynamic, stylish, and connected to India’s evolving urban lifestyle...");
        b2.setImageUrl("/assets/brand-1.png");

        HomePageContent.BrandBlock b3 = new HomePageContent.BrandBlock();
        b3.setLayout("text-left-image-right");
        b3.setText("Focused on accessibility, convenience, and premium value...");
        b3.setImageUrl("/assets/brand-1.png");

        bs.setBlocks(List.of(b1, b2, b3));
        content.setBrandSection(bs);

        // EVENTS
        HomePageContent.EventsSection es = new HomePageContent.EventsSection();
        es.setTitle("UPCOMING EVENTS");

        HomePageContent.Event e1 = new HomePageContent.Event();
        e1.setTitle("Food Festival");
        e1.setDescription("Curated world cuisine from top chefs.");
        e1.setImageUrl("/assets/event1.jpg");

        es.setEvents(List.of(e1));
        content.setEventsSection(es);

        // ABOUT
        HomePageContent.AboutSection as = new HomePageContent.AboutSection();
        as.setTitle("ABOUT US");
        as.setDescription("We deliver luxury and comfort across all our hotels.");
        as.setButtonText("Learn More");
        as.setButtonLink("/about");
        content.setAboutSection(as);

        // BRAND BANNER
        HomePageContent.BrandBanner bb = new HomePageContent.BrandBanner();
        bb.setTitle("TRULY INDIAN. TRADITIONALLY LUXURIOUS.");
        bb.setSubtitle("Leading Hotel Chain Group in India");
        content.setBrandBanner(bb);

        // CONTACT SECTION
        HomePageContent.ContactSection cs = new HomePageContent.ContactSection();
        cs.setReservationPhone("1800 209 1400");
        cs.setHotelPhone("+91 9876543210");
        cs.setEmail("reservations@hotel.com");
        cs.setCorporateAddress("Corporate Office, XYZ Plaza, Mumbai");
        cs.setSupportHours("24x7");

        HomePageContent.SocialLink fb = new HomePageContent.SocialLink();
        fb.setName("facebook");
        fb.setUrl("https://facebook.com/hotel");

        HomePageContent.SocialLink ig = new HomePageContent.SocialLink();
        ig.setName("instagram");
        ig.setUrl("https://instagram.com/hotel");

        HomePageContent.SocialLink li = new HomePageContent.SocialLink();
        li.setName("linkedin");
        li.setUrl("https://linkedin.com/company/hotel");

        cs.setSocialLinks(List.of(fb, ig, li));
        content.setContactSection(cs);

        repository.save(content);
    }

    /* ===========================================================
       GET HOME PAGE DATA
    ============================================================ */
    @Override
    public HomePageData getHomePageData() {
        return repository.findFirstBy()
                .map(this::toDto)
                .orElseGet(HomePageData::new);
    }

    /* ===========================================================
       UPDATE HOME PAGE DATA (PARTIAL UPDATE)
    ============================================================ */
    @Override
    public HomePageData updateHomePageData(HomePageData dto) {
        HomePageContent entity = repository.findFirstBy()
                .orElseGet(HomePageContent::new);

        updateEntityFromDto(entity, dto);

        HomePageContent saved = repository.save(entity);
        return toDto(saved);
    }

    /* ===========================================================
       DTO → ENTITY MAPPER (Partial update)
    ============================================================ */
    private void updateEntityFromDto(HomePageContent entity, HomePageData dto) {

        // HERO IMAGES
        if (dto.getHeroImages() != null)
            entity.setHeroImages(dto.getHeroImages());

        // BRAND SECTION
        if (dto.getBrandSection() != null) {
            HomePageContent.BrandDynamicSection bs = new HomePageContent.BrandDynamicSection();
            bs.setTitle(dto.getBrandSection().getTitle());

            if (dto.getBrandSection().getBlocks() != null) {
                List<HomePageContent.BrandBlock> blocks = dto.getBrandSection().getBlocks()
                        .stream()
                        .map(this::toBrandBlockEntity)
                        .collect(Collectors.toList());
                bs.setBlocks(blocks);
            }
            entity.setBrandSection(bs);
        }

        // EVENTS
        if (dto.getEventsSection() != null) {
            HomePageContent.EventsSection es = new HomePageContent.EventsSection();
            es.setTitle(dto.getEventsSection().getTitle());

            if (dto.getEventsSection().getEvents() != null) {
                es.setEvents(dto.getEventsSection().getEvents()
                        .stream().map(this::toEventEntity)
                        .collect(Collectors.toList()));
            }
            entity.setEventsSection(es);
        }

        // ABOUT
        if (dto.getAboutSection() != null) {
            HomePageContent.AboutSection as = new HomePageContent.AboutSection();
            as.setTitle(dto.getAboutSection().getTitle());
            as.setDescription(dto.getAboutSection().getDescription());
            as.setButtonText(dto.getAboutSection().getButtonText());
            as.setButtonLink(dto.getAboutSection().getButtonLink());

            if (dto.getAboutSection().getStats() != null) {
                as.setStats(dto.getAboutSection().getStats()
                        .stream().map(this::toStatEntity)
                        .collect(Collectors.toList()));
            }
            entity.setAboutSection(as);
        }

        // BRAND BANNER
        if (dto.getBrandBanner() != null) {
            HomePageContent.BrandBanner bb = new HomePageContent.BrandBanner();
            bb.setTitle(dto.getBrandBanner().getTitle());
            bb.setSubtitle(dto.getBrandBanner().getSubtitle());
            entity.setBrandBanner(bb);
        }

        // CONTACT
        if (dto.getContactSection() != null) {
            HomePageContent.ContactSection cs = new HomePageContent.ContactSection();
            cs.setReservationPhone(dto.getContactSection().getReservationPhone());
            cs.setHotelPhone(dto.getContactSection().getHotelPhone());
            cs.setEmail(dto.getContactSection().getEmail());
            cs.setCorporateAddress(dto.getContactSection().getCorporateAddress());
            cs.setSupportHours(dto.getContactSection().getSupportHours());

            if (dto.getContactSection().getSocialLinks() != null) {
                cs.setSocialLinks(dto.getContactSection().getSocialLinks()
                        .stream().map(this::toSocialLinkEntity)
                        .collect(Collectors.toList()));
            }
            entity.setContactSection(cs);
        }
    }

    /* ===========================================================
       ENTITY → DTO MAPPER
    ============================================================ */
    private HomePageData toDto(HomePageContent e) {
        HomePageData dto = new HomePageData();

        dto.setHeroImages(e.getHeroImages());
        dto.setBrandSection(toBrandDynamicDto(e.getBrandSection()));
        dto.setEventsSection(toEventsDto(e.getEventsSection()));
        dto.setAboutSection(toAboutDto(e.getAboutSection()));
        dto.setBrandBanner(toBannerDto(e.getBrandBanner()));
        dto.setContactSection(toContactDto(e.getContactSection()));

        return dto;
    }

    /* --- Sub Mappers DTO → ENTITY --- */
    private HomePageContent.BrandBlock toBrandBlockEntity(HomePageData.BrandBlock dto) {
        HomePageContent.BrandBlock b = new HomePageContent.BrandBlock();
        b.setLayout(dto.getLayout());
        b.setText(dto.getText());
        b.setImageUrl(dto.getImageUrl());
        return b;
    }

    private HomePageContent.Event toEventEntity(HomePageData.Event dto) {
        HomePageContent.Event e = new HomePageContent.Event();
        e.setTitle(dto.getTitle());
        e.setDescription(dto.getDescription());
        e.setImageUrl(dto.getImageUrl());
        return e;
    }

    private HomePageContent.Stat toStatEntity(HomePageData.Stat dto) {
        HomePageContent.Stat s = new HomePageContent.Stat();
        s.setValue(dto.getValue());
        s.setLabel(dto.getLabel());
        return s;
    }

    private HomePageContent.SocialLink toSocialLinkEntity(HomePageData.SocialLink dto) {
        HomePageContent.SocialLink s = new HomePageContent.SocialLink();
        s.setName(dto.getName());
        s.setUrl(dto.getUrl());
        return s;
    }

    /* --- Sub Mappers ENTITY → DTO --- */
    private HomePageData.BrandDynamicSection toBrandDynamicDto(HomePageContent.BrandDynamicSection e) {
        if (e == null) return null;
        HomePageData.BrandDynamicSection dto = new HomePageData.BrandDynamicSection();
        dto.setTitle(e.getTitle());

        if (e.getBlocks() != null) {
            dto.setBlocks(e.getBlocks()
                    .stream().map(this::toBrandBlockDto)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    private HomePageData.BrandBlock toBrandBlockDto(HomePageContent.BrandBlock e) {
        HomePageData.BrandBlock dto = new HomePageData.BrandBlock();
        dto.setLayout(e.getLayout());
        dto.setText(e.getText());
        dto.setImageUrl(e.getImageUrl());
        return dto;
    }

    private HomePageData.EventsSection toEventsDto(HomePageContent.EventsSection e) {
        if (e == null) return null;
        HomePageData.EventsSection dto = new HomePageData.EventsSection();
        dto.setTitle(e.getTitle());

        if (e.getEvents() != null) {
            dto.setEvents(e.getEvents()
                    .stream().map(this::toEventDto)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    private HomePageData.Event toEventDto(HomePageContent.Event e) {
        HomePageData.Event dto = new HomePageData.Event();
        dto.setTitle(e.getTitle());
        dto.setDescription(e.getDescription());
        dto.setImageUrl(e.getImageUrl());
        return dto;
    }

    private HomePageData.AboutSection toAboutDto(HomePageContent.AboutSection e) {
        if (e == null) return null;
        HomePageData.AboutSection dto = new HomePageData.AboutSection();
        dto.setTitle(e.getTitle());
        dto.setDescription(e.getDescription());
        dto.setButtonText(e.getButtonText());
        dto.setButtonLink(e.getButtonLink());

        if (e.getStats() != null) {
            dto.setStats(e.getStats()
                    .stream().map(this::toStatDto)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    private HomePageData.Stat toStatDto(HomePageContent.Stat e) {
        HomePageData.Stat dto = new HomePageData.Stat();
        dto.setValue(e.getValue());
        dto.setLabel(e.getLabel());
        return dto;
    }

    private HomePageData.BrandBanner toBannerDto(HomePageContent.BrandBanner e) {
        if (e == null) return null;
        HomePageData.BrandBanner dto = new HomePageData.BrandBanner();
        dto.setTitle(e.getTitle());
        dto.setSubtitle(e.getSubtitle());
        return dto;
    }

    private HomePageData.ContactSection toContactDto(HomePageContent.ContactSection e) {
        if (e == null) return null;
        HomePageData.ContactSection dto = new HomePageData.ContactSection();
        dto.setReservationPhone(e.getReservationPhone());
        dto.setHotelPhone(e.getHotelPhone());
        dto.setEmail(e.getEmail());
        dto.setCorporateAddress(e.getCorporateAddress());
        dto.setSupportHours(e.getSupportHours());

        if (e.getSocialLinks() != null) {
            dto.setSocialLinks(e.getSocialLinks()
                    .stream().map(this::toSocialLinkDto)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    private HomePageData.SocialLink toSocialLinkDto(HomePageContent.SocialLink e) {
        HomePageData.SocialLink dto = new HomePageData.SocialLink();
        dto.setName(e.getName());
        dto.setUrl(e.getUrl());
        return dto;
    }
}
