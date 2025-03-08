 package com.trading.app.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trading.app.Wallet;
import com.trading.app.Domain.OrderType;
import com.trading.app.Entity.Order;
import com.trading.app.Entity.User;
import com.trading.app.Repositary.WalletRepositary;

@Service
public class WalletServiceImpl implements WalletService {

	@Autowired
	private WalletRepositary walletRepositary;
	
	@Override
	public Wallet getUserWallet(User user) {

		Wallet wallet = walletRepositary.findByUserId(user.getId());
		if(wallet== null)
		{
			wallet = new Wallet();
			wallet.setUser(user);
		}
		return wallet;
		
		
	}

	@Override
	public Wallet addBalance(Wallet wallet, Long money) {
		
		BigDecimal balance = wallet.getBalance();
		BigDecimal newBalance =balance.add(BigDecimal.valueOf(money));
		
		wallet.setBalance(newBalance);
		
		
		return walletRepositary.save(wallet);
	}

	@Override
	public Wallet findWalletById(Long id) throws Exception {
		Optional<Wallet> wallet=walletRepositary.findById(id);
		if(wallet.isPresent())
		{
			return wallet.get();
		}
		throw new Exception("Wallet Not Found");
	}

	@Override
	public Wallet walletToWalletTransfer(User sender, Wallet receiverWallter, Long amount) throws Exception {
		Wallet senderWallet=getUserWallet(sender);
		
		if(senderWallet.getBalance().compareTo(BigDecimal.valueOf(amount))<0)
		{
			throw new Exception("Insufficient balance");
		}
		BigDecimal senderbalance= senderWallet.getBalance().subtract(BigDecimal.valueOf(amount));
		senderWallet.setBalance(senderbalance);
		walletRepositary.save(senderWallet);
		
		BigDecimal receiverBalance=receiverWallter.getBalance().add(BigDecimal.valueOf(amount));
		
		receiverWallter.setBalance(receiverBalance);
		walletRepositary.save(receiverWallter);
		return senderWallet;
	}

	@Override
	public Wallet payOrderPayment(Order order, User user) throws Exception {
		Wallet wallet = getUserWallet(user);
		
		if(order.getOrderType().equals(OrderType.BUY)) {
			BigDecimal newBalance = wallet.getBalance().subtract(order.getPrice());
			if(newBalance.compareTo(order.getPrice())<0)
			{
				throw new Exception("insufficient funds for this transaction");
			}
			wallet.setBalance(newBalance);
		} 
		else {
			BigDecimal newBalance=wallet.getBalance().add(order.getPrice());
			wallet.setBalance(newBalance);
			
			
		}
			
		walletRepositary.save(wallet);
		return wallet;
	}

}
