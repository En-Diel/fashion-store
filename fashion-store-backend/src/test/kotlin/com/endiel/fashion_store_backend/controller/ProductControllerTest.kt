package com.endiel.fashion_store_backend.controller

import com.endiel.fashion_store_backend.dto.ProductResponse
import com.endiel.fashion_store_backend.service.ProductService
import org.hamcrest.Matchers.hasSize
import org.hibernate.query.Order.asc
import org.springframework.http.MediaType
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.AutoConfigurationPackages.has
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
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

}