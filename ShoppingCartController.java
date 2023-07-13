package com.das_team.ordering;

import java.util.List;
import java.time.Duration;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiter;


@RestController
public class ShoppingCartController {
	
	private ShoppingCartRepository cartRepository = new ShoppingCartRepository();
	private ShoppingCartDetailRepository cartDetailRepository = new ShoppingCartDetailRepository();
	
	RateLimiterConfig config = RateLimiterConfig.custom()
            .limitRefreshPeriod(Duration.ofSeconds(1)) //period for refreshing the rate limiter permissions
            .limitForPeriod(10) // number of permissions available per refresh period
            .timeoutDuration(Duration.ofMillis(1)) // The duration after which the acquirePermission() call times out
            .build();
	
	// Create registry
	RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.of(config);

	// Use registry
	RateLimiter rateLimiter = rateLimiterRegistry.rateLimiter("myRateLimiter", config);
	
	@GetMapping ("rateLimiterTest")
	public void testRateLimiter() {
		for (int i = 0; i < 50; i++) {
	        boolean permissionGranted = rateLimiter.acquirePermission();
	        if(permissionGranted) {	
	        	System.out.println("Permission granted. Count: " + i);
	        }
	        else {
	        	System.out.println("Permission exceeded. Count: " + i);
	        }
		}
	}
	
	@GetMapping ("nestedLoop")
	public ResponseEntity<String> nestedLoop() {
		for (int i = 0; i < 50; i++) {
			for (int y = 0; y < 50; y++) {
				
			}
		}
		System.out.println("Loop closed");
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping ("wait20seconds")
	public ResponseEntity<String> waitForSeconds(){
		try {
			Thread.sleep(20000);
			System.out.println("waited 20 seconds");
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@Operation(summary = "Returns all ShoppingCarts",
			responses = { 
				    @ApiResponse(responseCode="200", description = "Successfully retrieved all ShoppingCarts"),
				    @ApiResponse(responseCode="404", description = "ShoppingCarts could not be found"),
				    @ApiResponse(responseCode="429", description = "Too many Requests"),
				    @ApiResponse(responseCode="500", description = "Internal Server Error")   
				}
			)
    @GetMapping ("carts")
    public ResponseEntity<List<ShoppingCart>> getAllCarts() {
		if(rateLimiter.acquirePermission()) {
			System.out.println("Permission granted");
			return new ResponseEntity<>(cartRepository.getAllCarts(), HttpStatus.OK);
		}
		else {
			System.out.println("Permission exceeded");
			return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
		}	
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
			shoppingCartDetail.setDetailName(cartDetailRepository.getDetailInfoFromUrl(shoppingCartDetail.getDetailType(), shoppingCartDetail.getDetailId()).get("name"));
			shoppingCartDetail.setUnitprice(Float.parseFloat(cartDetailRepository.getDetailInfoFromUrl(shoppingCartDetail.getDetailType(), shoppingCartDetail.getDetailId()).get("price")));
			cartDetailRepository.addShoppingCartDetail(shoppingCartDetail);
			//Update cartDetailsList
			cartRepository.getCartById(cartId).setCartDetails(cartDetailRepository.getCartDetailsByCartId(cartId));
			//Recalculate TotalSum
			cartRepository.getCartById(cartId).setTotalSum(cartDetailRepository.getCartDetailsSum(cartId));
			return new ResponseEntity<>(shoppingCartDetail, HttpStatus.CREATED);
		}
		else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    
	}
	
	
	@Operation(summary = "Removes a Detail from a ShoppingCart",
			responses = { 
				    @ApiResponse(responseCode="200", description = "ShoppingCartDetail removed succesfully"),
				    @ApiResponse(responseCode="400", description = "Invalid input provided"),
				    @ApiResponse(responseCode="404", description = "Specified ShoppingCart or Product in Cart does not exist"),
				    @ApiResponse(responseCode="500", description = "Internal Server Error")
				})
	@DeleteMapping("/carts/{cartId}/details/{detailId}")
	public ResponseEntity<Order> removeShoppingCartDetail(@PathVariable int cartId, @PathVariable String detailId) {
		if(cartRepository.getCartById(cartId) != null) {
			cartDetailRepository.removeShoppingCartDetail(cartId, detailId);
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
