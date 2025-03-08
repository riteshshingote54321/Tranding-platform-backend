package com.trading.app.Repositary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.app.Entity.Coin;

public interface CoinRepositary extends JpaRepository<Coin, String>{

}
