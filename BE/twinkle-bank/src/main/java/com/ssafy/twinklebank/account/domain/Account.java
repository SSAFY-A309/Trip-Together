package com.ssafy.twinklebank.account.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.twinklebank.global.domain.BaseEntity;
import com.ssafy.twinklebank.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "account")
public class Account extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@NotBlank
	@Column(name = "uuid")
	private String uuid;

	@Column(name = "balance")
	private Double balance;

	@NotBlank
	@Column(name = "name")
	private String name;

	@NotBlank
	@Column(name = "account_num")
	private String accountNum;

	@JsonIgnore
	@OneToMany(mappedBy = "account")
	List<AccountHistory> accountHistories = new ArrayList<>();

	@Builder
	public Account(Member member, String uuid, Double balance, String name, String accountNum) {
		setMember(member);
		this.uuid = uuid;
		this.balance = balance;
		this.name = name;
		this.accountNum = accountNum;
	}

	public void setMember(Member member) {
		this.member = member;
		member.getAccounts().add(this);
	}

	public void increase(double price) {
		this.balance += price;
	}

	public void decrease(double price) {
		this.balance -= price;
	}
}
