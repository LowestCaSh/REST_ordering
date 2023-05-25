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
		orders.add(new Order(1, 5,
				getCustomerNameFromUrl(5), "2024-12-13",
				getCustomerStreetFromUrl(5),
				getCustomerPostalCodeFromUrl(5),
				getCustomerCityFromUrl(5),
				getCustomerCountryFromUrl(5), "verschickt",
				orderDetailRepository.getOrderDetailsByOrderId(1),
				orderDetailRepository.getOrderDetailsSum(1)));
		orders.add(new Order(2, 3,
				getCustomerNameFromUrl(3), "2005-06-01",
				getCustomerStreetFromUrl(3),
				getCustomerPostalCodeFromUrl(3),
				getCustomerCityFromUrl(3),
				getCustomerCountryFromUrl(3), "ausstehend",
				orderDetailRepository.getOrderDetailsByOrderId(2),
				orderDetailRepository.getOrderDetailsSum(2)));
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
		order.setStreet(getCustomerStreetFromUrl(customerId));
		order.setPostalcode(getCustomerPostalCodeFromUrl(customerId));
		order.setCity(getCustomerCityFromUrl(customerId));
		order.setCountry(getCustomerCountryFromUrl(customerId));
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
		String url = "http://192.168.0.103:8000/customers/" + customerId;
	    HttpURLConnection connection = null;
	    BufferedReader reader = null;
	    StringBuilder response = new StringBuilder();

	    try {
	        URL apiUrl = new URL(url);
	        connection = (HttpURLConnection) apiUrl.openConnection();
	        connection.setRequestMethod("GET");

	        // Set a default timeout of 1 second (1000 milliseconds)
	        connection.setConnectTimeout(10);

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
	        return jsonNode.get("customer").get("FirstName").asText() + " " + jsonNode.get("customer").get("LastName").asText();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "Error: Failed to parse JSON response";
	    }
	}
	
	//CustomerId = AddressID
	public String getCustomerStreetFromUrl(int customerId) {
		String url = "http://192.168.0.103:8000/addresses/" + customerId;
	    HttpURLConnection connection = null;
	    BufferedReader reader = null;
	    StringBuilder response = new StringBuilder();

	    try {
	        URL apiUrl = new URL(url);
	        connection = (HttpURLConnection) apiUrl.openConnection();
	        connection.setRequestMethod("GET");

	        // Set a default timeout of 1 second (1000 milliseconds)
	        connection.setConnectTimeout(10);

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
	        return jsonNode.get("address").get("Street").asText();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "Error: Failed to parse JSON response";
	    }
	}
	
	public String getCustomerPostalCodeFromUrl(int customerId) {
		String url = "http://192.168.0.103:8000/addresses/" + customerId;
	    HttpURLConnection connection = null;
	    BufferedReader reader = null;
	    StringBuilder response = new StringBuilder();

	    try {
	        URL apiUrl = new URL(url);
	        connection = (HttpURLConnection) apiUrl.openConnection();
	        connection.setRequestMethod("GET");

	        // Set a default timeout of 1 second (1000 milliseconds)
	        connection.setConnectTimeout(10);

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
	        return jsonNode.get("address").get("PostalCode").asText();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "Error: Failed to parse JSON response";
	    }
	}
	
	public String getCustomerCityFromUrl(int customerId) {
		String url = "http://192.168.0.103:8000/addresses/" + customerId;
	    HttpURLConnection connection = null;
	    BufferedReader reader = null;
	    StringBuilder response = new StringBuilder();

	    try {
	        URL apiUrl = new URL(url);
	        connection = (HttpURLConnection) apiUrl.openConnection();
	        connection.setRequestMethod("GET");

	        // Set a default timeout of 1 second (1000 milliseconds)
	        connection.setConnectTimeout(10);

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
	        return jsonNode.get("address").get("City").asText();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "Error: Failed to parse JSON response";
	    }
	}
	
	public String getCustomerCountryFromUrl(int customerId) {
		String url = "http://192.168.0.103:8000/addresses/" + customerId;
	    HttpURLConnection connection = null;
	    BufferedReader reader = null;
	    StringBuilder response = new StringBuilder();

	    try {
	        URL apiUrl = new URL(url);
	        connection = (HttpURLConnection) apiUrl.openConnection();
	        connection.setRequestMethod("GET");

	        // Set a default timeout of 1 second (1000 milliseconds)
	        connection.setConnectTimeout(10);

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
	        return jsonNode.get("address").get("Country").asText();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "Error: Failed to parse JSON response";
	    }
	}
}