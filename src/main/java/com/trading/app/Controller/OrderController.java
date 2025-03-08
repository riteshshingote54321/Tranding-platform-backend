package com.trading.app.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trading.app.Domain.OrderType;
import com.trading.app.Entity.Coin;
import com.trading.app.Entity.Order;
import com.trading.app.Entity.User;
import com.trading.app.request.CreateOrderRequest;
import com.trading.app.service.CoinService;
import com.trading.app.service.OrderService;
import com.trading.app.service.UserService;

@RestController
@RequestMapping("api/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CoinService coinService;
	
//	@Autowired
//	private WalletTransactionService walletTransactionService;
	
	@PostMapping("/pay")
	public ResponseEntity<Order> payOrderPayment(@RequestHeader("Authorization")String jwt,@RequestBody CreateOrderRequest req) throws Exception
	{
		User user = userService.findUserProfileByJwt(jwt);
		Coin coin = coinService.findById(req.getCoidId());
		
		Order order = orderService.processOrder(coin, req.getQuantity(), req.getOrderType(), user);
		
		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/{orderId}")
	public ResponseEntity<Order> getOrderId(@RequestHeader("Authorization") String jwtToken ,@PathVariable Long orderId ) throws Exception{
		
		
		User user = userService.findUserProfileByJwt(jwtToken);
		
		Order order = orderService.getOrderById(orderId);
		
		if(order.getUser().getId().equals(user.getId()))
		{
			return ResponseEntity.ok(order);
		}else {
			throw new Exception("You Don't Have access");
		}
		
	}
	
	@GetMapping
	public ResponseEntity<List<Order>> getAllOrderForUser(@RequestHeader("Authorization") String jwt , @RequestParam(required = false)OrderType order_type, @RequestParam(required = false) String asset_symbol) throws Exception{
		
		Long userId = userService.findUserProfileByJwt(jwt).getId();
		
		List<Order> userOrders = orderService.getAllOrdersOfUser(userId, order_type, asset_symbol);
		
		return ResponseEntity.ok(userOrders);
	}
	
	
}
