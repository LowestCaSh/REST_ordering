package com.das_team.ordering;

import java.time.Duration;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
public class OrderController {
	
	private OrderRepository orderRepository = new OrderRepository();
	private ShoppingCartRepository cartRepository = new ShoppingCartRepository();
	
	RateLimiterConfig config = RateLimiterConfig.custom()
            .limitRefreshPeriod(Duration.ofSeconds(1)) //period for refreshing the rate limiter permissions
            .limitForPeriod(10) // number of permissions available per refresh period
            .timeoutDuration(Duration.ofMillis(100)) // The duration after which the acquirePermission() call times out
            .build();
	
	// Create registry
	RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.of(config);

	// Use registry
	RateLimiter rateLimiter = rateLimiterRegistry.rateLimiter("myRateLimiter", config);
	
	@GetMapping ("rateLimiterTest")
	public void cartsFallBackMethod() {
		for (int i = 0; i < 15; i++) {
	        boolean permissionGranted = rateLimiter.acquirePermission();
	        if(permissionGranted) {	
	        	System.out.println("perm granted");
	        }
	        else {
	        	System.out.println("exceeded");
	        }
		}
	}
	
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