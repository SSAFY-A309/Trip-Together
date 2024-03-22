package com.ssafy.triptogether.global.util;

import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

public interface NativeSqlCreator {
	NumberExpression<Double> createCalcDistanceSQL(Double longitudeCond, Double latitudeCond, StringPath dbLongitude, StringPath dbLatitude);
}
