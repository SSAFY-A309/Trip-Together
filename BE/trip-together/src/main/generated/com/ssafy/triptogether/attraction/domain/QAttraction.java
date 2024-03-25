package com.ssafy.triptogether.attraction.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAttraction is a Querydsl query type for Attraction
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAttraction extends EntityPathBase<Attraction> {

    private static final long serialVersionUID = -1350702830L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAttraction attraction = new QAttraction("attraction");

    public final com.ssafy.triptogether.global.domain.QBaseEntity _super = new com.ssafy.triptogether.global.domain.QBaseEntity(this);

    public final StringPath address = createString("address");

    public final ListPath<AttractionCategory, QAttractionCategory> attractionCategories = this.<AttractionCategory, QAttractionCategory>createList("attractionCategories", AttractionCategory.class, QAttractionCategory.class, PathInits.DIRECT2);

    public final ListPath<AttractionImage, QAttractionImage> attractionImages = this.<AttractionImage, QAttractionImage>createList("attractionImages", AttractionImage.class, QAttractionImage.class, PathInits.DIRECT2);

    public final NumberPath<Double> avgPrice = createNumber("avgPrice", Double.class);

    public final NumberPath<Double> avgRating = createNumber("avgRating", Double.class);

    public final StringPath businessNum = createString("businessNum");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final TimePath<java.time.LocalTime> endAt = createTime("endAt", java.time.LocalTime.class);

    public final ListPath<com.ssafy.triptogether.flashmob.domain.FlashMob, com.ssafy.triptogether.flashmob.domain.QFlashMob> flashMobs = this.<com.ssafy.triptogether.flashmob.domain.FlashMob, com.ssafy.triptogether.flashmob.domain.QFlashMob>createList("flashMobs", com.ssafy.triptogether.flashmob.domain.FlashMob.class, com.ssafy.triptogether.flashmob.domain.QFlashMob.class, PathInits.DIRECT2);

    public final NumberPath<Long> hit = createNumber("hit", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath latitude = createString("latitude");

    public final StringPath longitude = createString("longitude");

    public final StringPath name = createString("name");

    public final QRegion region;

    public final ListPath<com.ssafy.triptogether.review.domain.Review, com.ssafy.triptogether.review.domain.QReview> reviews = this.<com.ssafy.triptogether.review.domain.Review, com.ssafy.triptogether.review.domain.QReview>createList("reviews", com.ssafy.triptogether.review.domain.Review.class, com.ssafy.triptogether.review.domain.QReview.class, PathInits.DIRECT2);

    public final TimePath<java.time.LocalTime> startAt = createTime("startAt", java.time.LocalTime.class);

    public final StringPath thumbnailImageUrl = createString("thumbnailImageUrl");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QAttraction(String variable) {
        this(Attraction.class, forVariable(variable), INITS);
    }

    public QAttraction(Path<? extends Attraction> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAttraction(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAttraction(PathMetadata metadata, PathInits inits) {
        this(Attraction.class, metadata, inits);
    }

    public QAttraction(Class<? extends Attraction> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.region = inits.isInitialized("region") ? new QRegion(forProperty("region")) : null;
    }

}

