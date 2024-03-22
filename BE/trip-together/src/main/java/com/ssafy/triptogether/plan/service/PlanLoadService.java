package com.ssafy.triptogether.plan.service;

import java.util.List;

import com.ssafy.triptogether.attraction.data.response.AttractionResponse;
import com.ssafy.triptogether.plan.data.request.SearchCondition;

public interface PlanLoadService {
	List<AttractionResponse> search(SearchCondition searchCondition);
}
