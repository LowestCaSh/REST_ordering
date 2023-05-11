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
	
	/*
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
    }*/
	
	@Operation(summary = "Adds a new OrderDetail for the Specified Order (orderId)",
			responses = { 
				    @ApiResponse(responseCode="201", description = "OrderDetail added successfully, Id changed according to chosen PathVariable"),
				    @ApiResponse(responseCode="400", description = "Invalid input provided"),
				    @ApiResponse(responseCode="404", description = "Specified Order does not exist"),
				    @ApiResponse(responseCode="500", description = "Internal Server Error")
				})
	@PostMapping("orders/{orderId}/details")
	public ResponseEntity<OrderDetail> addOrderDetail(@PathVariable int orderId, @RequestBody OrderDetail orderDetail) {
		if(orderRepository.getOrderById(orderId) != null) {
			orderDetail.setOrderId(orderId);
			orderDetailRepository.addOrderDetail(orderDetail);
			//Add it to the orderDetailsList
			orderRepository.getOrderById(orderId).setOrderDetails(orderDetailRepository.getOrderDetailsByOrderId(orderId));
			//Recalculate TotalSum
			orderRepository.getOrderById(orderId).setTotalSum(orderDetailRepository.getOrderDetailsSum(orderId));
			return new ResponseEntity<>(orderDetail, HttpStatus.CREATED);
		}
		else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    
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