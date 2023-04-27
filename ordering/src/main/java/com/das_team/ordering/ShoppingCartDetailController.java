package com.das_team.ordering;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

@RestController
public class ShoppingCartDetailController {
	
	private ShoppingCartDetailRepository cartDetailRepository = new ShoppingCartDetailRepository();
	 
	@Operation(summary = "Returns all ShoppingCartDetails from the specified ShoppingCart (cartId)")
    @GetMapping("carts/{cartId}/details")
    public ResponseEntity<List<ShoppingCartDetail>> getOrderDetailsByOrderId(@PathVariable int cartId) {
        List<ShoppingCartDetail> cartDetails = cartDetailRepository.getCartDetailsByCartId(cartId);
        if (cartDetails == null || cartDetails.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(cartDetails, HttpStatus.OK);
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