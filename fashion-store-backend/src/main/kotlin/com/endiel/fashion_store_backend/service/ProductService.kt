package com.endiel.fashion_store_backend.service

import com.endiel.fashion_store_backend.entity.Product
import com.endiel.fashion_store_backend.repository.CategoryRepository
import com.endiel.fashion_store_backend.repository.ProductRepository
import org.apache.juli.logging.Log
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.server.ResponseStatusException
import kotlin.jvm.optionals.getOrNull

// Handles Product operations
@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
) {
    // FETCH ALL PRODUCTS
    fun getAllProducts(): List<Product> {
        return productRepository.findAll()
    }
    // FETCH PRODUCT BY ID
    fun getProductById(id: Long): Product {
        return productRepository.findById(id).orElseThrow {
            ResponseStatusException(
                HttpStatus.NOT_FOUND, "Product Not Found"
            )
        }
    }
    // CREATE PRODUCT
    fun createProduct(product: Product): Product {
        val categoryId = product.category?.id ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST, "Category ID is required"
        )

        val category = categoryRepository.findById(categoryId).orElseThrow {
            ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Category does not exist"
            )
        }
        // Reset ID
        product.id = null
        // Attach managed category
        product.category = category
        return productRepository.save(product)
    }
    // UPDATE PRODUCT
    fun updateProduct(id: Long, updatedProduct: Product): Product {
        // Find the existing product using its ID
        val existingProduct = productRepository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found")
        }

        val categoryId = updatedProduct.category?.id ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST, "Category ID is required"
        )

        val category = categoryRepository.findById(categoryId).orElseThrow {
            ResponseStatusException(HttpStatus.BAD_REQUEST, "Category does not exist")
        }

        // updates the product's fields, while preserving its existing ID
        existingProduct.name = updatedProduct.name
        existingProduct.description = updatedProduct.description
        existingProduct.price = updatedProduct.price
        existingProduct.imageUrl = updatedProduct.imageUrl
        existingProduct.stockQuantity = updatedProduct.stockQuantity
        existingProduct.category = category

        return productRepository.save(existingProduct)
    }
    // DELETE PRODUCT
    fun deleteProduct(id: Long) {
        if (!productRepository.existsById(id)) {
            throw ResponseStatusException(
                HttpStatus.NOT_FOUND, "Product not found"
            )
        }
        productRepository.deleteById(id)
    }

}