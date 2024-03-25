package com.ssafy.triptogether.plan.controller;

import com.ssafy.triptogether.auth.utils.SecurityMember;
import com.ssafy.triptogether.global.data.response.ApiResponse;
import com.ssafy.triptogether.global.data.response.StatusCode;
import com.ssafy.triptogether.plan.data.request.PlansSaveRequest;
import com.ssafy.triptogether.plan.data.response.PlanDetailFindResponse;
import com.ssafy.triptogether.plan.service.PlanLoadService;
import com.ssafy.triptogether.plan.service.PlanSaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.ssafy.triptogether.global.data.response.StatusCode.SUCCESS_PLANS_SAVE;
import static com.ssafy.triptogether.global.data.response.StatusCode.SUCCESS_PLAN_DETAIL_FIND;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/plan/v1/plans")
@RequiredArgsConstructor
public class PlanController {
    // Service
    private final PlanSaveService planSaveService;
    private final PlanLoadService planLoadService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> plansSave(
            @AuthenticationPrincipal SecurityMember securityMember,
            @RequestBody @Valid PlansSaveRequest plansSaveRequest
    ) {
        long memberId = securityMember.getId();
        planSaveService.plansSave(memberId, plansSaveRequest);

        return ApiResponse.emptyResponse(CREATED, SUCCESS_PLANS_SAVE);
    }

    @DeleteMapping("/{plan_id}")
    public ResponseEntity<ApiResponse<Void>> planDelete(
            @AuthenticationPrincipal SecurityMember securityMember,
            @PathVariable("plan_id") Long planId
    ) {
        long memberId = securityMember.getId();
        planSaveService.planDelete(memberId, planId);

        return ApiResponse.emptyResponse(
                HttpStatus.NO_CONTENT, StatusCode.SUCCESS_PLAN_DELETE
        );
    }

    @GetMapping("/{plan_id}")
    public ResponseEntity<ApiResponse<PlanDetailFindResponse>> findPlanDetail(
            @PathVariable("plan_id") long planId
    ) {
        PlanDetailFindResponse response = planLoadService.findPlanDetail(planId);
        return ApiResponse.toResponseEntity(OK, SUCCESS_PLAN_DETAIL_FIND, response);
    }
}
