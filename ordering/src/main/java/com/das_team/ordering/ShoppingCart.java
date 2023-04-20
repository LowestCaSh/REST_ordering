package com.das_team.ordering;

import java.util.List;

public class ShoppingCart {

    private int cartId;
    private int customerId;
    private List<ShoppingCartDetail> cartDetails;
    private float totalSum;
    private String lastUpdated;

    public ShoppingCart(int cartId,int customerId, List<ShoppingCartDetail> cartDetails, float totalSum, String lastUpdated) {
        this.cartId = cartId;
        this.customerId = customerId;
        this.cartDetails = cartDetails;
        this.totalSum = totalSum;
        this.lastUpdated = lastUpdated;
        
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }
    
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public List<ShoppingCartDetail> getCartDetails() {
		return cartDetails;
	}

	public void setCartDetails(List<ShoppingCartDetail> cartDetails) {
		this.cartDetails = cartDetails;
	}

	public float getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(float totalSum) {
        this.totalSum = totalSum;
    }
    
    public String getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
        
}
