package com.ssafy.triptogether.attraction.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.triptogether.flashmob.domain.FlashMob;
import com.ssafy.triptogether.global.domain.BaseEntity;
import com.ssafy.triptogether.review.domain.Review;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "attraction")
public class Attraction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attraction_id")
    private Long id;

    @NotBlank
    @Column(name = "business_num")
    private String businessNum;

    @Column(name = "thumbnail_image_url", length = 3000)
    private String thumbnailImageUrl;

    @NotBlank
    @Column(name = "address")
    private String address;

    @NotBlank
    @Column(name = "start_at")
    private LocalTime startAt;

    @NotBlank
    @Column(name = "end_at")
    private LocalTime endAt;

    @NotBlank
    @Column(name = "latitude")
    private String latitude;

    @NotBlank
    @Column(name = "longitude")
    private String longitude;

    @NotBlank
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "avg_rating")
    private Double avgRating;

    @Column(name = "avg_price")
    private Double avgPrice;

    @Column(name = "hit")
    private Long hit;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @JsonIgnore
    @OneToMany(mappedBy = "attraction", cascade = CascadeType.ALL)
    private List<AttractionImage> attractionImages = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "attraction", cascade = CascadeType.ALL)
    private List<AttractionCategory> attractionCategories = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "attraction", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "attraction", cascade = CascadeType.ALL)
    private List<FlashMob> flashMobs = new ArrayList<>();

    @Builder
    public Attraction(String businessNum, String thumbnailImageUrl, String address, LocalTime startAt, LocalTime endAt, String latitude, String longitude, String name, Region region) {
        this.businessNum = businessNum;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.address = address;
        this.startAt = startAt;
        this.endAt = endAt;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        setRegion(region);
    }

    public void setRegion(Region region) {
        this.region = region;
        region.getAttractions().add(this);
    }
}
