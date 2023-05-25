package com.das_team.ordering;

public class OrderDetail {
	
	private int orderId;
	private String detailId;
	private String detailName;
	private String detailType;
	private String unit;
	private float unitprice;
	private int quantity;
	
	public OrderDetail(int orderId, String detailId, String detailName, String detailType, String unit, float unitprice, int quantity) {
		this.orderId = orderId;
		this.detailId = detailId;
		this.detailName = detailName;
		this.detailType = detailType;
		this.unit = unit;
		this.unitprice = unitprice;
		this.quantity = quantity;
	}
	
	//Default Constructor needed for @PostMappings
	public OrderDetail() {
		
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
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
