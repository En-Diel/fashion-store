package com.endiel.fashion_store_backend.dto

import java.math.BigDecimal

data class ProductResponse (
    val id: Long,
    val name: String,
    val description: String?,
    val price: BigDecimal,
    val imageUrl: String?,
    val stockQuantity: Int,
    val categoryId: Long,
    val categoryName: String
)