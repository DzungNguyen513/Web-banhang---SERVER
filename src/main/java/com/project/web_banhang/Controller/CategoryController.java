package com.project.web_banhang.Controller;

import com.project.web_banhang.Components.LocalizationUtils;
import com.project.web_banhang.DTOS.CategoryDTO;
import com.project.web_banhang.Model.Category;
import com.project.web_banhang.Responses.CategoryResponse;
import com.project.web_banhang.Responses.UpdateCategoryResponse;
import com.project.web_banhang.Service.CategoryService;
import com.project.web_banhang.Service.ICategoryService;
import com.project.web_banhang.Utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final LocalizationUtils localizationUtils;


    @GetMapping("")
    public ResponseEntity<?> getAllCategories(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    @PostMapping("")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult result) {
            CategoryResponse categoryResponse = new CategoryResponse();
            if(result.hasErrors()){
                List<String> errMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                categoryResponse.setMessage(localizationUtils.getLocalizeMessage(MessageKeys.INSERT_CATEGORY_FAILED));
                categoryResponse.setErrors(errMessages);
                return ResponseEntity.badRequest().body(categoryResponse);
            }

        Category category = categoryService.createCategory(categoryDTO);
        categoryResponse.setCategory(category);
        return ResponseEntity.ok(categoryResponse);
    }


    @PutMapping("/{id}")
    public ResponseEntity<UpdateCategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryDTO categoryDTO
    ) {
        UpdateCategoryResponse updateCategoryResponse = new UpdateCategoryResponse();
        categoryService.updateCategory(id, categoryDTO);
        updateCategoryResponse.setMessage(localizationUtils.getLocalizeMessage(MessageKeys.UPDATE_CATEGORY_SUCCESSFULLY));
        return ResponseEntity.ok(updateCategoryResponse);
    }

    @DeleteMapping("/{}")
    public ResponseEntity<?> deleteCategory(@Valid @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(localizationUtils.getLocalizeMessage(MessageKeys.DELETE_CATEGORY_SUCCESSFULLY));    }


}
