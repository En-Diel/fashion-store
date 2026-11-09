package com.endiel.fashion_store_backend.controller

import com.endiel.fashion_store_backend.dto.CategoryRequest
import com.endiel.fashion_store_backend.dto.CategoryResponse
import com.endiel.fashion_store_backend.service.CategoryService
import org.hamcrest.Matchers.hasSize
import org.springframework.http.MediaType
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.Test

// THIS RECEIVES HTTP REQUESTS AND RETURN HTTPS RESPONSES
// WebMvcTest loads Web layer without loading the entire application or connection to PostgreSQL
@WebMvcTest(CategoryController::class)
class CategoryControllerTest {

    // @Autowired gives use the MockMvc instance that sends simulated HTTP requests. We can test
    // HTTP server without starting the real server
    @Autowired
    private lateinit var mockMvc: MockMvc

    // @MockitoBean replaces the real CategoryService with a Mockito mock
    @MockitoBean
    private lateinit var categoryService: CategoryService

    @Test
    fun `getAllCategories returns all categories`() {

        // Arrange
        // We create a sample response data
        val categories = listOf(
            CategoryResponse(id = 1L, name = "Men's Clothing"),
            CategoryResponse(id = 2L, name = "Women's Clothing"),
            CategoryResponse(id = 3L, name = "Shoes"),
        )
        // Tells Mockito what the Service should return
        `when`(categoryService.getAllCategories())
            .thenReturn(categories)

        // Act & Assert
        // Simulate HTTP requests
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
            .get("/api/v1/categories") // Simulates an HTTP GET request
            .accept(MediaType.APPLICATION_JSON) // Tells Spring that the client accepts a JSON response
        )
            .andExpect(status().isOk) // Expects HTTP 200
            // Check's the number of categories
            // Expect the JSON response to contain exactly 3 elements
            // $ represents the root of the JSON document (Entire JSON response)
            .andExpect(jsonPath("$").value(hasSize<Any>(3)))
            // The two below checks the categories first name
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("Men's Clothing"))
            // The last two checks the 2nd and 3rd categories
            .andExpect(jsonPath("$[1].name").value("Women's Clothing"))
            .andExpect(jsonPath("$[2].name").value("Shoes"))
    }

    @Test
    fun `getCategoryById returns category when category exists`() {

        // Arrange
        // Creating a category
        val category = CategoryResponse(
            id = 1L,
            name = "Men's Clothing"
        )
        // Configuring Mockito
        `when`(categoryService.getCategoryById(1L))
            .thenReturn(category)

        // Act & Assert
        mockMvc.perform(
            MockMvcRequestBuilders
                // Sending request
                .get("/api/v1/categories/1")
                .accept(MediaType.APPLICATION_JSON)
        )
            // Checking the response
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Men's Clothing"))
    }

    @Test
    fun `createCategory returns created category`() {

        // Arrange
        val request = CategoryRequest(name = "Accessories")
        val response = CategoryResponse(
            id = 4L,
            name = "Accessories"
        )

        `when`(categoryService.createCategory(request))
            .thenReturn(response)

        // Act & Assert
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name": "Accessories"}""")
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(4))
            .andExpect(jsonPath("$.name").value("Accessories"))
    }

}