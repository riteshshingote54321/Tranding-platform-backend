package com.trading.app.Repositary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.app.Entity.ForgotPasswordToken;

public interface ForgotPasswordRepositary extends JpaRepository<ForgotPasswordToken, String>{

	ForgotPasswordToken findByUserId(Long userId);
}
