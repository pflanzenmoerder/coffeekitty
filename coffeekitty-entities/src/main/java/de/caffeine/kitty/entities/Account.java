package de.caffeine.kitty.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "PF_ACCOUNT")
public class Account extends BaseEntity {
	private static final long serialVersionUID = 3690342725908956988L;

	@NotNull
	@Column(name = "BALANCE", nullable = false)
	private Float balance = 0f;

	@NotNull
	@Column(name = "ADMIN", nullable = false)
	private Boolean admin = Boolean.FALSE;

	@NotNull
	@Enumerated
	@Column(name = "APPROVED", nullable = false)
	private AccountStatusEnum accountStatusEnum  = AccountStatusEnum.REQUESTED;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private Kitty kitty;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setBalance(Float balance) {
		this.balance = balance;
	}

	public Float getBalance() {
		return balance;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public Kitty getKitty() {
		return kitty;
	}

	public void setKitty(Kitty kitty) {
		this.kitty = kitty;
	}

	public AccountStatusEnum getAccountStatusEnum() {
		return accountStatusEnum;
	}

	public void setAccountStatusEnum(AccountStatusEnum accountStatusEnum) {
		this.accountStatusEnum = accountStatusEnum;
	}

}
