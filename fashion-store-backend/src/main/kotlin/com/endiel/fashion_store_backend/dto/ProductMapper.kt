package com.endiel.fashion_store_backend.dto

import com.endiel.fashion_store_backend.entity.Product

// Kotlin extension function(Converts Product Entity to Product Response)
fun Product.toResponse(): ProductResponse {
    return ProductResponse(
        id = requireNotNull(id),
        name = name,
        description = description,
        price = price,
        imageUrl = imageUrl,
        stockQuantity = stockQuantity,
        categoryId = requireNotNull(category?.id),
        categoryName = requireNotNull(category?.name)
    )
}