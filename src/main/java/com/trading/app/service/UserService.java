package com.trading.app.service;

import com.trading.app.Domain.VerificationType;
import com.trading.app.Entity.User;

public interface UserService {
	public User findUserProfileByJwt(String jwt) throws Exception;
	public User findUserByEmail(String email) throws Exception;
	public User findUserById(Long userId) throws Exception;
	
	public User enableTwoFactorAuthenticaton(VerificationType verificationType,String sendTo, User user);
	
	User updatePassword(User user , String newPassword);
}
