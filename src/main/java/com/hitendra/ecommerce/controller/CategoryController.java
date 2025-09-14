package com.hitendra.ecommerce.controller;

import com.hitendra.ecommerce.config.AppConstants;
import com.hitendra.ecommerce.payload.CategoryDTO;
import com.hitendra.ecommerce.payload.CategoryResponse;
import com.hitendra.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(
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
                    defaultValue = AppConstants.SORT_CATEGORIES_BY,
                    required = false
            ) String sortBy,
            @RequestParam(
                    name = "sortOrder",
                    defaultValue = AppConstants.SORT_DIRECTION,
                    required = false
            ) String sortOrder

    ) {
            return new ResponseEntity<>(
                    categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder),
                    HttpStatus.OK
            );
    }

    @PostMapping("public/categories")
    public ResponseEntity<CategoryDTO> createCategory(
            @Valid @RequestBody CategoryDTO categoryDTO
    ) {
        return new ResponseEntity<>(
                categoryService.createCategory(categoryDTO),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("admin/categories/{categoryID}")
    public ResponseEntity<CategoryDTO> deleteCategory(
            @PathVariable("categoryID") Long categoryID
    ) {
            return new ResponseEntity<>(
                    categoryService.deleteCategory(categoryID),
                    HttpStatus.OK
            );
    }

    @PutMapping("admin/categories/{categoryID}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @RequestBody CategoryDTO categoryDTO,
            @PathVariable("categoryID") Long categoryID
    ) {
            return new ResponseEntity<>(
                    categoryService.updateCategory(categoryDTO, categoryID), HttpStatus.OK
            );
    }
}