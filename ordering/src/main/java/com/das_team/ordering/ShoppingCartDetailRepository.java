package com.das_team.ordering;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
	
	//Change to known URLs
	public ShoppingCartDetailRepository() {
	    cartDetails.add(new ShoppingCartDetail(1,
	    		"645cd9fce3ca8b1fac72544a",
	    		getDetailNameFromUrl("services", "645cd9fce3ca8b1fac72544a"), "services", "Stunde", 
	    		getDetailPriceFromUrl("services", "645cd9fce3ca8b1fac72544a"), 2));
		cartDetails.add(new ShoppingCartDetail(1,
				"645c9ffb7d433216f16d7c8f",
				getDetailNameFromUrl("products", "645c9ffb7d433216f16d7c8f"), "products", "Stück", 
				getDetailPriceFromUrl("products", "645c9ffb7d433216f16d7c8f"), 1));
		cartDetails.add(new ShoppingCartDetail(1,
				"645cd9fce3ca8b1fac725449",
				getDetailNameFromUrl("services", "645cd9fce3ca8b1fac725449"), "services", "Stunde", 
				getDetailPriceFromUrl("services", "645cd9fce3ca8b1fac725449"), 3));
		cartDetails.add(new ShoppingCartDetail(2,
				"645c9ffb7d433216f16d7c8d",
				getDetailNameFromUrl("products", "645c9ffb7d433216f16d7c8d"), "products", "Stück",
				getDetailPriceFromUrl("products", "645c9ffb7d433216f16d7c8d"), 7));
		cartDetails.add(new ShoppingCartDetail(2,
				"645c9ffb7d433216f16d7c8c",
				getDetailNameFromUrl("products", "645c9ffb7d433216f16d7c8c"), "products", "Stück", 
				getDetailPriceFromUrl("products", "645c9ffb7d433216f16d7c8c"), 3));
		cartDetails.add(new ShoppingCartDetail(3,
				"645cd9fce3ca8b1fac725448",
				getDetailNameFromUrl("services", "645cd9fce3ca8b1fac725448"), "services", "Stunde", 
				getDetailPriceFromUrl("services", "645cd9fce3ca8b1fac725448"), 2));
		cartDetails.add(new ShoppingCartDetail(4,
				"645c9ffb7d433216f16d7c8a",
				getDetailNameFromUrl("products", "645c9ffb7d433216f16d7c8a"), "products", "Stück",
				getDetailPriceFromUrl("products", "645c9ffb7d433216f16d7c8a"), 3));
		cartDetails.add(new ShoppingCartDetail(4,
				"645cd9fce3ca8b1fac725447",
				getDetailNameFromUrl("services", "645cd9fce3ca8b1fac725447"), "services", "Stunde",
				getDetailPriceFromUrl("services", "645cd9fce3ca8b1fac725447"), 20));
		cartDetails.add(new ShoppingCartDetail(4,
				"645c9ffb7d433216f16d7c88",
				getDetailNameFromUrl("products", "645c9ffb7d433216f16d7c88"), "products", "Stück",
				getDetailPriceFromUrl("products", "645c9ffb7d433216f16d7c88"), 1));
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
	
	public String getDetailNameFromUrl(String detailType, String productId) {
		String url = "http://192.168.0.100:8000/v2/" + detailType + "/" + productId;
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
		String url = "http://192.168.0.100:8000/v2/" + detailType + "/" + productId;
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
}