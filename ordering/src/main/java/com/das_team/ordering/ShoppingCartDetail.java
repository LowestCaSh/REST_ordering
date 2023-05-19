package com.das_team.ordering;

public class ShoppingCartDetail {
	
	private int cartId;
	private String productId;
	private String productName;
	private String unit;
	private float unitprice;
	private int quantity;
	
	public ShoppingCartDetail(int cartId, String productId, String productName, String unit, float unitprice, int quantity) {
		this.cartId = cartId;
		this.productId = productId;
		this.productName = productName;
		this.unit = unit;
		this.unitprice = unitprice;
		this.quantity = quantity;
	}
	
	//Default Constructor needed for @PostMappings
	public ShoppingCartDetail() {
		
	}

	public int getCartId() {
		return cartId;
	}

	public void setCartId(int cartId) {
		this.cartId = cartId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public float getUnitprice() {
		return unitprice;
	}

	public void setUnitprice(float unitprice) {
		this.unitprice = unitprice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}
