package com.endiel.fashion_store_backend.dto

import java.math.BigDecimal

data class ProductRequest (
    val name: String,
    val description: String?,
    val price: BigDecimal,
    val imageUrl: String?,
    val stockQuantity: Int,
    val categoryId: Long
)