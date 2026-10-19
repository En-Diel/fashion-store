package com.endiel.fashion_store_backend.repository

import com.endiel.fashion_store_backend.entity.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

// Handles database operations
interface ProductRepository: JpaRepository<Product, Long> {

    fun findByCategoryId(categoryId: Long, pageable: Pageable): Page<Product>
    fun findByNameContainingIgnoreCase(name: String, pageable: Pageable): Page<Product>
    fun findByCategoryIdAndNameContainingIgnoreCase(categoryId: Long, name: String, pageable: Pageable): Page<Product>
}