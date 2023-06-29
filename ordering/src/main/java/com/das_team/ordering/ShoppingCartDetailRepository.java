package com.das_team.ordering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class ShoppingCartDetailRepository {
	
	private List<ShoppingCartDetail> cartDetails = new ArrayList<>();
	private static String url_team404 = "http://192.168.0.100:8000/v2/";
	
	//Change to known URLs
	public ShoppingCartDetailRepository() {
		Map<String, String> detailInfo;
		
		detailInfo = getDetailInfoFromUrl("services", "645cd9fce3ca8b1fac72544a");		
	    cartDetails.add(new ShoppingCartDetail(1,
	    		"645cd9fce3ca8b1fac72544a",
	    		detailInfo.get("name"), "services", "Stunde", 
	    		Float.parseFloat(detailInfo.get("price")), 2));
	    
	    detailInfo = getDetailInfoFromUrl("products", "645c9ffb7d433216f16d7c8f");
		cartDetails.add(new ShoppingCartDetail(1,
				"645c9ffb7d433216f16d7c8f",
				detailInfo.get("name"), "products", "Stück", 
				Float.parseFloat(detailInfo.get("price")), 1));
		
		detailInfo = getDetailInfoFromUrl("services", "645cd9fce3ca8b1fac725449");
		cartDetails.add(new ShoppingCartDetail(1,
				"645cd9fce3ca8b1fac725449",
				detailInfo.get("name"), "services", "Stunde", 
				Float.parseFloat(detailInfo.get("price")), 3));
		
		detailInfo = getDetailInfoFromUrl("products", "645c9ffb7d433216f16d7c8d");
		cartDetails.add(new ShoppingCartDetail(2,
				"645c9ffb7d433216f16d7c8d",
				detailInfo.get("name"), "products", "Stück",
				Float.parseFloat(detailInfo.get("price")), 7));
		
		detailInfo = getDetailInfoFromUrl("products", "645c9ffb7d433216f16d7c8c");
		cartDetails.add(new ShoppingCartDetail(2,
				"645c9ffb7d433216f16d7c8c",
				detailInfo.get("name"), "products", "Stück", 
				Float.parseFloat(detailInfo.get("price")), 3));
		
		detailInfo = getDetailInfoFromUrl("services", "645cd9fce3ca8b1fac725448");
		cartDetails.add(new ShoppingCartDetail(3,
				"645cd9fce3ca8b1fac725448",
				detailInfo.get("name"), "services", "Stunde", 
				Float.parseFloat(detailInfo.get("price")), 2));
		
		detailInfo = getDetailInfoFromUrl("products", "645c9ffb7d433216f16d7c8a");
		cartDetails.add(new ShoppingCartDetail(4,
				"645c9ffb7d433216f16d7c8a",
				detailInfo.get("name"), "products", "Stück",
				Float.parseFloat(detailInfo.get("price")), 3));
		
		detailInfo = getDetailInfoFromUrl("services", "645cd9fce3ca8b1fac725447");
		cartDetails.add(new ShoppingCartDetail(4,
				"645cd9fce3ca8b1fac725447",
				detailInfo.get("name"), "services", "Stunde",
				Float.parseFloat(detailInfo.get("price")), 20));
		
		detailInfo = getDetailInfoFromUrl("products", "645c9ffb7d433216f16d7c88");
		cartDetails.add(new ShoppingCartDetail(4,
				"645c9ffb7d433216f16d7c88",
				detailInfo.get("name"), "products", "Stück",
				Float.parseFloat(detailInfo.get("price")), 1));
	}
	
	public List<ShoppingCartDetail> getCartDetailsByCartId(int cartId) {
		List<ShoppingCartDetail> cartDetailsByCartId = new ArrayList<>();
		for (ShoppingCartDetail cartDetail : cartDetails) {
	        if (cartDetail.getCartId() == cartId) {
	            cartDetailsByCartId.add(cartDetail);
	        }
	    }
	    return cartDetailsByCartId;
    }
	
	public float getCartDetailsSum(int cartId) {
		float shoppingCartDetailsSum = 0;
		for (ShoppingCartDetail cartDetail : cartDetails) {
	        if (cartDetail.getCartId() == cartId) {
	            shoppingCartDetailsSum += cartDetail.getUnitprice() * cartDetail.getQuantity();
	        }
	    }
		return shoppingCartDetailsSum;
	}
	
	public void addShoppingCartDetail(ShoppingCartDetail shoppingCartDetail) {
		cartDetails.add(shoppingCartDetail);
	}
	
	public void addShoppingCartDetail(List<ShoppingCartDetail> shoppingCartDetail) {
		cartDetails.addAll(shoppingCartDetail);
	}
	
	public void removeShoppingCartDetail(int cartId, String detailId) {
		for(ShoppingCartDetail shoppingCartDetail : cartDetails) {
			if(shoppingCartDetail.getCartId() == cartId && shoppingCartDetail.getDetailId() == detailId) {
				cartDetails.remove(shoppingCartDetail);
				break;
			}
		}
	}
	
	public void removeShoppingCartDetail(int cartId) {
	    Iterator<ShoppingCartDetail> iterator = cartDetails.iterator();
	    while (iterator.hasNext()) {
	        ShoppingCartDetail shoppingCartDetail = iterator.next();
	        if (shoppingCartDetail.getCartId() == cartId) {
	            iterator.remove();
	        }
	    }
	    
	}
	
	public Map<String, String> getDetailInfoFromUrl(String detailType, String detailId) {
	    String url = url_team404 + detailType + "/" + detailId;
	    HttpURLConnection connection = null;
	    BufferedReader reader = null;
	    StringBuilder response = new StringBuilder();
	    
	    Map<String, String> detailInfo = new HashMap<>();
	    detailInfo.put("name", "Couldn't retreive Name");
	    detailInfo.put("price", "0.0");

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
	            return detailInfo;
	        }
	    } catch (SocketTimeoutException e) {
	        return detailInfo;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return detailInfo;
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
	        return detailInfo;
	    }
	}
}