package io.medveckis.service.impl;

import io.medveckis.model.Category;
import io.medveckis.repository.CategoryRepository;
import io.medveckis.service.CategoryService;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Singleton
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;


    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return StreamSupport.stream(categoryRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }
}
