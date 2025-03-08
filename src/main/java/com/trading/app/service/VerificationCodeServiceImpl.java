package com.trading.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trading.app.Domain.VerificationType;
import com.trading.app.Entity.User;
import com.trading.app.Entity.VerificationCode;
import com.trading.app.Repositary.VerifictionCodeRepositary;
import com.trading.app.Utils.OtpUtils;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService{
	
	@Autowired
	private VerifictionCodeRepositary verifictionCodeRepositary;
	
	@Override
	public VerificationCode sendVerificationCode(User user , VerificationType verificationType) {
		VerificationCode verificationCode1=new VerificationCode();
		 verificationCode1.setOtp(OtpUtils.generateOTP());
		 verificationCode1.setVerificationType(verificationType);
		 verificationCode1.setUser(user);
		 
		 return verifictionCodeRepositary.save(verificationCode1);
	}

	@Override
	public VerificationCode getVerificationCodeById(Long id) throws Exception {
		Optional<VerificationCode> vericationCode= verifictionCodeRepositary.findById(id);
		if(vericationCode.isPresent())
		{
			return vericationCode.get();
		}
		throw new Exception("Verification Code not Found");
	}

	@Override
	public VerificationCode getVerificationByUser(Long userId) {
		
		return verifictionCodeRepositary.findByUserId(userId);
	}

	@Override
	public void deleteVerificationCodeById(VerificationCode verificationCode) {
		verifictionCodeRepositary.delete(verificationCode);
		
	}

}
