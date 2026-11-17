package com.endiel.fashion_store_backend.controller

import com.endiel.fashion_store_backend.dto.ProductRequest
import com.endiel.fashion_store_backend.dto.ProductResponse
import com.endiel.fashion_store_backend.service.ProductService

import org.hamcrest.Matchers.hasSize
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import java.math.BigDecimal
import kotlin.test.Test

@WebMvcTest(ProductController::class)
class ProductControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var productService: ProductService

    @Test
    fun `getAllProducts returns all products`() {

        // Arrange
        val products = listOf(
            ProductResponse(
                id = 1L,
                name = "T-shirt",
                description = "Cotton T-shirt",
                price = BigDecimal("2500"),
                imageUrl = "https://example.com/tshirt.jpg",
                stockQuantity = 20,
                categoryId = 1L,
                categoryName = "Men's Clothing"
            ),
            ProductResponse(
                id = 2L,
                name = "Sneakers",
                description = "Casual sneakers",
                price = BigDecimal("85000"),
                imageUrl = "https://example.com/sneakers.jpg",
                stockQuantity = 10,
                categoryId = 2L,
                categoryName = "Shoes"
            )
        )
        val pageable = PageRequest.of(0, 10, Sort.by("id").ascending())
        val productPage = PageImpl(products, pageable, products.size.toLong())

        `when`(
            productService.getAllProducts(
                null, null,0,10, "id", "asc"
            )
        ).thenReturn(productPage)

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders
            .get("/api/v1/products")
            .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").value(hasSize<Any>(2)))
            .andExpect(jsonPath("$.content[0].id").value(1))
            .andExpect(jsonPath("$.content[0].name").value("T-shirt"))
            .andExpect(jsonPath("$.content[1].name").value("Sneakers"))
            .andExpect(jsonPath("$.totalElements").value(2))
            .andExpect(jsonPath("$.number").value(0))
    }

    @Test
    fun `getProductById returns product when product exists`() {

        // Arrange
        val product = ProductResponse(
            id = 1L,
             name = "T-shirt",
            description = "Cotton T-shirt",
            price = BigDecimal("2500"),
            imageUrl = "https://example.com/tshirt.jpg",
            stockQuantity = 20,
            categoryId = 1L,
            categoryName = "Men's Clothing"
        )

        `when`(productService.getProductById(1L))
            .thenReturn(product)

        // Act & Assert
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/v1/products/1")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("T-shirt"))
            .andExpect(jsonPath("$.description").value("Cotton T-shirt"))
            .andExpect(jsonPath("$.price").value(2500))
            .andExpect(jsonPath("$.stockQuantity").value(20))
            .andExpect(jsonPath("$.categoryId").value(1))
            .andExpect(jsonPath("$.categoryName").value("Men's Clothing"))
    }

    @Test
    fun `createProduct returns created product`() {

        // Arrange
        val request  = ProductRequest(
            name = "Sneakers",
            description = "Casual sneakers",
            price = BigDecimal("50000"),
            imageUrl = "http://example.com/sneakers.jpg",
            stockQuantity = 15,
            categoryId = 1L
        )

        val response = ProductResponse(
             id = 1L,
            name = "Sneakers",
            description = "Casual sneakers",
            price = BigDecimal("50000"),
            imageUrl = "http://example.com/sneakers.jpg",
            stockQuantity = 15,
            categoryId = 1L,
            categoryName = "Shoes"
        )
        // Mocks the service response
        `when`(productService.createProduct(request))
            .thenReturn(response)

        // Act & Assert
        mockMvc.perform(
            // Sends HTTP post request
            MockMvcRequestBuilders.post("/api/v1/products")
                // Tells Spring we're sending JSON
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """ 
                    {
                        "name": "Sneakers", 
                        "description": "Casual sneakers", 
                        "price": 50000, 
                        "imageUrl": "http://example.com/sneakers.jpg", 
                        "stockQuantity": 15, 
                        "categoryId": 1 
                    }
                    """.trimIndent()
            )
        )
            //.andDo(print())
            // Checks for HTTP 201
            .andExpect(status().isCreated)
            // jsonPath checks fields in the returned JSON
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Sneakers"))
            .andExpect(jsonPath("$.price").value(50000))
            .andExpect(jsonPath("$.categoryName").value("Shoes"))

        // Confirms the controller called the service
        verify(productService).createProduct(request)
    }

}