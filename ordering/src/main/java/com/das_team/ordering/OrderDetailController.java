package com.das_team.ordering;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

@RestController
public class OrderDetailController {
	
	private OrderDetailRepository orderDetailRepository = new OrderDetailRepository();
	 
	@Operation(summary = "Returns all OrderDetails from the specified Order (orderId)")
    @GetMapping("orders/{orderId}/details")
    public ResponseEntity<List<OrderDetail>> getOrderDetailsByOrderId(@PathVariable int orderId) {
        List<OrderDetail> orderDetails = orderDetailRepository.getOrderDetailsByOrderId(orderId);
        if (orderDetails == null || orderDetails.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(orderDetails, HttpStatus.OK);
        }
    }
	/*
	@Operation(summary = "Creates a new OrderDetail")
	@PostMapping("/order")
	public ResponseEntity<OrderDetail> createOrderDetail(@RequestBody OrderDetail orderDetail) {
		orderDetailRepository.addOrderDetail(orderDetail);
	    return new ResponseEntity<>(orderDetail, HttpStatus.CREATED);
	}
	*/
}
