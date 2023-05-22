package com.das_team.ordering;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OrderDetailRepository {
	
	private List<OrderDetail> orderDetails = new ArrayList<>();
	
	// Change to known URLs
	public OrderDetailRepository() {
		orderDetails.add(new OrderDetail(1,
				"645c9ffb7d433216f16d7c90",
				getProductNameFromUrl("645c9ffb7d433216f16d7c90"), "Stück",
				getProductPriceFromUrl("645c9ffb7d433216f16d7c90"), 2));
		orderDetails.add(new OrderDetail(2,
				"645c9ffb7d433216f16d7c90",
				getProductNameFromUrl("645c9ffb7d433216f16d7c90"), "Stück",
				getProductPriceFromUrl("645c9ffb7d433216f16d7c90"), 1));
		orderDetails.add(new OrderDetail(1,
				"645c9ffb7d433216f16d7c90",
				getProductNameFromUrl("645c9ffb7d433216f16d7c90"), "Stunde",
				getProductPriceFromUrl("645c9ffb7d433216f16d7c90"), 3));
		orderDetails.add(new OrderDetail(2,
				"645c9ffb7d433216f16d7c90",
				getProductNameFromUrl("645c9ffb7d433216f16d7c90"), "Stück",
				getProductPriceFromUrl("645c9ffb7d433216f16d7c90"), 7));
		orderDetails.add(new OrderDetail(2,
				"645c9ffb7d433216f16d7c90",
				getProductNameFromUrl("645c9ffb7d433216f16d7c90"), "Stück",
				getProductPriceFromUrl("645c9ffb7d433216f16d7c90"), 3));
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
	
	public float getOrderDetailsSum(int orderId) {
		float orderDetailSum = 0f;
		for (OrderDetail orderDetail : orderDetails) {
	        if (orderDetail.getOrderId() == orderId) {
	            orderDetailSum += orderDetail.getUnitprice() * orderDetail.getQuantity();
	        }
	    }
		return orderDetailSum;
	}
	
	public void addOrderDetail(OrderDetail orderDetail) {
			orderDetails.add(orderDetail);

	}
	
	public void addOrderDetail(List<OrderDetail> orderDetails) {
		orderDetails.addAll(orderDetails);
	}
	
	public void addOrderDetailsFromShoppingCart(int orderId, List<ShoppingCartDetail> shoppingCartDetails) {
		for (ShoppingCartDetail shoppingCartDetail : shoppingCartDetails) {
			orderDetails.add(new OrderDetail(
					orderId,
					shoppingCartDetail.getProductId(),
					shoppingCartDetail.getProductName(),
					shoppingCartDetail.getUnit(),
					shoppingCartDetail.getUnitprice(),
					shoppingCartDetail.getQuantity()));
	    }	
	}
	
	public String getProductNameFromUrl(String productId) {
		String url = "http://192.168.0.106:8000/v2/products/" + productId;
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
	        return jsonNode.get("type").asText();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "Error: Failed to parse JSON response";
	    }
	}
	
	public float getProductPriceFromUrl(String productId) {
		String url = "http://192.168.0.106:8000/v2/products/" + productId;
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
	            return 0;
	        }
	    } catch (SocketTimeoutException e) {
	        e.printStackTrace();
	        return 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return 0;
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

	        JsonNode pricesNode = jsonNode.get("prices");
	        if (pricesNode != null && pricesNode.isArray() && pricesNode.size() > 0) {
	            JsonNode lastPriceNode = pricesNode.get(pricesNode.size() - 1);
	            float lastPrice = (float) lastPriceNode.get("price").asDouble();
	            return lastPrice;
	        } else {
	            return 0;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return 0;
	    }
	}
}
