package com.trading.app.service;

import com.trading.app.Entity.Coin;
import com.trading.app.Entity.User;
import com.trading.app.Entity.WatchList;

public interface WatchlistService {

	WatchList findUserWatchlist(Long UserId) throws Exception;
	
	WatchList createWatchlist(User user);
	
	WatchList findById(Long id) throws Exception;
	
	Coin addItemToWatchlist(Coin coin ,User user) throws Exception;
	

}
