package com.ssafy.triptogether.attraction.repository.query;

import java.util.List;

import com.ssafy.triptogether.attraction.data.response.AttractionResponse;
import com.ssafy.triptogether.plan.data.request.SearchCondition;

public interface AttractionRepositoryCustom {
	List<AttractionResponse> search(SearchCondition searchCondition);
}
