package com.trading.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.trading.app.Domain.VerificationType;
import com.trading.app.Entity.TwoFactorAuth;
import com.trading.app.Entity.User;
import com.trading.app.Repositary.UserRepo;
import com.trading.app.config.JwtProvider;


@RestController
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepo userRepo;
	
	
	
	@Override
	public User findUserProfileByJwt(String jwt) throws Exception  {
		String email= JwtProvider.getEmailFromToken(jwt);
		User user= userRepo.findByEmail(email);
		
		if(user == null)
		{
			throw new Exception("User Not Found");
		}
		
		return user;
	}

	@Override
	public User findUserByEmail(String email) throws Exception {
         User user= userRepo.findByEmail(email);
		
		if(user == null)
		{
			throw new Exception("User Not Found");
		}
		
		return user;
	}

	@Override
	public User findUserById(Long userId) throws Exception {
		 Optional<User> user= userRepo.findById(userId);
		 if(user.isEmpty())
		 {
			 throw new Exception("User Not Found");
		 }
		 
		return user.get();
	}

	@Override
	public User enableTwoFactorAuthenticaton(VerificationType verificationType, String sendTo, User user) {
		TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
		twoFactorAuth.setEnabled(true);
		twoFactorAuth.setSendTo(verificationType);
		
		user.setTwoFactorAuth(twoFactorAuth);
		
		return userRepo.save(user);
	}
	
	

	@Override
	public User updatePassword(User user, String newPassword) {
		user.setPassword(newPassword);

		return userRepo.save(user);
	}

	

}
