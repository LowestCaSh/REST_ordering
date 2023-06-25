package com.das_team.ordering;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OrderDetailRepository {
	
	private List<OrderDetail> orderDetails = new ArrayList<>();
	private static String url_team404 = "http://192.168.0.100:8000/v2/";
	
	// Change to known URLs
	public OrderDetailRepository() {
		Map<String, String> detailInfo;
		
		detailInfo = getDetailInfoFromUrl("products", "645c9ffb7d433216f16d7c87");
		orderDetails.add(new OrderDetail(1,
				"645c9ffb7d433216f16d7c87",
				detailInfo.get("name"), "products", "Stück",
				Float.parseFloat(detailInfo.get("price")), 2));
		
		detailInfo = getDetailInfoFromUrl("products", "645c9ffb7d433216f16d7c86");
		orderDetails.add(new OrderDetail(2,
				"645c9ffb7d433216f16d7c86",
				detailInfo.get("name"), "products", "Stück",
				Float.parseFloat(detailInfo.get("price")), 1));
		
		detailInfo = getDetailInfoFromUrl("services", "645cd9fce3ca8b1fac72544c");
		orderDetails.add(new OrderDetail(1,
				"645cd9fce3ca8b1fac72544c",
				detailInfo.get("name"), "services", "Stunde",
				Float.parseFloat(detailInfo.get("price")), 3));
		
		detailInfo = getDetailInfoFromUrl("products", "645c9ffb7d433216f16d7c84");
		orderDetails.add(new OrderDetail(2,
				"645c9ffb7d433216f16d7c84",
				detailInfo.get("name"), "products", "Stück",
				Float.parseFloat(detailInfo.get("price")), 7));
		
		detailInfo = getDetailInfoFromUrl("services", "645cd9fce3ca8b1fac72544b");
		orderDetails.add(new OrderDetail(2,
				"645cd9fce3ca8b1fac72544b",
				detailInfo.get("name"), "services", "Stunde",
				Float.parseFloat(detailInfo.get("price")), 3));
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
					shoppingCartDetail.getDetailId(),
					shoppingCartDetail.getDetailName(),
					shoppingCartDetail.getDetailType(),
					shoppingCartDetail.getUnit(),
					shoppingCartDetail.getUnitprice(),
					shoppingCartDetail.getQuantity()));
	    }	
	}
	
	public Map<String, String> getDetailInfoFromUrl(String detailType, String detailId) {
	    String url = url_team404 + detailType + "/" + detailId;
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
	        Map<String, String> detailInfo = new HashMap<>();
	        detailInfo.put("name", jsonNode.get("name").asText());
	        
	        JsonNode pricesNode = jsonNode.get("prices");
	        if (pricesNode != null && pricesNode.isArray() && pricesNode.size() > 0) {
	            JsonNode lastPriceNode = pricesNode.get(pricesNode.size() - 1);
	            float lastPrice = (float) lastPriceNode.get("price").asDouble();
	            detailInfo.put("price", Float.toString(lastPrice));
	        } else {
	            detailInfo.put("price", "0.0");
	        }
	    
	        return detailInfo;
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Error: Failed to parse JSON response");
	    }
	}
	
	/*
	public String getDetailNameFromUrl(String detailType, String detailId) {
		String url = url_team404 + detailType + "/" + detailId;
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
	        return jsonNode.get("name").asText();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "Error: Failed to parse JSON response";
	    }
	}
	
	public float getDetailPriceFromUrl(String detailType, String productId) {
		String url = url_team404 + detailType + "/" + productId;
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
	*/
}
