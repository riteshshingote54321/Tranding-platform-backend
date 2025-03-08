package com.trading.app.service;

import java.util.List;

import com.trading.app.Entity.User;
import com.trading.app.Entity.Withdrawal;

public interface WithdrawalService {

	Withdrawal requestWithdrawal(Long amount , User user);
	
	Withdrawal procedWithdrawal(Long withdrawalId , boolean accept) throws Exception;
	
	List<Withdrawal> getUsersWithdrawalHistory(User user);
	
	List<Withdrawal> getAllWithdrawalRequest();
	
}
