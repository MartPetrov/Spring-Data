package com.example.bookshop.services.categories;

import com.example.bookshop.domain.entities.Category;

import java.util.List;

import java.util.Set;

public interface CategoryService {
    void seedCategory(List<Category> categoryList);

    boolean isDataSeeded();


    Set<Category> getRandomCategories();
}
