package com.endiel.fashion_store_backend.service

import com.endiel.fashion_store_backend.repository.CategoryRepository
import com.endiel.fashion_store_backend.entity.Category
import org.springframework.stereotype.Service

// Handles category operations
@Service
class CategoryService(private val categoryRepository: CategoryRepository) {

    // FETCH ALL CATEGORIES
    fun getAllCategories(): List<Category> {
        return categoryRepository.findAll()
    }
    // FETCH CATEGORY BY ID
    fun getCategoryById(id: Long): Category? {
        return categoryRepository.findById(id).orElse(null)
    }
    // CREATE CATEGORY
    fun createCategory(category: Category): Category {
        return categoryRepository.save(category)
    }
    // DELETE CATEGORY
    fun deleteCategory(id: Long) {
        categoryRepository.deleteById(id)
    }

}