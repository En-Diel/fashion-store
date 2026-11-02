package com.endiel.fashion_store_backend.service

import com.endiel.fashion_store_backend.dto.CategoryRequest
import com.endiel.fashion_store_backend.entity.Category
import com.endiel.fashion_store_backend.repository.CategoryRepository
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.aot.hint.TypeReference.listOf
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.server.ResponseStatusException
import java.util.Optional
import kotlin.test.assertEquals

class CategoryServiceTest {

    private val categoryRepository = mock(CategoryRepository::class.java)
    private val categoryService = CategoryService(categoryRepository)

    // SIMULATES IF CATEGORY EXIST IN DATABASE
    @Test
    fun `getCategoryById returns category when it exists`() {

        // Arrange
        val category = Category(
            id = 1L,
            name = "Men's Clothing"
        )

        `when`(categoryRepository.findById(1L))
            .thenReturn(Optional.of(category))

        // Act
        val result = categoryService.getCategoryById(1L)
        // Assert
        assertNotNull(result)

        val actualCategory = result

        assertEquals(1L, result.id)
        assertEquals("Men's Clothing", result.name)

    }
    // SIMULATES A CATEGORY THAT DOESN'T EXIST
    @Test
    fun `getCategoryById throws 404 when category does not exist`() {

        // Arrange
        `when`(categoryRepository.findById(99999L))
            .thenReturn(Optional.empty())

        // Act
        val exception = assertThrows(ResponseStatusException::class.java) {
            categoryService.getCategoryById(99999L)
        }
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, exception.statusCode)
    }
    // CREATING A CATEGORY SUCCESSFULLY
    @Test
    fun `createCategory saves category successfully`() {

        // Arrange
        val request = CategoryRequest(
            name = "Shoes"
        )

        `when`(categoryRepository.existsByName("Shoes"))
            .thenReturn(false)

        `when`(categoryRepository.save(any(Category::class.java)))
            .thenAnswer { invocation ->
                val category = invocation.getArgument<Category>(0)
                category.id = 3L
                category
            }

        // Act
        val result = categoryService.createCategory(request)

        // Assert
        assertEquals(3L, result.id)
        assertEquals("Shoes", result.name)

        verify(categoryRepository).save(any(Category::class.java))

    }
    // PREVENTING DUPLICATE CATEGORY NAMES
    @Test
    fun `createCategory throws 404 when category already exists`() {

        // Arrange
        // Prepare a request to create a request named Shoes
        val request = CategoryRequest(
            name = "Shoes"
        )
        // Mockito simulates Shoes already exists
        `when`(categoryRepository.existsByName("Shoes"))
            .thenReturn(true)
        // Act
        // Call the real categoryService
        val exception = assertThrows(ResponseStatusException::class.java) {
            categoryService.createCategory(request)
        }
        // Assert
        // JUnit verifies that the service throws HTTP 409
        assertEquals(HttpStatus.CONFLICT, exception.statusCode)

        // Mockito verifies that save was never called
        verify(categoryRepository, never())
            .save(any(Category::class.java))
    }
    // RETRIEVING ALL CATEGORIES
    @Test
    fun `getAllCategories returns all categories`() {

        // Arrange
        val categories = listOf(
            Category(id = 1L, name = "Men's Clothing"),
            Category(id = 2L, name = "Women's Clothing"),
            Category(id = 3L, name = "Shoes")
        )

        `when`(categoryRepository.findAll())
            .thenReturn(categories)

        // Act
        val result = categoryService.getAllCategories()
        // Assert
        assertEquals(3, result.size)
        assertEquals("Men's Clothing", result[0].name)
        assertEquals("Women's Clothing", result[1].name)
        assertEquals("Shoes", result[2].name)

    }
    // SUCCESSFULLY CATEGORY DELETION
    @Test
    fun `deleteCategory deletes category when it exists`() {

        // Arrange
        val categoryId = 3L

        `when`(categoryRepository.existsById(categoryId))
            .thenReturn(true)

        // Act
        categoryService.deleteCategory(categoryId)
        // Assert
        verify(categoryRepository).deleteById(categoryId)

    }
    // THROWS ERROR WHEN CATEGORY DOES NOT EXIST
    @Test
    fun `deleteCategory throw 404 when category does not exits`() {

        // Arrange
        val categoryId = 99999L

        `when`(categoryRepository.existsById(categoryId))
            .thenReturn(false)

        // Act
        val exception = assertThrows(ResponseStatusException::class.java) {
            categoryService.deleteCategory(categoryId)
        }
        // Assert
        // Checks that the service throws HTTP 404 status
        assertEquals(HttpStatus.NOT_FOUND, exception.statusCode)

        verify(categoryRepository).existsById(categoryId)
        //verify(categoryRepository, never()).deleteById(categoryId)

    }
}