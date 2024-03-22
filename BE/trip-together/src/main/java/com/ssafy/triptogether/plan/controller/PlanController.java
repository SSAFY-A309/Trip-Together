package com.ssafy.triptogether.plan.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.triptogether.attraction.data.response.AttractionResponse;
import com.ssafy.triptogether.attraction.domain.Attraction;
import com.ssafy.triptogether.global.data.response.ApiResponse;
import com.ssafy.triptogether.global.data.response.StatusCode;
import com.ssafy.triptogether.plan.data.request.PlansSaveRequest;
import com.ssafy.triptogether.plan.data.request.SearchCondition;
import com.ssafy.triptogether.plan.service.PlanLoadService;
import com.ssafy.triptogether.plan.service.PlanSaveService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/plan/v1")
@RequiredArgsConstructor
public class PlanController {
	// Service
	private final PlanSaveService planSaveService;
	private final PlanLoadService planLoadService;

	@PostMapping("/plans")
	public ResponseEntity<ApiResponse<Void>> plansSave(
		@RequestBody @Valid PlansSaveRequest plansSaveRequest
	) {
		planSaveService.plansSave(1L, plansSaveRequest);

		return ApiResponse.emptyResponse(
			HttpStatus.CREATED, StatusCode.SUCCESS_PLANS_SAVE
		);
	}

	@DeleteMapping("/plans/{plan_id}")
	public ResponseEntity<ApiResponse<Void>> planDelete(
		@PathVariable("plan_id") Long planId
	) {
		planSaveService.planDelete(1L, planId);

		return ApiResponse.emptyResponse(
			HttpStatus.NO_CONTENT, StatusCode.SUCCESS_PLAN_DELETE
		);
	}

	@GetMapping("/attraction")
	public ResponseEntity<ApiResponse<List<AttractionResponse>>> getAttractionList(
		SearchCondition searchCondition
	) {
		List<AttractionResponse> attractionList = planLoadService.search(searchCondition);
		return null;
	}
}
