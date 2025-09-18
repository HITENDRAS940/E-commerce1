package com.hitendra.ecommerce.controller;

import com.hitendra.ecommerce.config.AppConstants;
import com.hitendra.ecommerce.model.Product;
import com.hitendra.ecommerce.payload.ProductDTO;
import com.hitendra.ecommerce.payload.ProductResponse;
import com.hitendra.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
            @Valid @RequestBody ProductDTO productDTO,
            @PathVariable("categoryId") Long categoryId
    ) {
        return new ResponseEntity<>(productService.addProduct(productDTO, categoryId) ,HttpStatus.CREATED);
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

    @PutMapping("admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable("productId") Long productId,
            @RequestBody ProductDTO productDTO
    ) {
        return new ResponseEntity<>(productService.updateProduct(productId, productDTO), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("admin/product/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(
            @PathVariable("productId") Long productId
    ) {
        return new ResponseEntity<>(productService.deleteProduct(productId), HttpStatus.OK);
    }

    @PutMapping("admin/product/{productId}/image")
    public ResponseEntity<ProductDTO> updateImage(
            @PathVariable("productId") Long productId,
            @RequestParam("image") MultipartFile image
    ) throws IOException {
        return new ResponseEntity<>(productService.updateImage(productId, image), HttpStatus.ACCEPTED);
    }
}
