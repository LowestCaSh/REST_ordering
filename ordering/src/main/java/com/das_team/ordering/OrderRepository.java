package com.das_team.ordering;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

public class OrderRepository{
	
	private List<Order> orders = new ArrayList<>();
	OrderDetailRepository orderDetailRepository = new OrderDetailRepository();
	ShoppingCartRepository cartRepository = new ShoppingCartRepository();
	
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
	

	public int getHighestOrderId() {
	    int highestId = 0;
	    for (Order order : orders) {
	        if (order.getOrderId() > highestId) {
	            highestId = order.getOrderId();
	        }
	    }
	    return highestId;
	}
	
	public void addOrder(int cartId) {
		ShoppingCart cart = cartRepository.getCartById(cartId);
		Order order = new Order();
		order.setOrderId(getHighestOrderId() + 1);
		order.setCustomerId(cart.getCustomerId());
		order.setCustomerName("REST: CustomerName");
		order.setOrderDate(LocalDate.now().toString());
		order.setStreet("REST: Street");
		order.setStreetNumber("REST: StreetNumber");
		order.setAddressAddition("REST: AdressAdition");
		order.setPostalcode(0);
		order.setCity("REST: City");
		order.setCountry("REST: Country");
		order.setStatus("Processing");
		orderDetailRepository.addOrderDetailsFromShoppingCart(order.getOrderId(), cart.getCartDetails());
		order.setOrderDetails(orderDetailRepository.getOrderDetailsByOrderId(order.getOrderId()));
		order.setTotalSum(orderDetailRepository.getOrderDetailsSum(order.getOrderId())); //Calculate the TotalSum of the Order
		orders.add(order);
	}
}