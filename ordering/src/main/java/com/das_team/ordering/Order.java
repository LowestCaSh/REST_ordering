package com.das_team.ordering;

import java.util.List;

public class Order {
	
    private int orderId;
    private int customerId;
    private String customerName;
    private String orderDate;
    private String street;
    private String postalcode;
    private String city;
    private String country;
    private String status;
    private List<OrderDetail> orderDetails;
    private float totalSum;

    public Order(int orderId, int customerId, String customerName, String orderDate, String street, String postalcode, String city, String country, String status, List<OrderDetail> orderDetails, float totalSum) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.street = street;
        this.postalcode = postalcode;
        this.city = city;
        this.country = country;
        this.status = status;
        this.orderDetails = orderDetails;
        this.totalSum = totalSum;
        
    }
    
  //Default Constructor needed for @PostMappings
  	public Order() {
  		
  	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getPostalcode() {
		return postalcode;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public List<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public float getTotalSum() {
		return totalSum;
	}

	public void setTotalSum(float totalSum) {
		this.totalSum = totalSum;
	}	
}
