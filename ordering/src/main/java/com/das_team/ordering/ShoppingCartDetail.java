package com.das_team.ordering;

public class ShoppingCartDetail {
	
	private int cartId;
	private String detailId;
	private String detailName;
	private String detailType;
	private String unit;
	private float unitprice;
	private int quantity;
	
	public ShoppingCartDetail(int cartId, String detailId, String detailName, String detailType, String unit, float unitprice, int quantity) {
		this.cartId = cartId;
		this.detailId = detailId;
		this.detailName = detailName;
		this.detailType = detailType;
		this.unit = unit;
		this.unitprice = unitprice;
		this.quantity = quantity;
	}
	

	//Default Constructor needed for @PostMappings
	public ShoppingCartDetail() {
		
	}
	
	public String getDetailId() {
		return detailId;
	}

	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}

	public String getDetailName() {
		return detailName;
	}

	public void setDetailName(String detailName) {
		this.detailName = detailName;
	}

	public String getDetailType() {
		return detailType;
	}

	public void setDetailType(String detailType) {
		this.detailType = detailType;
	}

	public int getCartId() {
		return cartId;
	}

	public void setCartId(int cartId) {
		this.cartId = cartId;
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
