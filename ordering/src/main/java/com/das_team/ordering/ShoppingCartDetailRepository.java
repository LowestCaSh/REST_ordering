package com.das_team.ordering;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartDetailRepository {
	
	private List<ShoppingCartDetail> cartDetails = new ArrayList<>();
	
	public ShoppingCartDetailRepository() {
		cartDetails.add(new ShoppingCartDetail(1, 5, "Kühlschrank Triple Deluxe", "Stück", 3000.0f, 2));
		cartDetails.add(new ShoppingCartDetail(1, 3, "Waschmaschine 2679A", "Stück", 399.99f, 1));
		cartDetails.add(new ShoppingCartDetail(1, 2, "Waschmaschine reparieren", "Stunde", 50.0f, 3));
		cartDetails.add(new ShoppingCartDetail(2, 10, "Mikrowelle 300W", "Stück", 59.99f, 7));
		cartDetails.add(new ShoppingCartDetail(2, 3, "Spülmaschine Megasauber", "Stück", 250.50f, 3));
		cartDetails.add(new ShoppingCartDetail(3, 9, "Küche putzen", "Stunde", 25.0f, 2));
		cartDetails.add(new ShoppingCartDetail(4, 7, "Herd Superhot", "Stück", 560.99f, 3));
		cartDetails.add(new ShoppingCartDetail(4, 8, "Toaster für Großfamilie", "Stück", 15.99f, 20));
		cartDetails.add(new ShoppingCartDetail(4, 11, "Jährliche Wartung", null, 0.0f, 1));
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
		float shoppingCartDetailsSum = 0f;
		for (ShoppingCartDetail cartDetail : cartDetails) {
	        if (cartDetail.getCartId() == cartId) {
	            shoppingCartDetailsSum += cartDetail.getUnitprice() * cartDetail.getQuantity();
	        }
	    }
		return shoppingCartDetailsSum;
	}
}