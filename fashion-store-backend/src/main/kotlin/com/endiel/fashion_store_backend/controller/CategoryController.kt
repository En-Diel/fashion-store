package com.endiel.fashion_store_backend.controller

import com.endiel.fashion_store_backend.entity.Category
import com.endiel.fashion_store_backend.service.CategoryService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

// Exposes REST API End points (Exposing operations through REST API)
@RestController
@RequestMapping("api/v1/categories")
class CategoryController(private val categoryService: CategoryService) {

    @GetMapping
    fun getAllCategories(): List<Category> {
        return categoryService.getAllCategories()
    }

    @GetMapping("/{id}")
    fun getCategoryById(@PathVariable id: Long): Category? {
        return categoryService.getCategoryById(id)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createCategory(@RequestBody category: Category): Category {
        return categoryService.createCategory(category)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCategory(@PathVariable id: Long) {
        categoryService.deleteCategory(id)
    }

}