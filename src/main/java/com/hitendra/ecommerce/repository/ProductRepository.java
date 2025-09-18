package com.hitendra.ecommerce.repository;

import com.hitendra.ecommerce.model.Category;
import com.hitendra.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> getProductsByCategory(Category category, Pageable pageable);

    Page<Product> getProductsByProductNameLikeIgnoreCase(String productName, Pageable pageable);
}
