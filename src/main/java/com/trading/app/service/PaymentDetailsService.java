package com.trading.app.service;

import com.trading.app.Entity.PaymentDetail;
import com.trading.app.Entity.User;

public interface PaymentDetailsService {

	public PaymentDetail addPaymentDetails(String accountNumber ,
											String accountHolderName,
											String ifsc,
											String bankName,
											User user);
	
	public PaymentDetail getUserPaymentDetails(User user);
	
	
}
