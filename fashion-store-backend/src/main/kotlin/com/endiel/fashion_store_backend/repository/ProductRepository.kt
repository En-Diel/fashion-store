package com.endiel.fashion_store_backend.repository

import com.endiel.fashion_store_backend.entity.Product
import org.springframework.data.jpa.repository.JpaRepository

// Handles database operations
interface ProductRepository: JpaRepository<Product, Long> {

    fun findByCategoryId(categoryId: Long): List<Product>
    fun findByNameContainingIgnoreCase(name: String): List<Product>
    fun findByCategoryIdAndNameContainingIgnoreCase(categoryId: Long, name: String): List<Product>
}