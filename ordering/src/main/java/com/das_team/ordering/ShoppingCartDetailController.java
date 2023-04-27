package com.das_team.ordering;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


@RestController
public class ShoppingCartDetailController {
	
	private ShoppingCartDetailRepository cartDetailRepository = new ShoppingCartDetailRepository();

	/*
	@Operation(summary = "Creates a new OrderDetail")
	@PostMapping("/order")
	public ResponseEntity<OrderDetail> createOrderDetail(@RequestBody OrderDetail orderDetail) {
		orderDetailRepository.addOrderDetail(orderDetail);
	    return new ResponseEntity<>(orderDetail, HttpStatus.CREATED);
	}
	*/
}