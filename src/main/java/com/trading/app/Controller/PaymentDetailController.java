package com.trading.app.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trading.app.Entity.PaymentDetail;
import com.trading.app.Entity.User;
import com.trading.app.service.PaymentDetailsService;
import com.trading.app.service.UserService;

@RestController
@RequestMapping("/api")
public class PaymentDetailController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private PaymentDetailsService paymentDetailsService;
	
	@PostMapping("/payment-details")
	public ResponseEntity<PaymentDetail> addPaymentDetails(@RequestBody PaymentDetail paymentDetailRequest,@RequestHeader("Authorization") String jwt) throws Exception
	{
		User user = userService.findUserProfileByJwt(jwt);
		PaymentDetail paymentDetail = paymentDetailsService.addPaymentDetails(paymentDetailRequest.getAccountNumber(), paymentDetailRequest.getAccountHolderName(), paymentDetailRequest.getIfsc(), paymentDetailRequest.getBankName(), user);
		
		return new ResponseEntity<>(paymentDetail,HttpStatus.CREATED);
	}
	
	
	@GetMapping("payment-details")
	public ResponseEntity<PaymentDetail> getUsersPaymentDetails(@RequestHeader("Authorization")String jwt) throws Exception
	{
		User user  = userService.findUserProfileByJwt(jwt);
		
		PaymentDetail paymentDetail = paymentDetailsService.getUserPaymentDetails(user);
		
		return new ResponseEntity<>(paymentDetail , HttpStatus.CREATED);
	}
	
}
