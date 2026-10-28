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
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
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
}