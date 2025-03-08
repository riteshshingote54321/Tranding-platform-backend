package com.trading.app.service;

import java.util.List;

import com.trading.app.Domain.OrderType;
import com.trading.app.Entity.Coin;
import com.trading.app.Entity.Order;
import com.trading.app.Entity.OrderItem;
import com.trading.app.Entity.User;

public interface OrderService {
	
	Order createOrder(User user , OrderItem orderItem, OrderType orderType);
	
	Order getOrderById(Long orderId) throws Exception;
	
	List<Order> getAllOrdersOfUser(Long userId , OrderType orderType , String assetSymbol);

	Order processOrder(Coin coin , double quentity , OrderType orderType, User user) throws Exception;
	
}
