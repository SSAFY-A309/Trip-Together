package com.ssafy.triptogether.tripaccount.domain;

import lombok.Getter;

@Getter
public enum CurrencyCode {
    GBP(163),
    EUR(8356);

    private final Integer unit;

    CurrencyCode(Integer message) {
        this.unit = message;
    }

    public static CurrencyCode fromString(String code) {
        for (CurrencyCode currencyCode : CurrencyCode.values()) {
            if (currencyCode.name().equals(code)) {
                return currencyCode;
            }
        }
        return null;
    }
}
