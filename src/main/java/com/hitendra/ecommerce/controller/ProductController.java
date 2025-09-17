package com.hitendra.ecommerce.controller;

import com.hitendra.ecommerce.config.AppConstants;
import com.hitendra.ecommerce.model.Product;
import com.hitendra.ecommerce.payload.ProductDTO;
import com.hitendra.ecommerce.payload.ProductResponse;
import com.hitendra.ecommerce.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(
                    name = "pageNumber",
                    defaultValue = AppConstants.PAGE_NUMBER,
                    required = false
            ) Integer pageNumber,
            @RequestParam(
                    name = "pageSize",
                    defaultValue = AppConstants.PAGE_SIZE,
                    required = false
            ) Integer pageSize
    ) {
        return new ResponseEntity<>(productService.getAllProducts(pageNumber, pageSize), HttpStatus.OK);
    }

    @PostMapping("admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(
            @RequestBody Product product,
            @PathVariable("categoryId") Long categoryId
    ) {
        ProductDTO productDTO = productService.addProduct(product, categoryId);
        return new ResponseEntity<>(productDTO ,HttpStatus.CREATED);
    }
}
