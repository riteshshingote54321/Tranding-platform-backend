package com.trading.app.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trading.app.Entity.Coin;
import com.trading.app.Entity.User;
import com.trading.app.Entity.WatchList;
import com.trading.app.service.CoinService;
import com.trading.app.service.UserService;
import com.trading.app.service.WatchlistService;

@RestController
@RequestMapping("/api/watchlist")
public class WatchlistController {

	@Autowired
	private WatchlistService watchlistService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CoinService coinService;
	
	@GetMapping("/user")
	public ResponseEntity<WatchList> getUserWatchlist(@RequestHeader("Authorization") String jwt) throws Exception
	{ 
		User user = userService.findUserProfileByJwt(jwt);
		WatchList watchList  = watchlistService.findUserWatchlist(user.getId());
		return ResponseEntity.ok(watchList);
		
	}
	
	@GetMapping("/{watchlistId}")
	public ResponseEntity<WatchList> getwatchlistById(@PathVariable Long watchlistId) throws Exception
	{
		WatchList watchList = watchlistService.findById(watchlistId);
		return ResponseEntity.ok(watchList);
	}
	
	@PatchMapping("/add/coin/{coinId}")
	public ResponseEntity<Coin> addItemToWatchlist(@RequestHeader("Authorization") String jwt,@PathVariable String coinId) throws Exception
	{
		
		
		User user =userService.findUserProfileByJwt(jwt);
		
		Coin coin=coinService.findById(coinId);
		
		
		Coin addedCoin = watchlistService.addItemToWatchlist(coin, user);
		
		
		return ResponseEntity.ok(addedCoin);
	
	}

	
}
