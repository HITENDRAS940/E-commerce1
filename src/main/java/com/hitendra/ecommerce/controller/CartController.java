package com.hitendra.ecommerce.controller;

import com.hitendra.ecommerce.model.Cart;
import com.hitendra.ecommerce.payload.CartDTO;
import com.hitendra.ecommerce.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(
            @PathVariable Long productId,
            @PathVariable Integer quantity
    ) {
        return new ResponseEntity<>(
                cartService.addProductToCart(productId, quantity),
                HttpStatus.CREATED
        );
    }

    @GetMapping("carts")
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        return new ResponseEntity<>(cartService.getAllCarts(), HttpStatus.FOUND);
    }

    @GetMapping("carts/user/cart")
    public ResponseEntity<CartDTO> getUserCart() {
        return new ResponseEntity<>(cartService.getUserCart(), HttpStatus.FOUND);
    }

    @PutMapping("cart/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateUserCart(
            @PathVariable Long productId,
            @PathVariable String operation
    ) {
        CartDTO cartDTO = cartService.updateUserCart(
                productId,
                operation.equalsIgnoreCase("delete") ? -1 : 1
        );
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @DeleteMapping("/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(
        @PathVariable("cartId") Long cartId,
        @PathVariable("productId") Long productId
    ) {
        return new ResponseEntity<>(
                cartService.deleteProductFromCart(cartId, productId),
                HttpStatus.OK
        );
    }

}
