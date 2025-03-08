package com.trading.app.service;

import java.util.List;

import com.trading.app.Entity.Asset;
import com.trading.app.Entity.Coin;
import com.trading.app.Entity.User;

public interface AssetService {

	Asset createAsset(User user , Coin coin , double quantity);
	
	Asset getAssetById(Long assetId) throws Exception;
	
	Asset getAssetByUserIdAndId(Long userId, Long assetId );
	
	List<Asset> getUsersAsset(Long userId);
	
	Asset updateAsset(Long assetId , double quantity) throws Exception;
	
	Asset findAssetByUserIdAndCoinId(Long userId , String coinId);
	
	void deleteAsset(Long assetId);
	
}
