package com.das_team.ordering;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
public class OrderController {
	
	private OrderRepository orderRepository = new OrderRepository();
	
	@Operation(summary = "Returns all Orders")
    @GetMapping ("orders")
    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }
    
	@Operation(summary = "Returns the Order with the specified Id")
    @GetMapping("orders/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable int id) {
        Order order = orderRepository.getOrderById(id);
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(order, HttpStatus.OK);
        }
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