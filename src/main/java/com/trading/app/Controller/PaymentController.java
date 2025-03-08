package com.trading.app.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import com.trading.app.Domain.PaymentMethod;
import com.trading.app.Entity.PaymentOrder;
import com.trading.app.Entity.User;
import com.trading.app.Responce.PaymentResponce;
import com.trading.app.service.PaymentService;
import com.trading.app.service.UserService;

@RestController
public class PaymentController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PaymentService paymentService;
	
	@PostMapping("/api/payment/{paymentMethod}/amount/{amount}")
	public ResponseEntity<PaymentResponce> paymentHandler(
			@PathVariable PaymentMethod paymentMethod ,
			@PathVariable Long amount,
			@RequestHeader("Authorization")String jwt) throws Exception, RazorpayException ,StripeException{
		User user = userService.findUserProfileByJwt(jwt);
		
		PaymentResponce paymentResponce;
		PaymentOrder order = paymentService.createOrder(user, amount, paymentMethod);
		
		if(paymentMethod.equals(paymentMethod.RAZORPAY)) {
			paymentResponce = paymentService.createRazorpayPaymentLink(user, amount);
			
		}else {
			paymentResponce = paymentService.createStripePaymentLink(user, amount, order.getId());
		}
		
		return new ResponseEntity<>(paymentResponce , HttpStatus.CREATED);
		
	}

}
