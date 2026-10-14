package com.endiel.fashion_store_backend.service

import com.endiel.fashion_store_backend.dto.ProductRequest
import com.endiel.fashion_store_backend.dto.ProductResponse
import com.endiel.fashion_store_backend.dto.toResponse
import com.endiel.fashion_store_backend.entity.Product
import com.endiel.fashion_store_backend.repository.CategoryRepository
import com.endiel.fashion_store_backend.repository.ProductRepository
import org.apache.juli.logging.Log
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.server.ResponseStatusException
import kotlin.jvm.optionals.getOrNull

// Handles Product operations
@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
) {
    // FETCH ALL PRODUCTS (We use @Transactional so that the entity to DTO mapping happens while
    // database session is active, coz product category uses Lazy loading)
    @Transactional(readOnly = true)
    fun getAllProducts(): List<ProductResponse> {
        return productRepository.findAll().map { it.toResponse() }
    }

    // FETCH PRODUCT BY ID
    @Transactional(readOnly = true)
    fun getProductById(id: Long): ProductResponse {
        val product = productRepository.findById(id).orElseThrow {
            ResponseStatusException(
                HttpStatus.NOT_FOUND, "Product Not Found"
            )
        }
        return product.toResponse()
    }

    // CREATE PRODUCT
    @Transactional
    fun createProduct(request: ProductRequest): ProductResponse {

        val category = categoryRepository.findById(request.categoryId).orElseThrow {
            ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Category does not exist"
            )
        }

        val product = Product(
            name = request.name,
            description = request.description,
            price = request.price,
            imageUrl = request.imageUrl,
            stockQuantity = request.stockQuantity,
            category = category
        )

        return productRepository.save(product).toResponse()
    }

    // UPDATE PRODUCT
    @Transactional
    fun updateProduct(id: Long, request: ProductRequest): ProductResponse {
        // Find the existing product using its ID
        val existingProduct = productRepository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found")
        }

        val category = categoryRepository.findById(request.categoryId).orElseThrow {
            ResponseStatusException(HttpStatus.BAD_REQUEST, "Category does not exist")
        }

        // updates the product's fields, while preserving its existing ID
        existingProduct.name = request.name
        existingProduct.description = request.description
        existingProduct.price = request.price
        existingProduct.imageUrl = request.imageUrl
        existingProduct.stockQuantity = request.stockQuantity
        existingProduct.category = category

        return productRepository.save(existingProduct).toResponse()
    }

    // DELETE PRODUCT
    @Transactional
    fun deleteProduct(id: Long) {
        if (!productRepository.existsById(id)) {
            throw ResponseStatusException(
                HttpStatus.NOT_FOUND, "Product not found"
            )
        }
        productRepository.deleteById(id)
    }

}