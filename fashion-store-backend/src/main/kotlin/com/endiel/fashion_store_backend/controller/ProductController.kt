package com.endiel.fashion_store_backend.controller

import com.endiel.fashion_store_backend.entity.Product
import com.endiel.fashion_store_backend.service.ProductService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

// Exposes REST API End points
@RestController
@RequestMapping("/api/v1/products")
class ProductController(private val productService: ProductService) {

    @GetMapping
    fun getAllProducts(): List<Product> {
        return productService.getAllProducts()
    }

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Long): Product {
        return productService.getProductById(id)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createProduct(@RequestBody product: Product): Product {
        return productService.createProduct(product)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProduct(@PathVariable id: Long) {
        productService.deleteProduct(id)
    }

}