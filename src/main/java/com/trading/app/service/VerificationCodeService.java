package com.trading.app.service;

import com.trading.app.Domain.VerificationType;
import com.trading.app.Entity.User;
import com.trading.app.Entity.VerificationCode;

public interface VerificationCodeService {
	
	VerificationCode sendVerificationCode(User user , VerificationType verificationType);
	
	VerificationCode getVerificationCodeById(Long id) throws Exception;
	
	VerificationCode getVerificationByUser(Long userId);
	
	
	
	void deleteVerificationCodeById(VerificationCode verificationCode);
}
