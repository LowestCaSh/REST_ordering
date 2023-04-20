package com.das_team.ordering;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;


@RestController
public class ShoppingCartController {
	
	private ShoppingCartRepository cartRepository = new ShoppingCartRepository();
	
    @GetMapping ("carts")
    public List<ShoppingCart> getAllCarts() {
        return cartRepository.getAllCarts();
    }
    
    @GetMapping ("carts/ids")
    public List<Integer> getAllCartIds() {
        return cartRepository.getAllCartIDs();
    }
    
    @GetMapping("/carts/{id}")
    public ResponseEntity<ShoppingCart> getCartById(@PathVariable int id) {
        ShoppingCart cart = cartRepository.getCartById(id);
        if (cart == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(cart, HttpStatus.OK);
        }
    }
    
    @GetMapping ("carts/last_updated")
    public List<String> getAllLastUpdated() {
        return cartRepository.getAllCartLastUpdated();
    }
}
