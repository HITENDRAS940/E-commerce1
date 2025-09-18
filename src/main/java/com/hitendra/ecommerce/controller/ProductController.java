package com.hitendra.ecommerce.controller;

import com.hitendra.ecommerce.config.AppConstants;
import com.hitendra.ecommerce.model.Product;
import com.hitendra.ecommerce.payload.ProductDTO;
import com.hitendra.ecommerce.payload.ProductResponse;
import com.hitendra.ecommerce.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            ) Integer pageSize,
            @RequestParam(
                    name = "sortBy",
                    defaultValue = AppConstants.SORT_PRODUCTS_BY,
                    required = false
            ) String sortBy,
            @RequestParam(
                    name = "sortOrder",
                    defaultValue = AppConstants.SORT_DIRECTION,
                    required = false
            ) String sortOrder
    ) {
        return new ResponseEntity<>(
                productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder),
                HttpStatus.OK
        );
    }

    @PostMapping("admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(
            @RequestBody Product product,
            @PathVariable("categoryId") Long categoryId
    ) {
        ProductDTO productDTO = productService.addProduct(product, categoryId);
        return new ResponseEntity<>(productDTO ,HttpStatus.CREATED);
    }

    @GetMapping("public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductByCategoryId(
            @PathVariable("categoryId") Long categoryId,
            @RequestParam(
                    name = "pageNumber",
                    defaultValue = AppConstants.PAGE_NUMBER,
                    required = false
            ) Integer pageNumber,
            @RequestParam(
                    name = "pageSize",
                    defaultValue = AppConstants.PAGE_SIZE,
                    required = false
            ) Integer pageSize,
            @RequestParam(
                    name = "sortBy",
                    defaultValue = AppConstants.SORT_PRODUCTS_BY,
                    required = false
            ) String sortBy,
            @RequestParam(
                    name = "sortOrder",
                    defaultValue = AppConstants.SORT_DIRECTION,
                    required = false
            ) String sortOrder

    ) {
        return new ResponseEntity<>(
                productService.getProductsByCategories(categoryId, pageNumber, pageSize, sortBy, sortOrder),
                HttpStatus.FOUND
        );
    }

    @GetMapping("public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(
            @PathVariable("keyword") String keyword,
            @RequestParam(
                    name = "pageNumber",
                    defaultValue = AppConstants.PAGE_NUMBER,
                    required = false
            ) Integer pageNumber,
            @RequestParam(
                    name = "pageSize",
                    defaultValue = AppConstants.PAGE_SIZE,
                    required = false
            ) Integer pageSize,
            @RequestParam(
                    name = "sortBy",
                    defaultValue = AppConstants.SORT_PRODUCTS_BY,
                    required = false
            ) String sortBy,
            @RequestParam(
                    name = "sortOrder",
                    defaultValue = AppConstants.SORT_DIRECTION,
                    required = false
            ) String sortOrder

    ) {
        return new ResponseEntity<>(
                productService.getProductsByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder),
                HttpStatus.OK
        );
    }
}
