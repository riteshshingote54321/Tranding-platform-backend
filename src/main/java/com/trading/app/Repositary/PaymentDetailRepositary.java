package com.trading.app.Repositary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.app.Entity.PaymentDetail;

public interface PaymentDetailRepositary extends JpaRepository<PaymentDetail, Long>{

	PaymentDetail findByUserId(Long userId);
}
