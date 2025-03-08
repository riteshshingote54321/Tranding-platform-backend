package com.trading.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trading.app.Entity.PaymentDetail;
import com.trading.app.Entity.User;
import com.trading.app.Repositary.PaymentDetailRepositary;

@Service
public class PaymentDetailsServiceImpl implements PaymentDetailsService{

	@Autowired
	private PaymentDetailRepositary paymentDetailRepositary;
	
	
	@Override
	public PaymentDetail addPaymentDetails(String accountNumber, String accountHolderName, String ifsc, String bankName,
			User user) {
  
		PaymentDetail paymentDetail = new PaymentDetail();
		
		paymentDetail.setAccountNumber(accountNumber);
		paymentDetail.setAccountHolderName(accountHolderName);
		paymentDetail.setIfsc(ifsc);
		paymentDetail.setBankName(bankName);
		paymentDetail.setUser(user);
		
		return paymentDetailRepositary.save(paymentDetail);
	}

	@Override
	public PaymentDetail getUserPaymentDetails(User user) {
		// TODO Auto-generated method stub
		return paymentDetailRepositary.findByUserId(user.getId());
	}

}
