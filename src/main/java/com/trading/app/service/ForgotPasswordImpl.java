package com.trading.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trading.app.Domain.VerificationType;
import com.trading.app.Entity.ForgotPasswordToken;
import com.trading.app.Entity.User;
import com.trading.app.Repositary.ForgotPasswordRepositary;

@Service
public class ForgotPasswordImpl implements ForgotPasswordService{

	@Autowired
	private ForgotPasswordRepositary forgotPasswordRepositary;
	
	@Override
	public ForgotPasswordToken createToken(User user, String id, String otp, VerificationType verificationType,
			String sendTo) {
		ForgotPasswordToken token= new ForgotPasswordToken();
		token.setUser(user);
		token.setSnedTo(sendTo);
		token.setVerificationType(verificationType);
		token.setOtp(otp);
		token.setId(id);
		return forgotPasswordRepositary.save(token);
	}

	@Override
	public ForgotPasswordToken findById(String id) {
		Optional<ForgotPasswordToken> token = forgotPasswordRepositary.findById(id);
		return token.orElse(null);
	}

	@Override
	public ForgotPasswordToken findByUser(Long UserId) {
		
		return forgotPasswordRepositary.findByUserId(UserId);
	}

	@Override
	public void deleteToken(ForgotPasswordToken token) {
		forgotPasswordRepositary.delete(token);
		
	}

}
  