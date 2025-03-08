package com.trading.app.service;

import com.trading.app.Domain.PaymentMethod;
import com.trading.app.Entity.PaymentOrder;
import com.trading.app.Entity.User;
import com.trading.app.Responce.PaymentResponce;

public interface PaymentService {

	PaymentOrder createOrder(User user , Long amount , PaymentMethod paymentMethod);
	
	PaymentOrder getPaymentOrderById(Long id) throws Exception;
	
	Boolean ProccedPaymentOrder(PaymentOrder paymentOrder , String paymentId) throws Exception;
	
	//who in india
	PaymentResponce createRazorpayPaymentLink(User user , Long amount) throws Exception;
	// who is not for india
	PaymentResponce createStripePaymentLink(User user, Long amount , Long orderId) throws Exception;

	
}
