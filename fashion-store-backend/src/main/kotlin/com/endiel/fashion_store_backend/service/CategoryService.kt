package com.endiel.fashion_store_backend.service

import com.endiel.fashion_store_backend.dto.CategoryRequest
import com.endiel.fashion_store_backend.dto.CategoryResponse
import com.endiel.fashion_store_backend.dto.toResponse
import com.endiel.fashion_store_backend.repository.CategoryRepository
import com.endiel.fashion_store_backend.entity.Category
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

// Handles category operations
@Service
class CategoryService(private val categoryRepository: CategoryRepository) {

    // FETCH ALL CATEGORIES
    @Transactional(readOnly = true)
    fun getAllCategories(): List<CategoryResponse> {
        return categoryRepository.findAll().map { it.toResponse() }
    }
    // FETCH CATEGORY BY ID
    @Transactional(readOnly = true)
    fun getCategoryById(id: Long): CategoryResponse? {

        val category =  categoryRepository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found")
        }

        return category.toResponse()
    }
    // CREATE CATEGORY
    @Transactional
    fun createCategory(request: CategoryRequest): CategoryResponse {

        if(categoryRepository.existsByName(request.name)) {
            throw ResponseStatusException(
                HttpStatus.CONFLICT, "Category already exists"
            )
        }

        val category = Category(name = request.name)

        return  categoryRepository.save(category).toResponse()
    }
    // DELETE CATEGORY
    fun deleteCategory(id: Long) {
        categoryRepository.deleteById(id)
    }

}