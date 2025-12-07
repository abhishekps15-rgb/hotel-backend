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
       INITIALIZE DEFAULT DATA ON FIRST RUN (SAFE FALLBACK)
    ============================================================ */
    @PostConstruct
    public void init() {
        if (repository.count() > 0) return;

        HomePageContent content = new HomePageContent();

        // HERO IMAGES (match Home.jsx)
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
        b1.setText("Experience the pinnacle of refined Indian luxury at Pride Plaza, our upscale brand created for high-income individuals, C-suite executives, and elite leisure travellers.");
        b1.setImageUrl("/assets/brand-1.png");

        HomePageContent.BrandBlock b2 = new HomePageContent.BrandBlock();
        b2.setLayout("image-left-text-right");
        b2.setText("Dynamic, stylish, and connected to India’s evolving urban lifestyle, Pride Premier is our upper midscale brand designed for affluent business executives and high-income families.");
        b2.setImageUrl("/assets/brand-1.png");

        HomePageContent.BrandBlock b3 = new HomePageContent.BrandBlock();
        b3.setLayout("text-left-image-right");
        b3.setText("Focused on accessibility, convenience, and premium value, Pride Premier blends contemporary comfort with personalised amenities.");
        b3.setImageUrl("/assets/brand-1.png");

        bs.setBlocks(List.of(b1, b2, b3));
        content.setBrandSection(bs);

        // EVENTS
        HomePageContent.EventsSection es = new HomePageContent.EventsSection();
        es.setTitle("PLAN YOUR EVENTS");

        HomePageContent.Event e1 = new HomePageContent.Event();
        e1.setTitle("Woyage - Daycations");
        e1.setDescription("Replenish your spirit as you escape into your world of serenity...");
        e1.setImageUrl("/assets/g1.png");

        HomePageContent.Event e2 = new HomePageContent.Event();
        e2.setTitle("Luxury Escapes");
        e2.setDescription("Unwind in curated luxurious settings crafted just for unforgettable experiences.");
        e2.setImageUrl("/assets/g2.png");

        HomePageContent.Event e3 = new HomePageContent.Event();
        e3.setTitle("Offers & Promotions");
        e3.setDescription("Exclusive seasonal offers crafted just for you.");
        e3.setImageUrl("/assets/g3.png");

        es.setEvents(List.of(e1, e2, e3));
        content.setEventsSection(es);

        // ABOUT
        HomePageContent.AboutSection as = new HomePageContent.AboutSection();
        as.setTitle("ABOUT US");
        as.setDescription("Since 2016, we've been helping travelers find stays they love — effortlessly. " +
                "We're about curating unforgettable journeys! Our passionate team blends seamless technology with a love for discovery.");
        as.setButtonText("Know More →");
        as.setButtonLink("/about");

        HomePageContent.Stat s1 = new HomePageContent.Stat();
        s1.setValue("98%+");
        s1.setLabel("Positive Feedback");

        HomePageContent.Stat s2 = new HomePageContent.Stat();
        s2.setValue("15+");
        s2.setLabel("Years of Expertise");

        HomePageContent.Stat s3 = new HomePageContent.Stat();
        s3.setValue("25K+");
        s3.setLabel("Happy Guests");

        as.setStats(List.of(s1, s2, s3));
        content.setAboutSection(as);

        // BRAND BANNER + CONTACTS
        HomePageContent.BrandBanner bb = new HomePageContent.BrandBanner();
        bb.setTitle("TRULY INDIAN. TRADITIONALLY LUXURIOUS.");
        bb.setSubtitle("Leading Hotel Chain Group in India");

        HomePageContent.ContactInfo c1 = new HomePageContent.ContactInfo();
        c1.setType("phone");
        c1.setValue("18002091400");
        c1.setDisplayValue("1800 209 1400");

        HomePageContent.ContactInfo c2 = new HomePageContent.ContactInfo();
        c2.setType("email");
        c2.setValue("centralreservations@hrchotel.com");
        c2.setDisplayValue("centralreservations@hrchotel.com");

        bb.setContacts(List.of(c1, c2));
        content.setBrandBanner(bb);

        // CONTACT SECTION
        HomePageContent.ContactSection cs = new HomePageContent.ContactSection();
        cs.setReservationPhone("1800 209 1400");
        cs.setHotelPhone("+91 9876543210");
        cs.setEmail("centralreservations@hrchotel.com");
        cs.setCorporateAddress("Corporate Office, Mumbai");
        cs.setSupportHours("24x7");

        HomePageContent.SocialLink fb = new HomePageContent.SocialLink();
        fb.setName("facebook");
        fb.setUrl("https://facebook.com/ihc");

        HomePageContent.SocialLink ig = new HomePageContent.SocialLink();
        ig.setName("instagram");
        ig.setUrl("https://instagram.com/ihc");

        cs.setSocialLinks(List.of(fb, ig));
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
       DTO → ENTITY (PARTIAL UPDATE)
    ============================================================ */
    private void updateEntityFromDto(HomePageContent entity, HomePageData dto) {
        if (dto == null) return;

        // HERO IMAGES
        if (dto.getHeroImages() != null) {
            entity.setHeroImages(dto.getHeroImages());
        }

        // BRAND SECTION (BrandDynamicSection)
        if (dto.getBrandSection() != null) {
            HomePageContent.BrandDynamicSection existing =
                    entity.getBrandSection() != null ? entity.getBrandSection() : new HomePageContent.BrandDynamicSection();

            if (dto.getBrandSection().getTitle() != null) {
                existing.setTitle(dto.getBrandSection().getTitle());
            }

            if (dto.getBrandSection().getBlocks() != null) {
                List<HomePageContent.BrandBlock> blocks = dto.getBrandSection().getBlocks()
                        .stream()
                        .map(this::toBrandBlockEntity)
                        .collect(Collectors.toList());
                existing.setBlocks(blocks);
            }

            entity.setBrandSection(existing);
        }

        // EVENTS SECTION
        if (dto.getEventsSection() != null) {
            HomePageContent.EventsSection existing =
                    entity.getEventsSection() != null ? entity.getEventsSection() : new HomePageContent.EventsSection();

            if (dto.getEventsSection().getTitle() != null) {
                existing.setTitle(dto.getEventsSection().getTitle());
            }

            if (dto.getEventsSection().getEvents() != null) {
                existing.setEvents(
                        dto.getEventsSection().getEvents()
                                .stream()
                                .map(this::toEventEntity)
                                .collect(Collectors.toList())
                );
            }

            entity.setEventsSection(existing);
        }

        // ABOUT SECTION
        if (dto.getAboutSection() != null) {
            HomePageContent.AboutSection existing =
                    entity.getAboutSection() != null ? entity.getAboutSection() : new HomePageContent.AboutSection();

            if (dto.getAboutSection().getTitle() != null) {
                existing.setTitle(dto.getAboutSection().getTitle());
            }
            if (dto.getAboutSection().getDescription() != null) {
                existing.setDescription(dto.getAboutSection().getDescription());
            }
            if (dto.getAboutSection().getButtonText() != null) {
                existing.setButtonText(dto.getAboutSection().getButtonText());
            }
            if (dto.getAboutSection().getButtonLink() != null) {
                existing.setButtonLink(dto.getAboutSection().getButtonLink());
            }

            if (dto.getAboutSection().getStats() != null) {
                existing.setStats(
                        dto.getAboutSection().getStats()
                                .stream()
                                .map(this::toStatEntity)
                                .collect(Collectors.toList())
                );
            }

            entity.setAboutSection(existing);
        }

        // BRAND BANNER (including contacts)
        if (dto.getBrandBanner() != null) {
            HomePageContent.BrandBanner existing =
                    entity.getBrandBanner() != null ? entity.getBrandBanner() : new HomePageContent.BrandBanner();

            if (dto.getBrandBanner().getTitle() != null) {
                existing.setTitle(dto.getBrandBanner().getTitle());
            }
            if (dto.getBrandBanner().getSubtitle() != null) {
                existing.setSubtitle(dto.getBrandBanner().getSubtitle());
            }

            if (dto.getBrandBanner().getContacts() != null) {
                existing.setContacts(
                        dto.getBrandBanner().getContacts()
                                .stream()
                                .map(this::toContactInfoEntity)
                                .collect(Collectors.toList())
                );
            }

            entity.setBrandBanner(existing);
        }

        // CONTACT SECTION
        if (dto.getContactSection() != null) {
            HomePageContent.ContactSection existing =
                    entity.getContactSection() != null ? entity.getContactSection() : new HomePageContent.ContactSection();

            if (dto.getContactSection().getReservationPhone() != null) {
                existing.setReservationPhone(dto.getContactSection().getReservationPhone());
            }
            if (dto.getContactSection().getHotelPhone() != null) {
                existing.setHotelPhone(dto.getContactSection().getHotelPhone());
            }
            if (dto.getContactSection().getEmail() != null) {
                existing.setEmail(dto.getContactSection().getEmail());
            }
            if (dto.getContactSection().getCorporateAddress() != null) {
                existing.setCorporateAddress(dto.getContactSection().getCorporateAddress());
            }
            if (dto.getContactSection().getSupportHours() != null) {
                existing.setSupportHours(dto.getContactSection().getSupportHours());
            }

            if (dto.getContactSection().getSocialLinks() != null) {
                existing.setSocialLinks(
                        dto.getContactSection().getSocialLinks()
                                .stream()
                                .map(this::toSocialLinkEntity)
                                .collect(Collectors.toList())
                );
            }

            entity.setContactSection(existing);
        }
    }

    /* ===========================================================
       ENTITY → DTO
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

    /* ===========================================================
       DTO → ENTITY HELPERS
    ============================================================ */
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

    private HomePageContent.ContactInfo toContactInfoEntity(HomePageData.ContactInfo dto) {
        HomePageContent.ContactInfo ci = new HomePageContent.ContactInfo();
        ci.setType(dto.getType());
        ci.setValue(dto.getValue());
        ci.setDisplayValue(dto.getDisplayValue());
        return ci;
    }

    /* ===========================================================
       ENTITY → DTO HELPERS
    ============================================================ */
    private HomePageData.BrandDynamicSection toBrandDynamicDto(HomePageContent.BrandDynamicSection e) {
        if (e == null) return null;
        HomePageData.BrandDynamicSection dto = new HomePageData.BrandDynamicSection();
        dto.setTitle(e.getTitle());

        if (e.getBlocks() != null) {
            dto.setBlocks(
                    e.getBlocks()
                            .stream()
                            .map(this::toBrandBlockDto)
                            .collect(Collectors.toList())
            );
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
            dto.setEvents(
                    e.getEvents()
                            .stream()
                            .map(this::toEventDto)
                            .collect(Collectors.toList())
            );
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
            dto.setStats(
                    e.getStats()
                            .stream()
                            .map(this::toStatDto)
                            .collect(Collectors.toList())
            );
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

        if (e.getContacts() != null) {
            dto.setContacts(
                    e.getContacts()
                            .stream()
                            .map(this::toContactInfoDto)
                            .collect(Collectors.toList())
            );
        }
        return dto;
    }

    private HomePageData.ContactInfo toContactInfoDto(HomePageContent.ContactInfo e) {
        HomePageData.ContactInfo dto = new HomePageData.ContactInfo();
        dto.setType(e.getType());
        dto.setValue(e.getValue());
        dto.setDisplayValue(e.getDisplayValue());
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
            dto.setSocialLinks(
                    e.getSocialLinks()
                            .stream()
                            .map(this::toSocialLinkDto)
                            .collect(Collectors.toList())
            );
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
