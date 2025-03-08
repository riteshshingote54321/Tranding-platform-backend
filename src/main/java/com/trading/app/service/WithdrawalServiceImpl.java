package com.trading.app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trading.app.Domain.WithdrawalStatus;
import com.trading.app.Entity.User;
import com.trading.app.Entity.Withdrawal;
import com.trading.app.Repositary.WithdrawalRepositary;

@Service
public class WithdrawalServiceImpl implements WithdrawalService {

	@Autowired
	private WithdrawalRepositary withdrawalRepositary;
	
	
	@Override
	public Withdrawal requestWithdrawal(Long amount, User user) {

		Withdrawal withdrawal = new Withdrawal();
		
		withdrawal.setAmount(amount);
		withdrawal.setUser(user);
		withdrawal.setStatus(WithdrawalStatus.PENDING);
		
		return withdrawalRepositary.save(withdrawal);
	}

	@Override
	public Withdrawal procedWithdrawal(Long withdrawalId, boolean accept) throws Exception {
		
		Optional<Withdrawal> withdrawal= withdrawalRepositary.findById(withdrawalId);
		if(withdrawal.isEmpty())
		{
			throw new Exception("Withdrawal Not Found ");
			
		}
		Withdrawal withdrawal1=withdrawal.get();
		
		withdrawal1.setDate(LocalDateTime.now());
		
		if(accept) {
			withdrawal1.setStatus(WithdrawalStatus.SUCCESS);
		}else {
			withdrawal1.setStatus(WithdrawalStatus.PENDING);
		}
		
		
		return withdrawalRepositary.save(withdrawal1);
	}

	@Override
	public List<Withdrawal> getUsersWithdrawalHistory(User user) {
		return withdrawalRepositary.findByUserId(user.getId());
	}

	@Override
	public List<Withdrawal> getAllWithdrawalRequest() {
		return withdrawalRepositary.findAll();
	}

	
}