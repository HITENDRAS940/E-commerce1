package com.hitendra.ecommerce.service;

import com.hitendra.ecommerce.payload.CategoryDTO;
import com.hitendra.ecommerce.payload.CategoryResponse;


public interface CategoryService {
    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO deleteCategory(Long categoryID);

    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryID);
}
