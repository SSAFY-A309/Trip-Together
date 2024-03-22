package com.ssafy.triptogether.global.util;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.StringPath;

@Component
public class MysqlNativeSqlCreator implements NativeSqlCreator {
	public NumberExpression<Double> createCalcDistanceSQL(Double latitudeCond, Double longitudeCond,
		StringPath dbLatitude, StringPath dbLongitude) {
		return Expressions.numberTemplate(Double.class,"ST_Distance_Sphere({0}, {1})",
			Expressions.stringTemplate("POINT({0}, {1})",
				latitudeCond,
				longitudeCond
			),
			Expressions.stringTemplate("POINT({0}, {1})",
				dbLatitude,
				dbLongitude
			)
		);
	}
}
