package com.trading.app.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.Mode;
import com.stripe.param.checkout.SessionCreateParams.PaymentMethodType;
import com.trading.app.Domain.PaymentMethod;
import com.trading.app.Domain.PaymentOrderStatus;
import com.trading.app.Entity.PaymentOrder;
import com.trading.app.Entity.User;
import com.trading.app.Repositary.PaymentOrderRepositary;
import com.trading.app.Responce.PaymentResponce;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private PaymentOrderRepositary paymentOrderRepositary;
	
	
	//Api Key
	@Value("${stipe.api.key}")
	private String stripeSecreateKey;
	
	@Value("${razorpay.api.key}")
	private String apiKey;
	
	@Value("${razorpay.api.secret}")
	private String apiSecreateKey;
	
	@Override
	public PaymentOrder createOrder(User user, Long amount, PaymentMethod paymentMethod) {

		PaymentOrder paymentOrder = new PaymentOrder();
		
		paymentOrder.setUser(user);
		paymentOrder.setAmount(amount);
		paymentOrder.setPaymentMethod(paymentMethod);
		
		
		
		return paymentOrderRepositary.save(paymentOrder);
	}

	@Override
	public PaymentOrder getPaymentOrderById(Long id) throws Exception {

		
		
		return paymentOrderRepositary.findById(id).orElseThrow(()-> new Exception("Payment Order Not found"));
	}

	@Override
	public Boolean ProccedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws Exception {

		if(paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)) {
			if(paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)) {
				RazorpayClient razorpay= new RazorpayClient(apiKey, apiSecreateKey);
				
				Payment payment =razorpay.payments.fetch(paymentId);
				
				Integer amount = payment.get("amount");
				String status = payment.get("status");
				
				if(status.equals("captured")) {
					paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
					return true;
				}
				paymentOrder.setStatus(PaymentOrderStatus.FAILED);
				paymentOrderRepositary.save(paymentOrder);
				return false;
			}
			paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
			paymentOrderRepositary.save(paymentOrder);
			return true;
		}
		
		return false;
	}

	@Override
	public PaymentResponce createRazorpayPaymentLink(User user, Long amount) throws Exception {

		 Long Amount = amount*100;
		
		 try {
			 //Instantiate A Razorpay Client with Your key id and Screte
			 
			 RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecreateKey);
			 
			 //create A json object with the payment kink request parameter
			 
			 JSONObject paymentLinkRequest =new JSONObject();
			 paymentLinkRequest.put("amount", amount);
			 paymentLinkRequest.put("currency", "INR");
			 
			 
			 //create json object with the Customer details
			 JSONObject customer = new JSONObject();
			 customer.put("name", user.getFullName());
			 
			 customer.put("email", user.getEmail());
			 paymentLinkRequest.put("customer", customer);
			 
			 
			 //create a json object with the notification setting
			 JSONObject notify = new JSONObject();
			 notify.put("email", true);
			 paymentLinkRequest.put("notify", notify);
			 
			 //set the reminder setting
			 paymentLinkRequest.put("reminder_enable", true);
			 
			 //set the callback URL And method
			 paymentLinkRequest.put("callback_url","http://localhost:8080/wallet");
			 paymentLinkRequest.put("callback_method", "get");
			 
			 
			 //create payment link using method
			 PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);
			 
			 String paymentLinkId = payment.get("id");
			 String paymentLinkUrl = payment.get("short_url");
			 
			 PaymentResponce res = new PaymentResponce();
			 res.setPayment_url(paymentLinkUrl);
			 
			 return res;
		 }catch (RazorpayException e) {
			 System.out.println("Error Creating payment Link:" + e.getMessage());
			 throw new RazorpayException(e.getMessage());
		}
		 
		 
		
	}

	@Override
	public PaymentResponce createStripePaymentLink(User user,Long amount, Long orderId) throws Exception {
		Stripe.apiKey= stripeSecreateKey;
		
		SessionCreateParams params = SessionCreateParams.builder()
				.addPaymentMethodType(PaymentMethodType.CARD)
				.setMode(Mode.PAYMENT)
				.setSuccessUrl("http://localhost:8080/wallet?order_id="+orderId)
				.setCancelUrl("http://localhost:8080/payment/cancel")
				.addLineItem(SessionCreateParams.LineItem.builder()
						.setQuantity(1L)
						.setPriceData(
								SessionCreateParams.LineItem.PriceData.builder()
								.setCurrency("usd")
								.setUnitAmount(amount*100)
								.setProductData(SessionCreateParams
										.LineItem
										.PriceData
										.ProductData
										.builder().setName("Top up wallet")
										.build()
										).build()
								).build()
						).build();
				
		Session session = Session.create(params);
		
		System.out.println("Session---"+session);
		
		PaymentResponce res = new PaymentResponce();
		res.setPayment_url(session.getUrl());
		return res;
	}

}
