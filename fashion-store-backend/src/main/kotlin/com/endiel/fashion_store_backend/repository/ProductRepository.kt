package com.endiel.fashion_store_backend.repository

import com.endiel.fashion_store_backend.entity.Product
import org.springframework.data.jpa.repository.JpaRepository

// Handles database operations
interface ProductRepository: JpaRepository<Product, Long>