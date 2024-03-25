package com.ssafy.triptogether.plan.domain.document;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Document(collation = "daily_plans")
public class DailyPlans {
    private final Long planId;
    List<DailyPlan> dailyPlans;
    @Id
    private String id;

    @Builder
    public DailyPlans(Long planId, List<DailyPlan> dailyPlans) {
        this.planId = planId;
        this.dailyPlans = dailyPlans;
    }
}
