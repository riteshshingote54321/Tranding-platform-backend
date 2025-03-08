package com.trading.app.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trading.app.Wallet;
import com.trading.app.Domain.WalletTransactionType;
import com.trading.app.Entity.User;
import com.trading.app.Entity.WalletTransaction;
import com.trading.app.Entity.Withdrawal;
import com.trading.app.service.UserService;
import com.trading.app.service.WalletService;
import com.trading.app.service.WithdrawalService;

@RestController
public class WithdrawalController {

	@Autowired
	private WithdrawalService withdrawalService;
	
	@Autowired
	private WalletService walletService;
	
	@Autowired
	private  UserService userService;
	
//	@Autowired
//	private WalletTransactionService walletTransacationService;
	
	@PostMapping("/api/withdrawal/{amount}")
	public ResponseEntity<?> withdrawalRequest(@PathVariable Long amount , @RequestHeader("Authorization") String jwt) throws Exception
	{
		User user = userService.findUserProfileByJwt(jwt);
		
		Wallet userWallet = walletService.getUserWallet(user);
		
		Withdrawal withdrawal = withdrawalService.requestWithdrawal(amount, user);
		walletService.addBalance(userWallet, -withdrawal.getAmount());
		
		//WalletTransaction walletTransaction = walletTransactionService.createTransacton(userWallet , WalletTransactionType.WITHDRAWAL , null ,"bank Account Withdrawal" , withdrawal.getAmount();
		
		return new ResponseEntity<>(withdrawal , HttpStatus.OK);
		
	}
	
	@PatchMapping("/api/admin/withdrawal/{id}/proceed/{accept}")
	public ResponseEntity<?> proceedWithdrawal(@PathVariable Long id , @PathVariable boolean accept , @RequestHeader("Authorization") String jwt) throws Exception
	{
		
		User user = userService.findUserProfileByJwt(jwt);
		
		Withdrawal withdrawal = withdrawalService.procedWithdrawal(id , accept);
		
		Wallet userWallet = walletService.getUserWallet(user);
		
		if(!accept)
		{
			walletService.addBalance(userWallet, withdrawal.getAmount());
		}
		
		return new ResponseEntity<>(withdrawal ,HttpStatus.OK);
	}
	
	@GetMapping("/api/withdrawal")
	public ResponseEntity<List<Withdrawal>> getwithdrawalHistory(@RequestHeader("Authorization") String jwt) throws Exception
	{
		User user = userService.findUserProfileByJwt(jwt);
		
		List<Withdrawal> withdrawals = withdrawalService.getUsersWithdrawalHistory(user);
		return new ResponseEntity<>(withdrawals , HttpStatus.OK);
	}
	
	@GetMapping("/api/admin/withdrawal")
	public ResponseEntity<List<Withdrawal>> getAllWithdrawalRequest(@RequestHeader("Authorization") String jwt) throws Exception
	{
		User user = userService.findUserProfileByJwt(jwt);
		
		List<Withdrawal> withdrawals =withdrawalService.getAllWithdrawalRequest();
		
		return new ResponseEntity<>(withdrawals , HttpStatus.OK);
		
	}
	
}
