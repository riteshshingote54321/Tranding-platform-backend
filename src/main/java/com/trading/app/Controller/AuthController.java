package com.trading.app.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trading.app.Entity.TwoFactorOtp;
import com.trading.app.Entity.User;
import com.trading.app.Repositary.UserRepo;
import com.trading.app.Responce.AuthResponce;
import com.trading.app.Utils.OtpUtils;
import com.trading.app.config.JwtProvider;
import com.trading.app.service.CostomService;
import com.trading.app.service.EmailService;
import com.trading.app.service.TwoFactorOtpService;
import com.trading.app.service.WatchlistService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private TwoFactorOtpService factorOtpService;
	
	@Autowired
	private CostomService costomService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private WatchlistService watchlistService;
	
	@PostMapping("/signup")
	public ResponseEntity<AuthResponce> register(@RequestBody User user) throws Exception
	{
		
		
		User isEmailExist = userRepo.findByEmail(user.getEmail());
		 if(isEmailExist!=null)
		 {
			 throw new Exception("Email is Already used with anothe account");
		 }
		
		 User newUser = new User();
			newUser.setEmail(user.getEmail());
			newUser.setPassword(user.getPassword());
			newUser.setEmail(user.getEmail());
			newUser.setFullName(user.getFullName());
		 
		 
		 
		User savedUser = userRepo.save(newUser);
		
		watchlistService.createWatchlist(savedUser);
		
		
		Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		
		 String jwt= JwtProvider.generateToken(auth);
		 
		
		 
		 
		 
		
		 
		 
		 
		AuthResponce res= new AuthResponce();
		res.setJwt(jwt);
		res.setStatus(true);
		res.setMessage("Register success");
		
		
		return new ResponseEntity<>(res , HttpStatus.CREATED);
	}
	
	
	
	
	@PostMapping("/signin")
	public ResponseEntity<AuthResponce> login(@RequestBody User user) throws Exception
	{
		
		
		String userName= user.getEmail();
		String password= user.getPassword();
		 
		
		
		    
		Authentication auth =authenticate(userName, password);
				
				
				
				
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		
		 String jwt= JwtProvider.generateToken(auth);
		 
		 User authuser = userRepo.findByEmail(userName);
		 
		 
		 if(user.getTwoFactorAuth().isEnabled())
		 {
			 AuthResponce res = new AuthResponce();
			 res.setMessage("Two Factor auth is enbled");
			 res.setTwoFactorAuthEnabled(true);
			 String otp= OtpUtils.generateOTP();
			 
			 TwoFactorOtp oldTwoFactorOtp=factorOtpService.findByUser(authuser.getId());
			 if(oldTwoFactorOtp!=null)
			 {
				 factorOtpService.deleteTwoFactorOtp(oldTwoFactorOtp);
			 }
			 
			 TwoFactorOtp newFactorOtp= factorOtpService.createTwoFactorOtp(authuser, otp, jwt);
			 
			 
			 
			 emailService.sendVerificationEmail(userName, otp);
			 
			 
			 res.setSession(newFactorOtp.getId());
			 
			 return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
			 
		 }
		 
		 
		 
		AuthResponce res= new AuthResponce();
		res.setJwt(jwt);
		res.setStatus(true);
		res.setMessage("Login success");
		
		
		return new ResponseEntity<>(res , HttpStatus.CREATED);
	}




	private Authentication authenticate(String userName, String password) {
		UserDetails userDetails = costomService.loadUserByUsername(userName);
		if(userDetails == null)
		{
			throw new BadCredentialsException("Invalid username");
		}
		if(!password.equals(userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid password");
		}
		return new UsernamePasswordAuthenticationToken(userDetails, password , userDetails.getAuthorities());
	}

	
	@PostMapping("/two-factor/otp/{otp}")
	public ResponseEntity<AuthResponce> verifySigninOtp(@PathVariable String otp , @RequestParam String id )throws Exception
	{
		
		TwoFactorOtp twoFactorOtp = factorOtpService.findById(id);
		
		if(factorOtpService.verifyTwoFactorOtp(twoFactorOtp , otp))
		{
			AuthResponce res= new AuthResponce();
			res.setMessage("Two Factor Authentication verified");
			res.setTwoFactorAuthEnabled(true);
			res.setJwt(twoFactorOtp.getJwt());
			return new ResponseEntity<>(res, HttpStatus.OK);
		}
		
		throw new Exception("invalid Otp");
	}
}
