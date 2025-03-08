package com.trading.app.Controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trading.app.Domain.VerificationType;
import com.trading.app.Entity.ForgotPasswordToken;
import com.trading.app.Entity.User;
import com.trading.app.Entity.VerificationCode;
import com.trading.app.Responce.ApiResponce;
import com.trading.app.Responce.AuthResponce;
import com.trading.app.Utils.OtpUtils;
import com.trading.app.request.ForgotPasswordTokenRequest;
import com.trading.app.request.ResetPasswordRequest;
import com.trading.app.service.EmailService;
import com.trading.app.service.ForgotPasswordService;
import com.trading.app.service.UserService;
import com.trading.app.service.VerificationCodeService;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private VerificationCodeService verificationCodeService;
	
	@Autowired
	private ForgotPasswordService forgotPasswordService;
	
	@GetMapping("/api/users/profile")
	public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) throws Exception{
		User user = userService.findUserProfileByJwt(jwt);
		return new ResponseEntity<User>(user , HttpStatus.OK);
	}
	
	@PostMapping("/api/users/verification/{verificationType}/send-otp")
	public ResponseEntity<String> sendVerificationOtp(@RequestHeader("Authorization") String jwt , @PathVariable VerificationType verificationType) throws Exception{
		
		User user = userService.findUserProfileByJwt(jwt);
		
		VerificationCode verificationCode = verificationCodeService.getVerificationByUser(user.getId());
		if(verificationCode==null)
		{
			verificationCode=verificationCodeService.sendVerificationCode(user, verificationType);
		}
		if(verificationType.equals(verificationType.EMIAL)) {
			emailService.sendVerificationEmail(user.getEmail(), verificationCode.getOtp());
		}
		return new ResponseEntity<>(" verification Otp sent successfully " , HttpStatus.OK);
	}
	
	
	
	
	@PatchMapping("/api/users/enable-two-factor/verify-otp/{otp}")
	public ResponseEntity<User> enableTwoFactorAuthontication(@PathVariable String otp ,@RequestHeader("Authorization") String jwt) throws Exception{
		User user = userService.findUserProfileByJwt(jwt);
		 VerificationCode verificationCode = verificationCodeService.getVerificationByUser(user.getId());
		
		 String sendTo= verificationCode.getVerificationType().equals(VerificationType.EMIAL)? verificationCode.getEmail():verificationCode.getMobile();
		 boolean isVerified=verificationCode.getOtp().equals(otp);
		 
		 if(isVerified)
		 {
			 User updateUser= userService.enableTwoFactorAuthenticaton(verificationCode.getVerificationType(), sendTo, user);
			 
			 verificationCodeService.deleteVerificationCodeById(verificationCode);
			 return new ResponseEntity<>(updateUser , HttpStatus.OK);
		 }
		 
		throw new Exception("Wrong Otp");
	}
	
	
	@PostMapping("/auth/users/reset-password/send-otp")
	public ResponseEntity<AuthResponce> sendForgotPasswordOtp(@RequestBody ForgotPasswordTokenRequest req) throws Exception{
		
		User user = userService.findUserByEmail(req.getSendTo());
		String otp = OtpUtils.generateOTP();
		UUID uuid = UUID.randomUUID();
		String id= uuid.toString();
		
		
		ForgotPasswordToken token = forgotPasswordService.findByUser(user.getId());
		
		if(token==null)
		{
			token=forgotPasswordService.createToken(user, id, otp,req.getVerificationType() , req.getSendTo());
		}
		if(req.getVerificationType().equals(VerificationType.EMIAL))
		{
			emailService.sendVerificationEmail(user.getEmail(), token.getOtp());
		}
		
		AuthResponce responce = new AuthResponce();
		responce.setSession(token.getId());
		responce.setMessage("Password Reset Otp Sent Successfully");
		
		
		
		return new ResponseEntity<>(responce, HttpStatus.OK);
	}
	
	
	
	
	@PatchMapping("/auth/users/reset-password/verify-otp")
	public ResponseEntity<ApiResponce> resetPassword(@RequestParam String id,@RequestBody ResetPasswordRequest req ,@RequestHeader("Authorization") String jwt) throws Exception{
		
		 ForgotPasswordToken forgotPasswordToken=forgotPasswordService.findById(id); 
		
		boolean isVerify= forgotPasswordToken.getOtp().equals(req.getOtp());
		
		if(isVerify)
		{
			userService.updatePassword(forgotPasswordToken.getUser(), req.getPassword());
			ApiResponce res = new ApiResponce();
			res.setMessage("Password Update Successfully");
			return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
		}
		throw new Exception("wrong otp");
	}
	
	
}
