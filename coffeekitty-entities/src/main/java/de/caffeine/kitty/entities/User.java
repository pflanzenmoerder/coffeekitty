package de.caffeine.kitty.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;

@Entity
@Table(name = "PF_USER")
public class User extends BaseEntity{
	private static final long serialVersionUID = -7863274989564105229L;

	@NotNull
	@Column(name = "DISPLAYNAME", unique = true, nullable = false)
    private String displayName;
	@NotNull
	@Column(name ="FULLNAME")
	private String fullName;
	@Email
	@Column(name ="EMAIL", unique = true, nullable = false)
	private String email;
	@NotNull
	@Column(name ="SHARESTATS")
	private Boolean shareStatistics = Boolean.FALSE;
	@NotNull
    @Column(name = "PASSWORD", nullable = false)
    private String password;
	@NotNull
    @Column(name = "SALT", nullable = false)
    private String salt;
    @Column(name ="WARNLEVEL", nullable = true)
    private Integer warnLevel; 
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private Set<Account> accounts = new HashSet<Account>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private Set<Kitty> kitties = new HashSet<Kitty>();
    @OneToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name="DEFACCOUNT", unique= true, nullable=true, insertable=true, updatable=true)
    private Account defaultAccount;
    @NotNull
	@Column(name ="APPADMIN")
	private Boolean applicationAdmin = Boolean.FALSE;
    
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Account getDefaultAccount() {
		return defaultAccount;
	}

	public void setDefaultAccount(Account defaultAccount) {
		this.defaultAccount = defaultAccount;
	}

	public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getFullName() {
    	return fullName;
    }
    
    public void setFullName(String fullName) {
    	this.fullName = fullName;
    }
    
    public String getEmail() {
    	return email;
    }
    
    public void setEmail(String email) {
    	this.email = email;
    }
    
    public Boolean getShareStatistics() {
    	return shareStatistics;
    }
    
    public void setShareStatistics(Boolean shareStatistics) {
    	this.shareStatistics = shareStatistics;
    }
    
    public Integer getWarnLevel() {
    	return warnLevel;
    }
    
    public void setWarnLevel(Integer warnLevel) {
    	this.warnLevel = warnLevel;
    }

	public Set<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<Account> accounts) {
		this.accounts = accounts;
	}

	public Boolean getApplicationAdmin() {
		return applicationAdmin;
	}

	public void setApplicationAdmin(Boolean applicationAdmin) {
		this.applicationAdmin = applicationAdmin;
	}

	public Set<Kitty> getKitties() {
		return kitties;
	}

	public void setKitties(Set<Kitty> kitties) {
		this.kitties = kitties;
	}

	public void addAccount(Account account) {
		getAccounts().add(account);
	}
	
	public void removeAccount(Account account) {
		getAccounts().remove(account);
	}

	public void addKitty(Kitty kitty) {
		getKitties().add(kitty);
	}
	
	public void removeAccount(Kitty kitty) {
		getKitties().remove(kitty);
	}
}
