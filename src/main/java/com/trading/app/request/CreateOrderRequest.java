package com.trading.app.request;

import com.trading.app.Domain.OrderType;

public class CreateOrderRequest {
	
	private String coidId;
	
	private double quantity;
	
	private OrderType orderType;

	
	
	public String getCoidId() {
		return coidId;
	}

	public void setCoidId(String coidId) {
		this.coidId = coidId;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}
	
	

}
