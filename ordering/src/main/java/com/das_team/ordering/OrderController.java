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
	
	@Operation(
		summary = "Returns all OrderDetails from the specified Order (orderId)",
		responses = { 
		    @ApiResponse(responseCode="200", description = "Successfully retrieved OrderDetails from specified Order"),
		    @ApiResponse(responseCode="404", description = "Specified Order could not be found"),
		    @ApiResponse(responseCode="500", description = "Internal Server Error")
		}
	)
	@GetMapping("orders/{orderId}/details")
    public ResponseEntity<List<OrderDetail>> getOrderDetailsByOrderId(@PathVariable int orderId) {
		//We need to check if the Order with orderId exists
		if(orderRepository.getOrderById(orderId) != null) {
			List<OrderDetail> orderDetails = orderDetailRepository.getOrderDetailsByOrderId(orderId);
            return new ResponseEntity<>(orderDetails, HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

	/*
	@Operation(summary = "Creates a new Order")
	@PostMapping("/orders")
	public ResponseEntity<Order> createOrder(@RequestBody Order order) {
	    orderRepository.addOrder(order);
	    return new ResponseEntity<>(order, HttpStatus.CREATED);
	}
	*/
}