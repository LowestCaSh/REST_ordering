package com.das_team.ordering;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailRepository {
	
	private List<OrderDetail> orderDetails = new ArrayList<>();;
	
	public OrderDetailRepository() {
		orderDetails.add(new OrderDetail(1, 5, "Kühlschrank Triple Deluxe", "Stück", 3000.0f, 2));
		orderDetails.add(new OrderDetail(1, 3, "Waschmaschine 2679A", "Stück", 399.99f, 1));
		orderDetails.add(new OrderDetail(1, 2, "Waschmaschine reparieren", "Stunde", 50.0f, 3));
		orderDetails.add(new OrderDetail(2, 10, "Mikrowelle 300W", "Stück", 59.99f, 7));
		orderDetails.add(new OrderDetail(2, 3, "Spülmaschine Megasauber", "Stück", 250.50f, 3));
	}
	
	public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
		List<OrderDetail> orderDetailsByOrderId = new ArrayList<>();
		
		for (OrderDetail orderDetail : orderDetails) {
	        if (orderDetail.getOrderId() == orderId) {
	            orderDetailsByOrderId.add(orderDetail);
	        }
	    }
	    return orderDetailsByOrderId;
    }
}
