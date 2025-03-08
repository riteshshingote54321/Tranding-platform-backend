package com.trading.app.service;

import com.trading.app.Domain.VerificationType;
import com.trading.app.Entity.ForgotPasswordToken;
import com.trading.app.Entity.User;

public interface ForgotPasswordService {
	
	 ForgotPasswordToken createToken(User user ,String id , String otp, VerificationType verificationType, String sendTo);
	 
	 ForgotPasswordToken findById(String id);
	
	 ForgotPasswordToken findByUser(Long UserId);
	 
	 void deleteToken(ForgotPasswordToken token);
}
