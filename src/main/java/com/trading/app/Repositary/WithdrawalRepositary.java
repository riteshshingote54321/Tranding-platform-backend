package com.trading.app.Repositary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.app.Entity.Withdrawal;

public interface WithdrawalRepositary extends JpaRepository<Withdrawal, Long>{

	List<Withdrawal> findByUserId(Long userId);
}
