package com.ssafy.triptogether.plan.domain;

import lombok.Getter;

@Getter
public enum Status {
	BEFORE("before"),
	IN_PROGRESS("in_progress"),
	AFTER("done");

	private final String message;

	Status(String message) {
		this.message = message;
	}
}
