package com.das_team.ordering;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
public class ShoppingCartController {
	
	private ShoppingCartRepository cartRepository = new ShoppingCartRepository();
	private ShoppingCartDetailRepository cartDetailRepository = new ShoppingCartDetailRepository();
	
	@Operation(summary = "Returns all ShoppingCarts",
			responses = { 
				    @ApiResponse(responseCode="200", description = "Successfully retrieved all ShoppingCarts"),
				    @ApiResponse(responseCode="404", description = "ShoppingCarts could not be found"),
				    @ApiResponse(responseCode="500", description = "Internal Server Error")
				}
			)
    @GetMapping ("carts")
    public List<ShoppingCart> getAllCarts() {
        return cartRepository.getAllCarts();
    }
    
	@Operation(summary = "Returns the ShoppingCart with the specified Id",
			responses = { 
				    @ApiResponse(responseCode="200", description = "Successfully retrieved specifid ShoppingCart"),
				    @ApiResponse(responseCode="404", description = "Specified ShoppingCart could not be found"),
				    @ApiResponse(responseCode="500", description = "Internal Server Error")
				}
			)
    @GetMapping("carts/{cartId}")
    public ResponseEntity<ShoppingCart> getCartById(@PathVariable int cartId) {
        ShoppingCart cart = cartRepository.getCartById(cartId);
        if (cart == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(cart, HttpStatus.OK);
        }
    }
	
	@Operation(
			summary = "Returns all ShoppingCartDetails from the specified ShoppingCart (cartId)",
			responses = { 
			    @ApiResponse(responseCode="200", description = "Successfully retrieved ShoppingCartDetails from specified ShoppingCart (Empty list possible if cart is empty)"),
			    @ApiResponse(responseCode="404", description = "Specified ShoppingCart could not be found"),
			    @ApiResponse(responseCode="500", description = "Internal Server Error")
			}
		)
    @GetMapping("carts/{cartId}/details")
    public ResponseEntity<List<ShoppingCartDetail>> getCartDetailsByCartId(@PathVariable int cartId) {
		//We need to check if the specified Cart exists
		if(cartRepository.getCartById(cartId) != null) {
	        List<ShoppingCartDetail> cartDetails = cartDetailRepository.getCartDetailsByCartId(cartId);
	        return new ResponseEntity<>(cartDetails, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
	
	@Operation(summary = "Adds a new ShoppingCartDetail for the Specified ShoppingCart (cartId)",
			responses = { 
				    @ApiResponse(responseCode="201", description = "ShoppingCartDetail added successfully"),
				    @ApiResponse(responseCode="400", description = "Invalid input provided"),
				    @ApiResponse(responseCode="404", description = "Specified ShoppingCart does not exist"),
				    @ApiResponse(responseCode="500", description = "Internal Server Error")
				})
	@PostMapping("/carts/{cartId}/details")
	public ResponseEntity<ShoppingCartDetail> addShoppingCartDetail(@PathVariable int cartId, @RequestBody ShoppingCartDetail shoppingCartDetail) {
		if(cartRepository.getCartById(cartId) != null) {
			cartDetailRepository.addShoppingCartDetail(shoppingCartDetail);
			return new ResponseEntity<>(shoppingCartDetail, HttpStatus.CREATED);
		}
		else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    
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
