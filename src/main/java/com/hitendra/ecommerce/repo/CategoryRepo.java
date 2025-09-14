package com.hitendra.ecommerce.repo;

import com.hitendra.ecommerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
    Category findCategoryByCategoryName(String categoryName);
}
