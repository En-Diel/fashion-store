package com.endiel.fashion_store_backend.repository

import com.endiel.fashion_store_backend.entity.Category
import org.springframework.data.jpa.repository.JpaRepository

// Handles Database operations
interface CategoryRepository : JpaRepository<Category, Long> {
    fun existsByName(name: String): Boolean
}