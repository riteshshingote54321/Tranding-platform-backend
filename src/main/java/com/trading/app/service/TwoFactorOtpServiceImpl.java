 package com.trading.app.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trading.app.Entity.TwoFactorOtp;
import com.trading.app.Entity.User;
import com.trading.app.Repositary.TwoFactorOtpRepositary;


@Service
public class TwoFactorOtpServiceImpl implements TwoFactorOtpService{
	
	@Autowired
	private TwoFactorOtpRepositary twoFactorOtpRepositary;
	
	
	@Override
	public TwoFactorOtp createTwoFactorOtp(User user, String otp, String jwt) {
		UUID uuid = UUID.randomUUID();
		
		String id= uuid.toString();
		
		TwoFactorOtp twoFactorOtp = new TwoFactorOtp();
		twoFactorOtp.setOtp(otp);
		twoFactorOtp.setJwt(jwt);
		twoFactorOtp.setId(id);
		twoFactorOtp.setUser(user);
		
		return twoFactorOtpRepositary.save(twoFactorOtp);
		
	}

	@Override
	public TwoFactorOtp findByUser(Long userId) {
		// TODO Auto-generated method stub
		return twoFactorOtpRepositary.findByUserId(userId);
	}

	@Override
	public TwoFactorOtp findById(String id) {
		Optional<TwoFactorOtp> otp = twoFactorOtpRepositary.findById(id);
		return otp.orElse(null);
	}

	@Override
	public boolean verifyTwoFactorOtp(TwoFactorOtp twoFactorOtp, String otp) {
		// TODO Auto-generated method stub
		return twoFactorOtp.getOtp().equals(otp);
	}

	@Override
	public void deleteTwoFactorOtp(TwoFactorOtp twoFactorOtp) {
		twoFactorOtpRepositary.delete(twoFactorOtp);
		
	}

}
