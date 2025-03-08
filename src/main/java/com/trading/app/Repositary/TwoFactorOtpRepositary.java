package com.trading.app.Repositary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.app.Entity.TwoFactorOtp;

public interface TwoFactorOtpRepositary extends JpaRepository<TwoFactorOtp, String> {
	TwoFactorOtp findByUserId(Long userId);
}
