package com.ssafy.triptogether.attraction.repository.query;

import java.util.List;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.triptogether.attraction.data.response.AttractionResponse;
import com.ssafy.triptogether.global.util.NativeSqlCreator;
import com.ssafy.triptogether.plan.data.request.SearchCondition;

import lombok.RequiredArgsConstructor;

import static com.ssafy.triptogether.attraction.domain.QAttraction.*;

@RequiredArgsConstructor
public class AttractionRepositoryCustomImpl implements AttractionRepositoryCustom{

	private final JPAQueryFactory queryFactory;
	private final NativeSqlCreator mysqlNativeSQLCreator;
	@Override
	public List<AttractionResponse> search(SearchCondition searchCondition) {
		// TODO: 검색 수정
		List<AttractionResponse> attractionList;
		if (searchCondition.longitude_delta() == null) {
			attractionList = queryFactory.select(Projections.constructor(AttractionResponse.class,
				attraction.id,
				attraction.thumbnailImageUrl,
				attraction.name,
				attraction.address,
				attraction.avgRating,
				attraction.avgPrice,
				attraction.latitude,
				attraction.longitude,
				getCalcDistanceNativeSQL(searchCondition).as("distance")
				)
			)
				.from(attraction)
				.where(attraction.name.like("%"+searchCondition.keyword()+"%"))
				// TODO: 두번 계산한다면 수정
				.orderBy(getCalcDistanceNativeSQL(searchCondition).asc())
				.fetch()
				;
			return attractionList;
		}
		return null;
	}

	private NumberExpression<Double> getCalcDistanceNativeSQL(SearchCondition searchCondition) {
		return mysqlNativeSQLCreator.createCalcDistanceSQL(
			searchCondition.longitude(), searchCondition.latitude(),
			attraction.longitude, attraction.latitude
		);
	}
}
