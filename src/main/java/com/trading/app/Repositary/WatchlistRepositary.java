package com.trading.app.Repositary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.app.Entity.WatchList;

public interface WatchlistRepositary extends JpaRepository<WatchList, Long>{

	WatchList findByUserId(Long userId);
}
