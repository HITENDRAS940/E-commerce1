package com.hitendra.ecommerce.service;

import com.hitendra.ecommerce.exceptions.APIException;
import com.hitendra.ecommerce.exceptions.ResourceNotFoundException;
import com.hitendra.ecommerce.model.Category;
import com.hitendra.ecommerce.payload.CategoryDTO;
import com.hitendra.ecommerce.payload.CategoryResponse;
import com.hitendra.ecommerce.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImplementation implements CategoryService{

    private final CategoryRepository categoryRepository;

    private final ModelMapper modelMapper;

    public CategoryServiceImplementation(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

        List<Category> categories =  categoryPage.getContent();
        if(categories.isEmpty())
            throw new APIException("No categories present at the moment.");

        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category existingCategory = categoryRepository
                .findCategoryByCategoryName(
                        categoryDTO.getCategoryName()
                );

        if(existingCategory!=null)
            throw new APIException(
                    "Category with name " + categoryDTO.getCategoryName() + " already exists"
            );

        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }


    @Override
    public CategoryDTO deleteCategory(
            Long categoryID
    ) {
        Category category = categoryRepository
                .findById(categoryID)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category", "categoryID", categoryID)
                );
        categoryRepository
                .deleteById(categoryID);
        return modelMapper.map(category, CategoryDTO.class);

    }

    @Override
    public CategoryDTO updateCategory(
            CategoryDTO categoryDTO,
            Long categoryID
    ) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        categoryRepository
                .findById(categoryID)
                .orElseThrow(() ->
                        new ResourceNotFoundException("category", "categoryID", categoryID)
                );

        Category sameNameCategory = categoryRepository
                .findCategoryByCategoryName(categoryDTO.getCategoryName());

        if(sameNameCategory!=null) {
            throw new APIException("Category Already Exists");
        }

        category.setCategoryId(categoryID);

        Category savedCategory =  categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }
}
