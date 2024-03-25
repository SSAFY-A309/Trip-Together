package com.ssafy.triptogether.plan.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlan is a Querydsl query type for Plan
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlan extends EntityPathBase<Plan> {

    private static final long serialVersionUID = 862672854L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlan plan = new QPlan("plan");

    public final com.ssafy.triptogether.global.domain.QBaseEntity _super = new com.ssafy.triptogether.global.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DatePath<java.time.LocalDate> endAt = createDate("endAt", java.time.LocalDate.class);

    public final NumberPath<Double> estimatedBudget = createNumber("estimatedBudget", Double.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.ssafy.triptogether.member.domain.QMember member;

    public final NumberPath<Double> realBudget = createNumber("realBudget", Double.class);

    public final com.ssafy.triptogether.attraction.domain.QRegion region;

    public final DatePath<java.time.LocalDate> startAt = createDate("startAt", java.time.LocalDate.class);

    public final StringPath title = createString("title");

    public QPlan(String variable) {
        this(Plan.class, forVariable(variable), INITS);
    }

    public QPlan(Path<? extends Plan> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlan(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlan(PathMetadata metadata, PathInits inits) {
        this(Plan.class, metadata, inits);
    }

    public QPlan(Class<? extends Plan> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.ssafy.triptogether.member.domain.QMember(forProperty("member")) : null;
        this.region = inits.isInitialized("region") ? new com.ssafy.triptogether.attraction.domain.QRegion(forProperty("region")) : null;
    }

}

