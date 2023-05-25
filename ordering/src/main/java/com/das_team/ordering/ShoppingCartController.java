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
				    @ApiResponse(responseCode="201", description = "ShoppingCartDetail added successfully, Id changed according to chosen PathVariable"),
				    @ApiResponse(responseCode="400", description = "Invalid input provided"),
				    @ApiResponse(responseCode="404", description = "Specified ShoppingCart does not exist"),
				    @ApiResponse(responseCode="500", description = "Internal Server Error")
				})
	@PostMapping("/carts/{cartId}/details")
	public ResponseEntity<ShoppingCartDetail> addShoppingCartDetail(@PathVariable int cartId, @RequestBody ShoppingCartDetail shoppingCartDetail) {
		if(cartRepository.getCartById(cartId) != null) {
			shoppingCartDetail.setCartId(cartId);
			shoppingCartDetail.setDetailName(cartDetailRepository.getDetailNameFromUrl(shoppingCartDetail.getDetailType(), shoppingCartDetail.getDetailId()));
			shoppingCartDetail.setUnitprice(cartDetailRepository.getDetailPriceFromUrl(shoppingCartDetail.getDetailType(), shoppingCartDetail.getDetailId()));
			cartDetailRepository.addShoppingCartDetail(shoppingCartDetail);
			//Update cartDetailsList
			cartRepository.getCartById(cartId).setCartDetails(cartDetailRepository.getCartDetailsByCartId(cartId));
			//Recalculate TotalSum
			cartRepository.getCartById(cartId).setTotalSum(cartDetailRepository.getCartDetailsSum(cartId));
			return new ResponseEntity<>(shoppingCartDetail, HttpStatus.CREATED);
		}
		else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    
	}
	
	
	@Operation(summary = "Removes a Product from a ShoppingCart",
			responses = { 
				    @ApiResponse(responseCode="200", description = "ShoppingCartDetail removed succesfully"),
				    @ApiResponse(responseCode="400", description = "Invalid input provided"),
				    @ApiResponse(responseCode="404", description = "Specified ShoppingCart or Product in Cart does not exist"),
				    @ApiResponse(responseCode="500", description = "Internal Server Error")
				})
	@DeleteMapping("/carts/{cartId}/details/{productId}")
	public ResponseEntity<Order> removeShoppingCartDetail(@PathVariable int cartId, @PathVariable String productId) {
		if(cartRepository.getCartById(cartId) != null) {
			cartDetailRepository.removeShoppingCartDetail(cartId, productId);
			//Update cartDetailsList
			cartRepository.getCartById(cartId).setCartDetails(cartDetailRepository.getCartDetailsByCartId(cartId));
			//Recalculate TotalSum
			cartRepository.getCartById(cartId).setTotalSum(cartDetailRepository.getCartDetailsSum(cartId));
			return new ResponseEntity<>(HttpStatus.OK);
		}
		else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@Operation(summary = "Creates a new ShoppingCart",
			responses = { 
				    @ApiResponse(responseCode="201", description = "ShoppingCart created successfully, TotalSum calculated automatically, Id of ShoppingCart set automatically"),
				    @ApiResponse(responseCode="400", description = "Invalid input provided"),
				    @ApiResponse(responseCode="500", description = "Internal Server Error")
				})
	@PostMapping("/carts")
	public ResponseEntity<ShoppingCart> createShoppingCart(@RequestBody ShoppingCart cart) {
	    cartRepository.addShoppingCart(cart);
	    return new ResponseEntity<>(cart, HttpStatus.CREATED);
	}
}
