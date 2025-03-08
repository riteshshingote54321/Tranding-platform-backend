package com.trading.app.Repositary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.app.Entity.VerificationCode;

public interface VerifictionCodeRepositary extends JpaRepository<VerificationCode, Long>{

	
	public VerificationCode findByUserId(Long userId);
}
