package com.endiel.fashion_store_backend.service

import com.endiel.fashion_store_backend.CategoryRepository
import com.endiel.fashion_store_backend.entity.Category
import org.springframework.stereotype.Service

// Handles category operations
@Service
class CategoryService(private val categoryRepository: CategoryRepository) {

    fun getAllCategories(): List<Category> {
        return categoryRepository.findAll()
    }

    fun getCategoryById(id: Long): Category? {
        return categoryRepository.findById(id).orElse(null)
    }

    fun createCategory(category: Category): Category {
        return categoryRepository.save(category)
    }

    fun deleteCategory(id: Long) {
        categoryRepository.deleteById(id)
    }

}