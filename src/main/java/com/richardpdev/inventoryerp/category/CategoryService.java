package com.richardpdev.inventoryerp.category;

import com.richardpdev.inventoryerp.category.dto.CategoryResponse;
import com.richardpdev.inventoryerp.category.dto.CreateCategoryRequest;
import com.richardpdev.inventoryerp.category.dto.UpdateCategoryRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository repo;

    public CategoryService(CategoryRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public CategoryResponse create(CreateCategoryRequest req) {
        if (repo.existsByNameIgnoreCase(req.name())) {
            throw new IllegalArgumentException("Category name already exists");
        }
        Category saved = repo.save(new Category(req.name().trim()));
        return new CategoryResponse(saved.getId(), saved.getName());
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> list() {
        return repo.findAll().stream()
                .map(c -> new CategoryResponse(c.getId(), c.getName()))
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse getById(Long id) {
        Category c = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        return new CategoryResponse(c.getId(), c.getName());
    }

    @Transactional
    public CategoryResponse update(Long id, UpdateCategoryRequest req) {
        Category c = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        String newName = req.name().trim();
        if (!c.getName().equalsIgnoreCase(newName) && repo.existsByNameIgnoreCase(newName)) {
            throw new IllegalArgumentException("Category name already exists");
        }

        c.setName(newName);
        return new CategoryResponse(c.getId(), c.getName());
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Category not found");
        }
        repo.deleteById(id);
    }
}