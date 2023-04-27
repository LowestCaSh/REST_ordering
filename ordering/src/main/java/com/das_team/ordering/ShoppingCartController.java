package com.das_team.ordering;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;

@RestController
public class ShoppingCartController {
	
	private ShoppingCartRepository cartRepository = new ShoppingCartRepository();
	
	@Operation(summary = "Returns all ShoppingCarts")
    @GetMapping ("carts")
    public List<ShoppingCart> getAllCarts() {
        return cartRepository.getAllCarts();
    }
    
	@Operation(summary = "Returns the ShoppingCart with the specified Id")
    @GetMapping("carts/{id}")
    public ResponseEntity<ShoppingCart> getCartById(@PathVariable int id) {
        ShoppingCart cart = cartRepository.getCartById(id);
        if (cart == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(cart, HttpStatus.OK);
        }
    }
	/*
	@Operation(summary = "Creates a new ShoppingCart")
	@PostMapping("/carts")
	public ResponseEntity<ShoppingCart> createCart(@RequestBody ShoppingCart cart) {
	    cartRepository.addCart(cart);
	    return new ResponseEntity<>(cart, HttpStatus.CREATED);
	}
	*/
}
