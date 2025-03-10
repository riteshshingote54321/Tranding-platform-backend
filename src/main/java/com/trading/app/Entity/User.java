package com.trading.app.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.trading.app.Domain.UserRoll;

import jakarta.annotation.Generated;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String fullName;
	
	private String email;
	
	@JsonProperty(access =Access.WRITE_ONLY )
	private String password;
	
	@Embedded
	 private TwoFactorAuth twoFactorAuth= new TwoFactorAuth();
	
	private UserRoll role= UserRoll.ROLE_COSTOMER;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public TwoFactorAuth getTwoFactorAuth() {
		return twoFactorAuth;
	}

	public void setTwoFactorAuth(TwoFactorAuth twoFactorAuth) {
		this.twoFactorAuth = twoFactorAuth;
	}

	public UserRoll getRole() {
		return role;
	}

	public void setRole(UserRoll role) {
		this.role = role;
	}
	
	
	

}
