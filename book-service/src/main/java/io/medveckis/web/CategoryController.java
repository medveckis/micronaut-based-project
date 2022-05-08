package io.medveckis.web;

import io.medveckis.service.CategoryService;
import io.medveckis.web.dto.CategoryData;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.reactivex.rxjava3.core.Flowable;
import org.reactivestreams.Publisher;

import java.util.List;
import java.util.stream.Collectors;

@Controller("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Get
    public Publisher<CategoryData> getAllCategories() {
        return Flowable.fromStream(categoryService.getAllCategories()
                .stream()
                .map(category -> new CategoryData(category.getId(), category.getName()))
        );
    }
}