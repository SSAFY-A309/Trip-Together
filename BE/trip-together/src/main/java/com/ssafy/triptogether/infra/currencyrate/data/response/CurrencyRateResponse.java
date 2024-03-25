package com.ssafy.triptogether.infra.currencyrate.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record CurrencyRateResponse(
        @JsonProperty("result")
        Integer result,
        @JsonProperty("cur_unit")
        String cur_unit,
        @JsonProperty("cur_nm")
        String cur_nm,
        @JsonProperty("ttb")
        String ttb,
        @JsonProperty("tts")
        String tts,
        @JsonProperty("deal_bas_r")
        String dealBasR,
        @JsonProperty("bkpr")
        String bkpr,
        @JsonProperty("yy_efee_r")
        String yyEfeeR,
        @JsonProperty("ten_dd_efee_r")
        String tenDdEfeeR,
        @JsonProperty("kftc_deal_bas_r")
        String kftcDealBasR,
        @JsonProperty("kftc_bkpr")
        String kftcBkpr
) {
}
