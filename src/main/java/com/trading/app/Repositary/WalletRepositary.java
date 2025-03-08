package com.trading.app.Repositary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.app.Wallet;

public interface WalletRepositary extends JpaRepository<Wallet, Long>{

	Wallet findByUserId(Long userId);
}
