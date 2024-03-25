package com.ssafy.twinklebank.account.domain;

import com.ssafy.twinklebank.global.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "account_history")
public class AccountHistory extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_history_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id")
	private Account account;

	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private Type type;

	@NotBlank
	@Column(name = "business_name")
	private String businessName;

	@NotBlank
	@Column(name = "address")
	private String address;

	@Column(name = "price")
	private Double price;

	@Builder
	public AccountHistory(Account account, Type type, String businessName, String address, Double price) {
		setAccount(account);
		this.type = type;
		this.businessName = businessName;
		this.address = address;
		this.price = price;
	}

	public void setAccount(Account account) {
		this.account = account;
		account.getAccountHistories().add(this);
	}
}
