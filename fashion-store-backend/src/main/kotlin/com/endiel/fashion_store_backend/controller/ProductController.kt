package com.endiel.fashion_store_backend.controller

import com.endiel.fashion_store_backend.dto.ProductRequest
import com.endiel.fashion_store_backend.dto.ProductResponse
import com.endiel.fashion_store_backend.entity.Product
import com.endiel.fashion_store_backend.service.ProductService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

// Exposes REST API End points
@RestController
@RequestMapping("/api/v1/products")
class ProductController(private val productService: ProductService) {

    @GetMapping
    fun getAllProducts(
        @RequestParam(required = false) categoryId: Long?,
        @RequestParam(required = false) search: String?
    ): List<ProductResponse> {
        return productService.getAllProducts(categoryId, search )
    }

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Long): ProductResponse {
        return productService.getProductById(id)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createProduct(@Valid @RequestBody request: ProductRequest): ProductResponse {
        return productService.createProduct(request)
    }

    @PutMapping("/{id}")
    fun updateProduct(
        @PathVariable id: Long, @Valid @RequestBody request: ProductRequest
    ): ProductResponse = productService.updateProduct(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProduct(@PathVariable id: Long) {
        productService.deleteProduct(id)
    }

}