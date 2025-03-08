package com.trading.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trading.app.Entity.Asset;
import com.trading.app.Entity.Coin;
import com.trading.app.Entity.User;
import com.trading.app.Repositary.AssetRepositary;

@Service 
public class AssetServiceImpl implements AssetService {

	@Autowired
	private AssetRepositary assetRepositary;
	
	@Override
	public Asset createAsset(User user, Coin coin, double quantity) {
		Asset asset = new Asset();
		
		asset.setUser(user);
		asset.setCoin(coin);
		asset.setQuantity(quantity);
		asset.setBuyPrice(coin.getCurrentPrice());
		
		
		return assetRepositary.save(asset);
	}

	@Override
	public Asset getAssetById(Long assetId) throws Exception {
		return assetRepositary.findById(assetId).orElseThrow(()-> new Exception("Asset Not Found"));
	}

	@Override
	public Asset getAssetByUserIdAndId(Long userId, Long assetId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Asset> getUsersAsset(Long userId) {
		return assetRepositary.findByUserId(userId);
	}

	@Override
	public Asset updateAsset(Long assetId, double quantity) throws Exception {

		 Asset oldAsset= getAssetById(assetId);
		oldAsset.setQuantity(quantity + oldAsset.getQuantity());
		
		 
		return assetRepositary.save(oldAsset);
	}

	@Override
	public Asset findAssetByUserIdAndCoinId(Long userId, String coinId) {

		
		
		return assetRepositary.findByUserIdAndCoinId(userId, coinId);
	}

	@Override
	public void deleteAsset(Long assetId) {

		assetRepositary.deleteById(assetId);
	}

}
