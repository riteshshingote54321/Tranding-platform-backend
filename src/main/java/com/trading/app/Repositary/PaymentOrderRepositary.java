package com.trading.app.Repositary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.app.Entity.PaymentOrder;

public interface PaymentOrderRepositary extends JpaRepository<PaymentOrder, Long>{

}
