package com.ssafy.triptogether.plan.repository;

import com.ssafy.triptogether.plan.domain.document.DailyPlans;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DailyPlansRepository extends MongoRepository<DailyPlans, String> {
}
