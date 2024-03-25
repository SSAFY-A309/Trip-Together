package com.ssafy.triptogether.tripaccount.domain;

import com.ssafy.triptogether.global.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "currency",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {
                        "code",
                        "unit",
                        "currency_nation"
                })
        }
)
public class Currency extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "currency_id")
    private Long id;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @Column(name = "code")
    private CurrencyCode code;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @Column(name = "currency_nation")
    private CurrencyNation currencyNation;

    @NotBlank
    @Column(name = "rate")
    private Double rate;

    @Builder
    public Currency(CurrencyCode code, CurrencyNation currencyNation, Double rate) {
        this.code = code;
        this.currencyNation = currencyNation;
        this.rate = rate;
    }

    public void updateRate(Double newRate) {
        this.rate = newRate;
    }
}
