package com.ssafy.triptogether.tripaccount.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.triptogether.global.domain.BaseEntity;
import com.ssafy.triptogether.global.exception.exceptions.category.BadRequestException;
import com.ssafy.triptogether.global.exception.response.ErrorCode;
import com.ssafy.triptogether.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "trip_account")
public class TripAccount extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_account_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @NotNull
    @Min(0)
    @Column(name = "balance")
    private Double balance;

    @JsonIgnore
    @OneToMany(mappedBy = "tripAccount", cascade = CascadeType.ALL)
    private List<AccountHistory> accountHistories = new ArrayList<>();

    @Builder
    public TripAccount(Member member, Currency currency, Double balance) {
        setMember(member);
        this.currency = currency;
        this.balance = balance;
    }

    public void setMember(Member member) {
        this.member = member;
        member.getTripAccounts().add(this);
    }

    public void depositBalance(Double newBalance) {
        this.balance += newBalance;
    }

    public void withdrawBalance(Double newBalance) {
        if (this.balance < newBalance) {
            throw new BadRequestException("TripAccountExchange", ErrorCode. TRIP_ACCOUNT_BALANCE_BAD_REQUEST);
        }
        this.balance -= newBalance;
    }
}
