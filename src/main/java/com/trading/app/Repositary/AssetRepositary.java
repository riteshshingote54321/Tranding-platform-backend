package com.trading.app.Repositary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.app.Entity.Asset;

public interface AssetRepositary extends JpaRepository<Asset, Long>{

	List<Asset> findByUserId(Long userId);
	
	Asset findByUserIdAndCoinId(Long userId , String coinId);
}
