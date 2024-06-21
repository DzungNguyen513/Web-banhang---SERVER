package com.project.web_banhang.Service;

import com.project.web_banhang.DTOS.CategoryDTO;
import com.project.web_banhang.Model.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO);

    Category getCategoryById(long id);

    List<Category> getAllCategories();

    Category updateCategory(long id, CategoryDTO categoryDTO);

    void deleteCategory(long id);
}
