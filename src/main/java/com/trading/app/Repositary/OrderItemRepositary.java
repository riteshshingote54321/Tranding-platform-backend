package com.trading.app.Repositary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.app.Entity.OrderItem;

public interface OrderItemRepositary extends JpaRepository<OrderItem, Long>{

}
