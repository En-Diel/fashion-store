package com.endiel.fashion_store_backend.dto

import com.endiel.fashion_store_backend.entity.Category

fun Category.toResponse(): CategoryResponse{
    return CategoryResponse(
        id = requireNotNull(id),
        name = name
    )
}