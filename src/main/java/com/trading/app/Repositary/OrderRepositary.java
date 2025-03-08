package com.trading.app.Repositary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.app.Entity.Order;

public interface OrderRepositary extends JpaRepository<Order, Long>{
	List<Order> findByUserId(Long userId);

}
