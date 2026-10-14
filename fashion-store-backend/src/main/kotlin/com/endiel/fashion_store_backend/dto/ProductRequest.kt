package com.endiel.fashion_store_backend.dto

import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size
import java.math.BigDecimal

data class ProductRequest (
    @field:NotBlank(message = "Product name is required")
    @field:Size(max = 150, message = "Product name cannot exceed 150 characters")
    val name: String,

    val description: String?,

    @field:NotNull(message = "Product price is required")
    @field:DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    val price: BigDecimal,

    @field:Size(max = 500, message = "Image URL cannot exceed 500 characters")
    val imageUrl: String?,

    @field:PositiveOrZero(message = "Stock quantity cannot be negative")
    val stockQuantity: Int,

    @field:NotNull(message = "Category ID is required")
    val categoryId: Long
)