package com.trading.app.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trading.app.Domain.OrderStatus;
import com.trading.app.Domain.OrderType;
import com.trading.app.Entity.Asset;
import com.trading.app.Entity.Coin;
import com.trading.app.Entity.Order;
import com.trading.app.Entity.OrderItem;
import com.trading.app.Entity.User;
import com.trading.app.Repositary.OrderItemRepositary;
import com.trading.app.Repositary.OrderRepositary;

import jakarta.transaction.Transactional;

@Service 
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepositary orderRepositary;
	
	@Autowired
	private WalletService walletService;
	
	@Autowired
	private OrderItemRepositary orderItemRepositary;
	
	@Autowired
	private AssetService assetService;
	
	
	@Override
	public Order createOrder(User user, OrderItem orderItem, OrderType orderType) {

		double price= orderItem.getCoin().getCurrentPrice()*orderItem.getQuantity();
		
		Order order = new Order();
		order.setUser(user);
		order.setOrderItem(orderItem);
		order.setOrderType(orderType);;
		order.setPrice(BigDecimal.valueOf(price));
		order.setTimestamp(LocalDateTime.now());
		order.setStatus(OrderStatus.PENDING);
		
		
		return orderRepositary.save(order);
	}

	@Override
	public Order getOrderById(Long orderId) throws Exception {
		return orderRepositary.findById(orderId).orElseThrow(()-> new Exception("order Not found "));
	}

	@Override
	public List<Order> getAllOrdersOfUser(Long userId, OrderType orderType, String assetSymbol) {
		return orderRepositary.findByUserId(userId);
	}

	
	private OrderItem createOrderItem(Coin coin , double quantity, double buyPrice, double sellProce) {
		OrderItem orderItem = new OrderItem();
		orderItem.setCoin(coin);
		orderItem.setQuantity(quantity);
		orderItem.setBuyPrice(buyPrice);
		orderItem.setSellPrice(sellProce);
		return orderItemRepositary.save(orderItem); 
	}
	

	@Transactional
	private Order buyAsset(Coin coin , double quantity , User user) throws Exception
	{
		if(quantity <= 0)
		{
			throw new Exception("Quantity should be > 0");
		}
		double buyPrice=coin.getCurrentPrice();
		
		OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, 0);
		
		
		Order order =createOrder(user, orderItem, OrderType.BUY);
		orderItem.setOrder(order);
		
		
		walletService.payOrderPayment(order, user);
		
		order.setStatus(OrderStatus.SUCCESS);
		order.setOrderType(OrderType.BUY);
		Order savedOrder = orderRepositary.save(order); 
		
		//create asset
		
		Asset oldAsset= assetService.findAssetByUserIdAndCoinId(order.getUser().getId(), order.getOrderItem().getCoin().getId());
		
		if(oldAsset == null)
		{
			assetService.createAsset(user, orderItem.getCoin(), orderItem.getQuantity());
		}
		else {
			assetService.updateAsset(oldAsset.getId(), quantity);
		}
		return savedOrder;
	}
	
	
	@Transactional
	private Order sellAsset(Coin coin , double quantity , User user) throws Exception
	{
		if(quantity <= 0)
		{
			throw new Exception("Quantity should be > 0");
		}
		
		double sellPrice=coin.getCurrentPrice();
		
		Asset assetToSell=assetService.findAssetByUserIdAndCoinId(user.getId(), coin.getId());
		double buyPrice=assetToSell.getBuyPrice();
		if(assetToSell != null)
		{
			OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, sellPrice);
		
		
		Order order =createOrder(user, orderItem, OrderType.SELL);
		orderItem.setOrder(order);
		
		if(assetToSell.getQuantity()>=quantity) {
			
			order.setStatus(OrderStatus.SUCCESS);
			order.setOrderType(OrderType.SELL);
			Order savedOrder = orderRepositary.save(order); 
			
			
			walletService.payOrderPayment(order, user);
			
			Asset updatedAsset=assetService.updateAsset(assetToSell.getId() ,-quantity);
			 
			if(updatedAsset.getQuantity()*coin.getCurrentPrice()<=1) {
				assetService.deleteAsset(updatedAsset.getId());
			}
			
			
			
			 
			return savedOrder;
		}
		throw new Exception("Insufficient Quantity to Sell");
	}
	
	throw new Exception("Asset Not found");
		
		
		
		
	}
	
	
	
	
	
	@Override
	@Transactional 
	public Order processOrder(Coin coin, double quentity, OrderType orderType, User user) throws Exception {
		if(orderType.equals(orderType.BUY)) {
			return buyAsset(coin, quentity, user);
		}else if(orderType.equals(orderType.SELL)) {
			return sellAsset(coin, quentity, user);
		}
		throw new Exception("Invalid Order Type");
	}

}
