package de.caffeine.kitty.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "PF_KITTY")
public class Kitty extends BaseEntity {
	private static final long serialVersionUID = -4575421572896404181L;
	
	@NotNull
	@Column(name = "NAME", nullable = false, unique = true)
	private String name;
	@NotNull
	@Column(name = "PRICE", nullable = false)
	private Float pricePerMugInEuro = 0.30f;
	@OneToMany(mappedBy = "kitty", cascade = CascadeType.ALL)
	private List<Account> accounts = new ArrayList<Account>();
	@ManyToOne(optional = false)
	private User user;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getPricePerMugInEuro() {
		return pricePerMugInEuro;
	}

	public void setPricePerMugInEuro(Float pricePerMugInEuro) {
		this.pricePerMugInEuro = pricePerMugInEuro;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public void addAccount(Account account) {
		getAccounts().add(account);
	}

	public void removeAccount(Account account) {
		getAccounts().remove(account);
	}
	
}
