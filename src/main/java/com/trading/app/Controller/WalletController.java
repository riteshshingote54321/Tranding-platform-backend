package com.trading.app.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trading.app.Wallet;
import com.trading.app.Entity.Order;
import com.trading.app.Entity.PaymentOrder;
import com.trading.app.Entity.User;
import com.trading.app.Entity.WalletTransaction;
import com.trading.app.Responce.PaymentResponce;
import com.trading.app.service.OrderService;
import com.trading.app.service.PaymentService;
import com.trading.app.service.UserService;
import com.trading.app.service.WalletService;

@RestController
public class WalletController {
	
	@Autowired
	private WalletService walletService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private PaymentService paymentService;
	
	
	@GetMapping("/api/wallet")
	public ResponseEntity<Wallet> getUserWallet(@RequestHeader("Authorization") String jwt) throws Exception
	{
		User user = userService.findUserProfileByJwt(jwt);
		Wallet wallet = walletService.getUserWallet(user);
		return new ResponseEntity<>(wallet , HttpStatus.ACCEPTED);
	}

	
	@PutMapping("/api/wallet/{walletId}/transfer")
	public ResponseEntity<Wallet> walletToWalletTransfer(
			@RequestHeader("Authorization")String jwt,
			@PathVariable Long walletId,
			@RequestBody WalletTransaction req
			) throws Exception
	{
		User sendUser = userService.findUserProfileByJwt(jwt);
		Wallet receiverWallet=walletService.findWalletById(walletId);
		Wallet wallet =walletService.walletToWalletTransfer(sendUser, receiverWallet, req.getAmount());
		return new ResponseEntity<>(wallet , HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/api/wallet/order/{orderId}/pay")
	public ResponseEntity<Wallet> payOrderPayment(
			@RequestHeader("Authorization")String jwt,
			@PathVariable Long orderId
			
			) throws Exception
	{
		User user = userService.findUserProfileByJwt(jwt);
		
		Order order = orderService.getOrderById(orderId);
		
		Wallet wallet = walletService.payOrderPayment(order, user);
		
		return new ResponseEntity<>(wallet , HttpStatus.ACCEPTED);
	}
	
	
	@PutMapping("/api/wallet/deposit")
	public ResponseEntity<Wallet> addBalanceToWallet(
			@RequestHeader("Authorization")String jwt,
			@RequestParam(name= "order_id") Long order_Id,
			@RequestParam(name="Payment_Id") String paymentId
			
			
			) throws Exception
	{
		User user = userService.findUserProfileByJwt(jwt);
		
		Wallet wallet = walletService.getUserWallet(user);
		
		PaymentOrder order = paymentService.getPaymentOrderById(order_Id);
		
		Boolean status = paymentService.ProccedPaymentOrder(order, paymentId);
		
		if(status)
		{
			wallet = walletService.addBalance(wallet, order.getAmount());
		}
		
		return new ResponseEntity<>(wallet , HttpStatus.ACCEPTED);
	}
	
	
	
	
}



