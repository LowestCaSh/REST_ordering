package com.das_team.ordering;

import java.util.List;
import java.util.ArrayList;

public class ShoppingCartRepository{
	
	private List<ShoppingCart> carts;
	ShoppingCartDetailRepository cartDetailRepository = new ShoppingCartDetailRepository();
	
	public ShoppingCartRepository() {
		carts = new ArrayList<>();
		carts.add(new ShoppingCart(1, 3, cartDetailRepository.getCartDetailsByCartId(1), cartDetailRepository.getCartDetailsSum(1), "2024-12-13"));
		carts.add(new ShoppingCart(2, 9, cartDetailRepository.getCartDetailsByCartId(2), cartDetailRepository.getCartDetailsSum(2), "1997-01-01"));
		carts.add(new ShoppingCart(3, 27, cartDetailRepository.getCartDetailsByCartId(3), cartDetailRepository.getCartDetailsSum(3), "2001-06-09"));
		carts.add(new ShoppingCart(4, 14, cartDetailRepository.getCartDetailsByCartId(4), cartDetailRepository.getCartDetailsSum(4), "1994-08-27"));
		carts.add(new ShoppingCart(5, 23, cartDetailRepository.getCartDetailsByCartId(5), cartDetailRepository.getCartDetailsSum(5), "1994-08-27"));
	}
	
	public List<ShoppingCart> getAllCarts() {
        return carts;
    }
	
	public List<Integer> getAllCartIDs() {
		List<Integer> cartIds = new ArrayList<>();
	    for (ShoppingCart cart : carts) {
	        cartIds.add(cart.getCartId());
	    }
	    return cartIds;
    }
	
	public ShoppingCart getCartById(int cartId) {
		for (ShoppingCart cart : carts) {
	        if (cart.getCartId() == cartId) {
	            return cart;
	        }
	    }
	    return null;
    }
	
	public List<String> getAllCartLastUpdated() {
		List<String> cartUpdates = new ArrayList<>();
	    for (ShoppingCart cart : carts) {
	    	cartUpdates.add(cart.getLastUpdated());
	    }
	    return cartUpdates;
    }
	
	
	public void addCart(ShoppingCart cart) {
       carts.add(cart);
    }
	
	/*
    public void deleteCart(int cartId) {
        
    }

    public void updateCart(int cartId, ShoppingCart updatedCart) {
        
    }
	
    */

}