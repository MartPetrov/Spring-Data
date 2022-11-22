package com.example.bookshop.services.categories;

import com.example.bookshop.domain.entities.Category;
import com.example.bookshop.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void seedCategory(List<Category> categoryList) {
        this.categoryRepository.saveAll(categoryList);
    }

    @Override
    public boolean isDataSeeded() {
        return this.categoryRepository.count() > 0;
    }

    @Override
    public Set<Category> getRandomCategories() {
        final long count = this.categoryRepository.count();
        if (count != 0) {
            final Long randomId = new Random().nextLong(1L, count);

           return Set.of(this.categoryRepository.findById(randomId).orElseThrow(NoSuchElementException::new));
        }

        throw new RuntimeException();
    }


}
