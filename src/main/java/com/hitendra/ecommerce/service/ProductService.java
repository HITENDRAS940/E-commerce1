package com.hitendra.ecommerce.service;

import com.hitendra.ecommerce.model.Product;
import com.hitendra.ecommerce.payload.ProductDTO;
import com.hitendra.ecommerce.payload.ProductResponse;

public interface ProductService {

    ProductDTO addProduct(Product product, Long categoryId);

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse getProductsByCategories(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
}
