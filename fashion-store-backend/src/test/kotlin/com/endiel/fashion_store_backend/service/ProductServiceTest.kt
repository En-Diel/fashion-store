package com.endiel.fashion_store_backend.service

import com.endiel.fashion_store_backend.dto.ProductRequest
import com.endiel.fashion_store_backend.entity.Category
import com.endiel.fashion_store_backend.entity.Product
import com.endiel.fashion_store_backend.repository.CategoryRepository
import com.endiel.fashion_store_backend.repository.ProductRepository
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal
import java.util.Optional
import kotlin.test.assertEquals

class ProductServiceTest {

    private val productRepository = mock(ProductRepository::class.java)
    private val categoryRepository = mock(CategoryRepository::class.java)

    private val productService = ProductService(
        productRepository,
        categoryRepository
    )
    // GET NONEXISTENT product, 404 Not Found
    @Test
    fun `getProductById throws 404 when product does not exist`() {
        // Arrange
        `when`(productRepository.findById(99999L))
            .thenReturn(Optional.empty())

        // Act and Assert
        val exception = assertThrows(ResponseStatusException::class.java) {
            productService.getProductById(99999L) // L means the number is Long
        }
        // Compares an expected value with an actual value
        assertEquals(HttpStatus.NOT_FOUND, exception.statusCode)
    }
    // CREATE PRODUCT with invalid category, 400 bad request
    @Test
    fun `createProduct throws 404 when category does not exist`() {
        // Arrange
        // Create a product with valid information
        val request = ProductRequest(
            name = "Black T-shirt",
            description = "Cotton T-shirt",
            price = BigDecimal("2500.00"),
            imageUrl = null,
            stockQuantity = 10,
            categoryId = 99999L
        )
        // We create a category ID that doesn't exist
        `when`(categoryRepository.findById(99999L))
            // Mockito simulates the category repository returning an empty list
            .thenReturn(Optional.empty())
        // Act
        val exception = assertThrows(ResponseStatusException::class.java) {
            productService.createProduct(request)
        }
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, exception.statusCode)
    }
    // CREATE A PRODUCT WITH A VALID CATEGORY, product saved successfully
    @Test
    fun `createProduct saves product when category exists` () {
        // Arrange
        val category = Category(
            id = 1L,
            name = "Men's Clothing"
        )

        val request = ProductRequest(
            name = "Black T-Shirt",
            description = "Cotton T-Shirt",
            price = BigDecimal("2500.00"),
            imageUrl = null,
            stockQuantity = 10,
            categoryId = 1L
        )

        `when`(categoryRepository.findById(1L))
            // This tells mockito the category ID 1 exists
            .thenReturn(Optional.of(category))
        // Mock repository doesn't connect to postgreSql, so it won't generate product ID. So We simulate it
        `when`(productRepository.save(any(Product::class.java)))
            .thenAnswer { invocation ->
                val product = invocation.getArgument<Product>(0)
                product.id = 1L
                product
            }
        // Act
        val result = productService.createProduct(request)
        // Assert
        assertNotNull(result.id)
        assertEquals(1L, result.id)
        assertEquals("Black T-Shirt", result.name)
        assertEquals(BigDecimal("2500.00"), result.price)
        assertEquals(1L, result.categoryId)
        assertEquals("Men's Clothing", result.categoryName)

        verify(productRepository).save(any(Product::class.java))
    }
    // UPDATE AN EXISTING PRODUCT
    @Test
    fun `updateProduct updates existing product successfully`() {

        // Arrange
        val newCategory = Category(
            id = 2L,
            name = "Accessories"
        )

        val oldCategory = Category(
            id = 1L,
            name = "Men's Clothing"
        )

        val existingProduct = Product(
            id = 10L,
            name = "Black T-shirt",
            description = "Cotton T-Shirt",
            price = BigDecimal("25000.00"),
            imageUrl = null,
            stockQuantity = 10,
            category = oldCategory
        )

        val request = ProductRequest(
            name = "Black Cap",
            description = "Premium Cotton Cap",
            price = BigDecimal("1500.00"),
            imageUrl = null,
            stockQuantity = 20,
            categoryId = 2L
        )

        `when`(productRepository.findById(10L))
            .thenReturn(Optional.of(existingProduct))

        `when`(categoryRepository.findById(2L))
            .thenReturn(Optional.of(newCategory))
    // Simulate postgreSQL connection to generate product ID
        `when`(productRepository.save(any(Product::class.java)))
            .thenAnswer { invocation ->
                invocation.getArgument<Product>(0)
            }
        // Act
        val resultId = productService.updateProduct(10L, request)
        // Assert
        assertEquals(10L, resultId.id)
        assertEquals("Black Cap", resultId.name)
        assertEquals("Premium Cotton Cap", resultId.description)
        assertEquals(BigDecimal("1500.00"), resultId.price)
        assertEquals(20, resultId.stockQuantity)
        assertEquals(2L, resultId.categoryId)
        assertEquals("Accessories", resultId.categoryName)

        verify(productRepository).save(existingProduct)
    }
    // UPDATE PRODUCT WHEN PRODUCT DOES NOT EXIST
    @Test
    fun `updateProduct throw 404 when product does not exist`() {

        // Arrange
        val request = ProductRequest(
            name = "Black Hoodie",
            description = "Premium Cotton Hoodie",
            price = BigDecimal("45000.00"),
            imageUrl = null,
            stockQuantity = 15,
            categoryId = 1L
        )

        `when`(productRepository.findById(99999L))
            .thenReturn(Optional.empty())

        // Act
        val exception = assertThrows(ResponseStatusException::class.java) {
            productService.updateProduct(99999L, request)
        }

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, exception.statusCode)
    }
    // DELETE PRODUCT WHEN PRODUCT EXISTS
    @Test
    fun `deleteProduct deletes product when it exists`() {

        // Arrange
        val productId = 10L
        // Simulating an existing product
        `when`(productRepository.existsById(productId))
            .thenReturn(true)

        // Act
        productService.deleteProduct(productId)

        // Assert
        // Verify deletion
        verify(productRepository).deleteById(productId)
    }

    @Test
    fun `deleteProduct throws 404 when product does not exist`() {

        // Arrange
        val productId = 99999L

        `when`(productRepository.existsById(productId))
            .thenReturn(false)

        // Act
        val exception = assertThrows(ResponseStatusException::class.java) {
            productService.deleteProduct(productId)
        }

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, exception.statusCode)
        // This checks that the deletion method was never called
        verify(productRepository, never()).deleteById(productId)
    }
}