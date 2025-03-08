package com.trading.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trading.app.Entity.Coin;
import com.trading.app.Entity.User;
import com.trading.app.Entity.WatchList;
import com.trading.app.Repositary.WatchlistRepositary;
@Service
public class WatchlistServiceImpl implements WatchlistService {

	@Autowired
	private WatchlistRepositary watchlistRepositary;
	
	@Override
	public WatchList findUserWatchlist(Long UserId) throws Exception {
		
		WatchList watchList =watchlistRepositary.findByUserId(UserId);
		
		if(watchList==null)
		{
			throw new Exception("watchlist not found");
		}
		return watchList;
	}

	@Override
	public WatchList createWatchlist(User user) {
 
		WatchList watchList= new WatchList();
		watchList.setUser(user);
		
		return watchlistRepositary.save(watchList);
	}

	@Override
	public WatchList findById(Long id) throws Exception {

		Optional<WatchList> watchlistOptional=watchlistRepositary.findById(id);
		if(watchlistOptional.isEmpty())
		{
			throw new Exception("Watchlist not Found ");
		}
		
		return watchlistOptional.get();
	}

	@Override
	public Coin addItemToWatchlist(Coin coin, User user) throws Exception {
		WatchList watchList = findUserWatchlist(user.getId());
		
		if(watchList.getCoins().contains(coin))
		{
			watchList.getCoins().remove(coin);
		}
		else watchList.getCoins().add(coin);
		watchlistRepositary.save(watchList);
		return coin;
	}

}
