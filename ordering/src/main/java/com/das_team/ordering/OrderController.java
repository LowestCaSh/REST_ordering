package com.das_team.ordering;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;


@RestController
public class OrderController {
	
	private OrderRepository orderRepository = new OrderRepository();
	
    @GetMapping ("ordering/orders")
    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }
       
    @GetMapping("ordering/orders/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable int id) {
        Order order = orderRepository.getOrderById(id);
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(order, HttpStatus.OK);
        }
    }
}