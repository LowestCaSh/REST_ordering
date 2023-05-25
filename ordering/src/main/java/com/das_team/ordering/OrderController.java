package com.das_team.ordering;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
public class OrderController {
	
	private OrderRepository orderRepository = new OrderRepository();
	private OrderDetailRepository orderDetailRepository = new OrderDetailRepository();
	private ShoppingCartRepository cartRepository = new ShoppingCartRepository();
	
	@Operation(summary = "Returns all Orders",
			responses = { 
				    @ApiResponse(responseCode="200", description = "Successfully retrieved all Orders"),
				    @ApiResponse(responseCode="404", description = "Orders could not be found"),
				    @ApiResponse(responseCode="500", description = "Internal Server Error")
				}
			)
    @GetMapping ("orders")
    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }
    
	@Operation(summary = "Returns the Order with the specified Id",
			responses = { 
				    @ApiResponse(responseCode="200", description = "Successfully retrieved specified Order"),
				    @ApiResponse(responseCode="404", description = "Specified Order could not be found"),
				    @ApiResponse(responseCode="500", description = "Internal Server Error")
				}
			)
    @GetMapping("orders/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable int orderId) {
        Order order = orderRepository.getOrderById(orderId);
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(order, HttpStatus.OK);
        }
    }

	@Operation(summary = "Creates a new Order from specified ShoppingCart",
			responses = { 
				    @ApiResponse(responseCode="201", description = "Order created successfully"),
				    @ApiResponse(responseCode="400", description = "Invalid input provided"),
				    @ApiResponse(responseCode="404", description = "Specified ShoppingCart not found"),
				    @ApiResponse(responseCode="500", description = "Internal Server Error")
				})
	@PostMapping("orders/{cartId}")
	public ResponseEntity<Order> createOrder(@PathVariable int cartId) {
		if(cartRepository.getCartById(cartId) != null) {
			orderRepository.addOrder(cartId);
			return new ResponseEntity<>(orderRepository.getOrderById(orderRepository.getHighestOrderId()), HttpStatus.CREATED);
		}
		else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}