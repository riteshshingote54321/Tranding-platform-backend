 package com.trading.app.service;

import com.trading.app.Wallet;
import com.trading.app.Entity.Order;
import com.trading.app.Entity.User;

public interface WalletService {

	Wallet getUserWallet(User user);
	Wallet addBalance(Wallet wallet , Long money);
	Wallet findWalletById(Long id) throws Exception;
	Wallet walletToWalletTransfer(User sender , Wallet receiverWallter , Long amount) throws Exception;
	Wallet payOrderPayment(Order order, User user) throws Exception;
}
