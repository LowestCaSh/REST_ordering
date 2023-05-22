package com.das_team.ordering;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.time.LocalDate;

public class OrderRepository{
	
	private List<Order> orders = new ArrayList<>();
	OrderDetailRepository orderDetailRepository = new OrderDetailRepository();
	ShoppingCartRepository cartRepository = new ShoppingCartRepository();
	
	// Change to known URLs
	public OrderRepository() {
		orders.add(new Order(1, 3,
				getCustomerNameFromUrl(9), "2024-12-13", "Musterstraße", "1a", "WG 9", 44862, "Zweibrücken", "Deutschland", "verschickt", orderDetailRepository.getOrderDetailsByOrderId(1), orderDetailRepository.getOrderDetailsSum(1)));
		orders.add(new Order(2, 9,
				getCustomerNameFromUrl(9), "2005-06-01", "Coole Straße", "29", null, 66482, "Kaiserslautern", "Deutschland", "ausstehend", orderDetailRepository.getOrderDetailsByOrderId(2), orderDetailRepository.getOrderDetailsSum(2)));
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
	
	//Change to known URL
	public void addOrder(int cartId) {
		ShoppingCart cart = cartRepository.getCartById(cartId);
		int customerId = cart.getCustomerId();
		Order order = new Order();
		order.setOrderId(getHighestOrderId() + 1);
		order.setCustomerId(customerId);
		order.setCustomerName(getCustomerNameFromUrl(customerId));
		order.setOrderDate(LocalDate.now().toString());
		order.setStreet("REST: Street");
		order.setStreetNumber("REST: StreetNumber");
		order.setAddressAddition("REST: AdressAdition");
		order.setPostalcode(0);
		order.setCity("REST: City");
		order.setCountry("REST: Country");
		order.setStatus("Processing");
		//Add ShoppingCartDetails to OrderDetails
		orderDetailRepository.addOrderDetailsFromShoppingCart(order.getOrderId(), cart.getCartDetails());
		order.setOrderDetails(orderDetailRepository.getOrderDetailsByOrderId(order.getOrderId()));
		//TODO: Empty the ShppingCartDetails from the cart
		order.setTotalSum(orderDetailRepository.getOrderDetailsSum(order.getOrderId())); //Calculate the TotalSum of the Order
		orders.add(order);
		cartRepository.clearShoppingCartDetails(cartId);
	}
	
	public String getCustomerNameFromUrl(int customerId) {
		String url = "http://127.0.0.1:5000/customers/" + customerId;
	    HttpURLConnection connection = null;
	    BufferedReader reader = null;
	    StringBuilder response = new StringBuilder();

	    try {
	        URL apiUrl = new URL(url);
	        connection = (HttpURLConnection) apiUrl.openConnection();
	        connection.setRequestMethod("GET");

	        // Set a default timeout of 1 second (1000 milliseconds)
	        connection.setConnectTimeout(1000);

	        int responseCode = connection.getResponseCode();
	        if (responseCode == HttpURLConnection.HTTP_OK) {
	            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	            String line;
	            while ((line = reader.readLine()) != null) {
	                response.append(line);
	            }
	        } else {
	            return "Error: " + responseCode;
	        }
	    } catch (SocketTimeoutException e) {
	        return "Error: Timeout occurred";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "Error: " + e.getMessage();
	    } finally {
	        try {
	            if (reader != null) {
	                reader.close();
	            }
	            if (connection != null) {
	                connection.disconnect();
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    try {
	        ObjectMapper objectMapper = new ObjectMapper();
	        JsonNode jsonNode = objectMapper.readTree(response.toString());
	        return jsonNode.get("FirstName").asText() + jsonNode.get("LastName").asText();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "Error: Failed to parse JSON response";
	    }
	}
}