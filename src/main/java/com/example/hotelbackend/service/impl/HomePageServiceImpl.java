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

    /* --------------------------------------------------------
       Initialize DB with default content on first run
    ---------------------------------------------------------*/
    @PostConstruct
    public void init() {
        if (repository.count() == 0) {
            HomePageContent content = new HomePageContent();

            // HERO IMAGES (match your Home.jsx ORIGINAL_IMAGES)
            content.setHeroImages(List.of(
                    "/assets/hero1.png",
                    "/assets/hero2.png",
                    "/assets/brand-2.png",
                    "/assets/hero2.png",
                    "/assets/brand-2.png"
            ));

            // BRAND SECTION - dynamic blocks
            HomePageContent.BrandDynamicSection brandSection = new HomePageContent.BrandDynamicSection();
            brandSection.setTitle("OUR BRANDS");

            HomePageContent.BrandBlock block1 = new HomePageContent.BrandBlock();
            block1.setLayout("text-left-image-right");
            block1.setText(
                    "Experience the pinnacle of refined Indian luxury at Pride Plaza, our upscale brand created for high-income individuals, " +
                            "C-suite executives, and elite leisure travellers. Combining timeless Indian traditions with sophisticated contemporary design, " +
                            "Pride Plaza delivers world-class hospitality, bespoke services, and a setting that celebrates elegance."
            );
            block1.setImageUrl("/assets/brand-1.png");

            HomePageContent.BrandBlock block2 = new HomePageContent.BrandBlock();
            block2.setLayout("image-left-text-right");
            block2.setText(
                    "Dynamic, stylish, and connected to India’s evolving urban lifestyle, Pride Premier is our upper midscale brand designed " +
                            "for affluent business executives and high-income families."
            );
            block2.setImageUrl("/assets/brand-1.png");

            HomePageContent.BrandBlock block3 = new HomePageContent.BrandBlock();
            block3.setLayout("text-left-image-right");
            block3.setText(
                    "Focused on accessibility, convenience, and premium value, Pride Premier blends contemporary comfort with personalised " +
                            "amenities to meet the needs of guests who seamlessly combine work and leisure."
            );
            block3.setImageUrl("/assets/brand-1.png");

            brandSection.setBlocks(List.of(block1, block2, block3));
            content.setBrandSection(brandSection);

            // EVENTS SECTION
            HomePageContent.EventsSection eventsSection = new HomePageContent.EventsSection();
            eventsSection.setTitle("UPCOMING EVENTS");

            HomePageContent.Event e1 = new HomePageContent.Event();
            e1.setTitle("Event 1");
            e1.setDescription("Event description");
            e1.setImageUrl("/assets/event1.jpg");

            eventsSection.setEvents(List.of(e1));
            content.setEventsSection(eventsSection);

            // ABOUT SECTION
            HomePageContent.AboutSection aboutSection = new HomePageContent.AboutSection();
            aboutSection.setTitle("ABOUT US");
            aboutSection.setDescription("About us description");
            aboutSection.setButtonText("Learn More");
            aboutSection.setButtonLink("/about");
            content.setAboutSection(aboutSection);

            // BRAND BANNER
            HomePageContent.BrandBanner banner = new HomePageContent.BrandBanner();
            banner.setTitle("TRULY INDIAN. TRADITIONALLY LUXURIOUS.");
            banner.setSubtitle("Leading Hotel Chain Group in India");
            content.setBrandBanner(banner);

            // CONTACT SECTION (Option C)
            HomePageContent.ContactSection contact = new HomePageContent.ContactSection();
            contact.setReservationPhone("1800 209 1400");
            contact.setHotelPhone("+91 9876543210");
            contact.setEmail("reservations@hotel.com");
            contact.setCorporateAddress("Corporate Office, XYZ Plaza, Mumbai");
            contact.setSupportHours("24x7");

            HomePageContent.SocialLink fb = new HomePageContent.SocialLink();
            fb.setName("facebook");
            fb.setUrl("https://facebook.com/hotel");

            HomePageContent.SocialLink ig = new HomePageContent.SocialLink();
            ig.setName("instagram");
            ig.setUrl("https://instagram.com/hotel");

            HomePageContent.SocialLink li = new HomePageContent.SocialLink();
            li.setName("linkedin");
            li.setUrl("https://linkedin.com/company/hotel");

            contact.setSocialLinks(List.of(fb, ig, li));
            content.setContactSection(contact);

            repository.save(content);
        }
    }

    /* --------------------------------------------------------
        Fetch Home Page Data
    ---------------------------------------------------------*/
    @Override
    public HomePageData getHomePageData() {
        return repository.findFirstBy()
                .map(this::toDto)
                .orElseGet(HomePageData::new);
    }

    /* --------------------------------------------------------
        Update Home Page Data
    ---------------------------------------------------------*/
    @Override
    public HomePageData updateHomePageData(HomePageData dto) {
        HomePageContent entity = repository.findFirstBy()
                .orElseGet(HomePageContent::new);

        updateEntityFromDto(entity, dto);
        HomePageContent saved = repository.save(entity);
        return toDto(saved);
    }

    /* --------------------------------------------------------
        Map DTO → ENTITY
    ---------------------------------------------------------*/
    private void updateEntityFromDto(HomePageContent entity, HomePageData dto) {
        if (dto == null) return;

        // Hero images
        entity.setHeroImages(dto.getHeroImages());

        // Brand Section (dynamic blocks)
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

        // Events Section
        if (dto.getEventsSection() != null) {
            HomePageContent.EventsSection es = new HomePageContent.EventsSection();
            es.setTitle(dto.getEventsSection().getTitle());

            if (dto.getEventsSection().getEvents() != null) {
                List<HomePageContent.Event> events = dto.getEventsSection().getEvents()
                        .stream().map(this::toEventEntity)
                        .collect(Collectors.toList());
                es.setEvents(events);
            }
            entity.setEventsSection(es);
        }

        // About Section
        if (dto.getAboutSection() != null) {
            HomePageContent.AboutSection as = new HomePageContent.AboutSection();
            as.setTitle(dto.getAboutSection().getTitle());
            as.setDescription(dto.getAboutSection().getDescription());
            as.setButtonText(dto.getAboutSection().getButtonText());
            as.setButtonLink(dto.getAboutSection().getButtonLink());

            if (dto.getAboutSection().getStats() != null) {
                List<HomePageContent.Stat> stats = dto.getAboutSection().getStats()
                        .stream().map(this::toStatEntity)
                        .collect(Collectors.toList());
                as.setStats(stats);
            }

            entity.setAboutSection(as);
        }

        // Brand Banner
        if (dto.getBrandBanner() != null) {
            HomePageContent.BrandBanner bb = new HomePageContent.BrandBanner();
            bb.setTitle(dto.getBrandBanner().getTitle());
            bb.setSubtitle(dto.getBrandBanner().getSubtitle());
            entity.setBrandBanner(bb);
        }

        // Contact Section
        if (dto.getContactSection() != null) {
            HomePageContent.ContactSection cs = new HomePageContent.ContactSection();
            cs.setReservationPhone(dto.getContactSection().getReservationPhone());
            cs.setHotelPhone(dto.getContactSection().getHotelPhone());
            cs.setEmail(dto.getContactSection().getEmail());
            cs.setCorporateAddress(dto.getContactSection().getCorporateAddress());
            cs.setSupportHours(dto.getContactSection().getSupportHours());

            if (dto.getContactSection().getSocialLinks() != null) {
                List<HomePageContent.SocialLink> links = dto.getContactSection().getSocialLinks()
                        .stream().map(this::toSocialLinkEntity)
                        .collect(Collectors.toList());
                cs.setSocialLinks(links);
            }

            entity.setContactSection(cs);
        }
    }

    /* --------------------------------------------------------
        Map ENTITY → DTO
    ---------------------------------------------------------*/
    private HomePageData toDto(HomePageContent entity) {
        HomePageData dto = new HomePageData();

        dto.setHeroImages(entity.getHeroImages());

        // Brand Section (dynamic blocks)
        if (entity.getBrandSection() != null) {
            HomePageData.BrandDynamicSection bs = new HomePageData.BrandDynamicSection();
            bs.setTitle(entity.getBrandSection().getTitle());

            if (entity.getBrandSection().getBlocks() != null) {
                List<HomePageData.BrandBlock> blocks = entity.getBrandSection().getBlocks()
                        .stream()
                        .map(this::toBrandBlockDto)
                        .collect(Collectors.toList());
                bs.setBlocks(blocks);
            }

            dto.setBrandSection(bs);
        }

        // Events Section
        if (entity.getEventsSection() != null) {
            HomePageData.EventsSection es = new HomePageData.EventsSection();
            es.setTitle(entity.getEventsSection().getTitle());

            if (entity.getEventsSection().getEvents() != null) {
                List<HomePageData.Event> events = entity.getEventsSection().getEvents()
                        .stream().map(this::toEventDto)
                        .collect(Collectors.toList());
                es.setEvents(events);
            }
            dto.setEventsSection(es);
        }

        // About Section
        if (entity.getAboutSection() != null) {
            HomePageData.AboutSection as = new HomePageData.AboutSection();
            as.setTitle(entity.getAboutSection().getTitle());
            as.setDescription(entity.getAboutSection().getDescription());
            as.setButtonText(entity.getAboutSection().getButtonText());
            as.setButtonLink(entity.getAboutSection().getButtonLink());

            if (entity.getAboutSection().getStats() != null) {
                List<HomePageData.Stat> stats = entity.getAboutSection().getStats()
                        .stream().map(this::toStatDto)
                        .collect(Collectors.toList());
                as.setStats(stats);
            }
            dto.setAboutSection(as);
        }

        // Brand Banner
        if (entity.getBrandBanner() != null) {
            HomePageData.BrandBanner bb = new HomePageData.BrandBanner();
            bb.setTitle(entity.getBrandBanner().getTitle());
            bb.setSubtitle(entity.getBrandBanner().getSubtitle());
            dto.setBrandBanner(bb);
        }

        // Contact Section
        if (entity.getContactSection() != null) {
            HomePageData.ContactSection cs = new HomePageData.ContactSection();
            cs.setReservationPhone(entity.getContactSection().getReservationPhone());
            cs.setHotelPhone(entity.getContactSection().getHotelPhone());
            cs.setEmail(entity.getContactSection().getEmail());
            cs.setCorporateAddress(entity.getContactSection().getCorporateAddress());
            cs.setSupportHours(entity.getContactSection().getSupportHours());

            if (entity.getContactSection().getSocialLinks() != null) {
                List<HomePageData.SocialLink> links = entity.getContactSection().getSocialLinks()
                        .stream().map(this::toSocialLinkDto)
                        .collect(Collectors.toList());
                cs.setSocialLinks(links);
            }

            dto.setContactSection(cs);
        }

        return dto;
    }

    /* --------------------------------------------------------
        Converters DTO → Entity
    ---------------------------------------------------------*/
    private HomePageContent.BrandBlock toBrandBlockEntity(HomePageData.BrandBlock dto) {
        HomePageContent.BrandBlock e = new HomePageContent.BrandBlock();
        e.setLayout(dto.getLayout());
        e.setText(dto.getText());
        e.setImageUrl(dto.getImageUrl());
        return e;
    }

    private HomePageContent.Event toEventEntity(HomePageData.Event dto) {
        HomePageContent.Event e = new HomePageContent.Event();
        e.setTitle(dto.getTitle());
        e.setDescription(dto.getDescription());
        e.setImageUrl(dto.getImageUrl());
        return e;
    }

    private HomePageContent.Stat toStatEntity(HomePageData.Stat dto) {
        HomePageContent.Stat e = new HomePageContent.Stat();
        e.setValue(dto.getValue());
        e.setLabel(dto.getLabel());
        return e;
    }

    private HomePageContent.SocialLink toSocialLinkEntity(HomePageData.SocialLink dto) {
        HomePageContent.SocialLink e = new HomePageContent.SocialLink();
        e.setName(dto.getName());
        e.setUrl(dto.getUrl());
        return e;
    }

    /* --------------------------------------------------------
        Converters Entity → DTO
    ---------------------------------------------------------*/
    private HomePageData.BrandBlock toBrandBlockDto(HomePageContent.BrandBlock entity) {
        HomePageData.BrandBlock dto = new HomePageData.BrandBlock();
        dto.setLayout(entity.getLayout());
        dto.setText(entity.getText());
        dto.setImageUrl(entity.getImageUrl());
        return dto;
    }

    private HomePageData.Event toEventDto(HomePageContent.Event entity) {
        HomePageData.Event dto = new HomePageData.Event();
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setImageUrl(entity.getImageUrl());
        return dto;
    }

    private HomePageData.Stat toStatDto(HomePageContent.Stat entity) {
        HomePageData.Stat dto = new HomePageData.Stat();
        dto.setValue(entity.getValue());
        dto.setLabel(entity.getLabel());
        return dto;
    }

    private HomePageData.SocialLink toSocialLinkDto(HomePageContent.SocialLink entity) {
        HomePageData.SocialLink dto = new HomePageData.SocialLink();
        dto.setName(entity.getName());
        dto.setUrl(entity.getUrl());
        return dto;
    }
}
