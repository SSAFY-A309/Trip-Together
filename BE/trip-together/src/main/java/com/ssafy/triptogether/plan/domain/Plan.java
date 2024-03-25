package com.ssafy.triptogether.plan.domain;

import com.ssafy.triptogether.attraction.domain.Region;
import com.ssafy.triptogether.global.domain.BaseEntity;
import com.ssafy.triptogether.global.exception.exceptions.category.BadRequestException;
import com.ssafy.triptogether.global.exception.response.ErrorCode;
import com.ssafy.triptogether.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "plan")
public class Plan extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long id;

    @NotBlank
    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name = "estimated_budget")
    private Double estimatedBudget;

    @Column(name = "real_budget")
    private Double realBudget;

    @Column(name = "start_at")
    private LocalDate startAt;

    @Column(name = "end_at")
    private LocalDate endAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @Builder
    public Plan(String title, Double estimatedBudget, LocalDate startAt, LocalDate endAt, Member member, Region region) {
        this.title = title;
        this.estimatedBudget = estimatedBudget;
        this.startAt = startAt;
        this.endAt = endAt;
        setMember(member);
        setRegion(region);
    }

    @PrePersist
    @PreUpdate
    private void validateStartAndEndDates() {
        if (endAt.isBefore(startAt)) {
            throw new BadRequestException("PlanStartAndEndDates", ErrorCode.PLAN_DATE_BAD_REQUEST);
        }
    }

    public void setRegion(Region region) {
        this.region = region;
        region.getPlans().add(this);
    }

    public void setMember(Member member) {
        this.member = member;
        member.getPlans().add(this);
    }
}
