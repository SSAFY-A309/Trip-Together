package com.ssafy.triptogether.plan.domain.document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "daily_plans")
public class DailyPlans {
    @Id
    private String id;
    @Field("daily_plans")
    List<DailyPlan> dailyPlans;
    @Field("plan_id")
    private Long planId;

    @Builder
    public DailyPlans(Long planId, List<DailyPlan> dailyPlans) {
        this.planId = planId;
        this.dailyPlans = dailyPlans;
    }
}
