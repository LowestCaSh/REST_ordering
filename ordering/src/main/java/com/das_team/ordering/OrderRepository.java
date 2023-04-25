package com.das_team.ordering;

import java.util.List;
import java.util.ArrayList;

public class OrderRepository{
	
	private List<Order> orders = new ArrayList<>();
	OrderDetailRepository orderDetailRepository = new OrderDetailRepository();
	
	public OrderRepository() {
		orders.add(new Order(1, 3, "Hans Zimmermann", "2024-12-13", "Musterstraße", "1a", "WG 9", 44862, "Zweibrücken", "Deutschland", "verschickt", orderDetailRepository.getOrderDetailsByOrderId(1), orderDetailRepository.getOrderDetailsSum(1)));
		orders.add(new Order(2, 9, "Peter Müller", "2005-06-01", "Coole Straße", "29", null, 66482, "Kaiserslautern", "Deutschland", "ausstehend", orderDetailRepository.getOrderDetailsByOrderId(2), orderDetailRepository.getOrderDetailsSum(2)));
	}
	
	public List<Order> getAllOrders() {
        return orders;
    }
	
	public Order getOrderById(int orderId) {
		
		for (Order order : orders) {
	        if (order.getOrderId() == orderId) {        	
	            return order;
	        }
	    }
	    return null;
    }
}