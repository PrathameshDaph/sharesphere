package com.sharesphere.service.impl;

import com.sharesphere.dto.response.CategoryResponse;
import com.sharesphere.entity.Category;
import com.sharesphere.exception.ResourceNotFoundException;
import com.sharesphere.repository.CategoryRepository;
import com.sharesphere.repository.ItemRepository;
import com.sharesphere.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        return toResponse(categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id)));
    }

    @Override
    public CategoryResponse createCategory(String name, String icon, String description) {
        Category cat = Category.builder().name(name).icon(icon).description(description).build();
        return toResponse(categoryRepository.save(cat));
    }

    private CategoryResponse toResponse(Category c) {
        long count = c.getId() != null ? itemRepository.countByCategoryId(c.getId()) : 0;
        return CategoryResponse.builder()
                .id(c.getId()).name(c.getName()).icon(c.getIcon()).description(c.getDescription())
                .itemCount((int) count)
                .build();
    }
}
