package com.trading.app.service;

import com.trading.app.Entity.TwoFactorOtp;
import com.trading.app.Entity.User;

public interface TwoFactorOtpService {
	
	TwoFactorOtp createTwoFactorOtp(User user , String otp, String jwt);
	
	TwoFactorOtp findByUser(Long userId);
	
	TwoFactorOtp findById(String id);
	
	boolean verifyTwoFactorOtp(TwoFactorOtp twoFactorOtp, String otp);
	
	void deleteTwoFactorOtp(TwoFactorOtp twoFactorOtp);
}
