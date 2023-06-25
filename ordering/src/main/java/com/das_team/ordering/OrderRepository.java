package com.das_team.ordering;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
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
	private static String url_teamPinaColada = "http://192.168.0.101:8000/";
	
	// Change to known URLs
	public OrderRepository() {
		Map<String, String> customerInfo;
		
		customerInfo = getCustomerInfoFromUrl(5);
		orders.add(new Order(1, 5,
				customerInfo.get("name"),
				"2024-12-13",
				customerInfo.get("street"),
				customerInfo.get("postalCode"),
				customerInfo.get("city"),
				customerInfo.get("country"),
				"verschickt",
				orderDetailRepository.getOrderDetailsByOrderId(1),
				orderDetailRepository.getOrderDetailsSum(1)));
		
		customerInfo = getCustomerInfoFromUrl(3);
		orders.add(new Order(2, 3,
				customerInfo.get("name"),
				"2005-06-01",
				customerInfo.get("street"),
				customerInfo.get("postalCode"),
				customerInfo.get("city"),
				customerInfo.get("country"), "ausstehend",
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
	
	public void addOrder(int cartId) {
		ShoppingCart cart = cartRepository.getCartById(cartId);
		int customerId = cart.getCustomerId();
		Map<String, String> customerInfo = getCustomerInfoFromUrl(customerId);
		
		Order order = new Order();
		order.setOrderId(getHighestOrderId() + 1);
		order.setCustomerId(customerId);
		order.setCustomerName(customerInfo.get("name"));
		order.setOrderDate(LocalDate.now().toString());
		order.setStreet(customerInfo.get("street"));
		order.setPostalcode(customerInfo.get("postalCode"));
		order.setCity(customerInfo.get("city"));
		order.setCountry(customerInfo.get("country"));
		order.setStatus("Processing");
		//Add ShoppingCartDetails to OrderDetails
		orderDetailRepository.addOrderDetailsFromShoppingCart(order.getOrderId(), cart.getCartDetails());
		order.setOrderDetails(orderDetailRepository.getOrderDetailsByOrderId(order.getOrderId()));
		
		order.setTotalSum(orderDetailRepository.getOrderDetailsSum(order.getOrderId())); //Calculate the TotalSum of the Order
		orders.add(order);
		cartRepository.clearShoppingCartDetails(cartId);
	}
	
	public Map<String, String> getCustomerInfoFromUrl(int customerId) {
	    String url = url_teamPinaColada + "customers/" + customerId;
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
	            throw new RuntimeException("Error: " + responseCode);
	        }
	    } catch (SocketTimeoutException e) {
	        throw new RuntimeException("Error: Timeout occurred");
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Error: " + e.getMessage());
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
	        Map<String, String> customerInfo = new HashMap<>();
	        customerInfo.put("name", jsonNode.get("customer").get("FirstName").asText()+jsonNode.get("customer").get("LastName").asText());
	        customerInfo.put("street", jsonNode.get("address").get("Street").asText());
	        customerInfo.put("postalCode", jsonNode.get("address").get("PostalCode").asText());
	        customerInfo.put("city", jsonNode.get("address").get("City").asText());
	        customerInfo.put("country", jsonNode.get("address").get("Country").asText());

	        return customerInfo;
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Error: Failed to parse JSON response");
	    }
	}
	
	/*
	public String getCustomerNameFromUrl(int customerId) {
		String url = url_teamPinaColada + "customers/" + customerId;
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
		String url = url_teamPinaColada + "addresses/" + customerId;
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
		String url = url_teamPinaColada + "addresses/" + customerId;
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
		String url = url_teamPinaColada + "addresses/" + customerId;
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
		String url = url_teamPinaColada + "addresses/" + customerId;
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
	*/
}