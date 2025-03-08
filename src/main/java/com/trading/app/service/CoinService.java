package com.trading.app.service;

import java.util.List;

import com.trading.app.Entity.Coin;

public interface CoinService {
	 List<Coin> getCoinsList(int page) throws Exception;
	 
	 String getMarketChart(String coinId , int days) throws Exception;
	 
	 //its for coingecko api
	 String getCoinDetails(String coinId) throws Exception;
	 
	 //present in database
	 Coin findById(String coniId) throws Exception;
	 
	 String searchCoin(String keyword) throws Exception;
	 
	 String getTop50CoinsByMarketCapRank() throws Exception;
	 
	 String getTreadingCoins() throws Exception;
	 
	 

}
