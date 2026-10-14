package com.endiel.fashion_store_backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal

// Represents Product data in postgreSQL
@Entity
@Table(name = "product")
class Product (

    @field:Id
    @field:GeneratedValue(GenerationType.IDENTITY)
    var id: Long? = null,

    @field:Column(nullable = false, unique = true, length = 150)
    var name: String = "",

    @field:Column(columnDefinition = "TEXT")
    var description: String? = null,

    @field:Column(nullable = false, precision = 10, scale = 2)
    var price: BigDecimal = BigDecimal.ZERO,

    @field:Column(length = 500)
    var imageUrl: String? = null,

    @field:Column(nullable = false)
    var stockQuantity: Int = 0,

    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "category_id", nullable = false)
    var category: Category? = null

)