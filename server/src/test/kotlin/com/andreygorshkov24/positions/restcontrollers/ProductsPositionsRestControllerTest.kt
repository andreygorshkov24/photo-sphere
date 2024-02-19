package com.andreygorshkov24.positions.restcontrollers

import com.andreygorshkov24.rest.RestError
import com.andreygorshkov24.ObjectMapperInit
import com.andreygorshkov24.positions.ProductsPositionsService
import com.andreygorshkov24.positions.ProductsPositionsService.ProductsPositionsResult.ProductDoesNotExistProductsPositionsResult
import com.andreygorshkov24.rest.positions.ProductsPositionsRestController
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.patch
import java.net.URI

@WebMvcTest
@ContextConfiguration(classes = [ProductsPositionsRestController::class, ObjectMapperInit::class])
class ProductsPositionsRestControllerTest {

  @Autowired
  private lateinit var objectMapper: ObjectMapper

  @Autowired
  private lateinit var mockMvc: MockMvc

  @MockBean
  private lateinit var productsPositionsService: ProductsPositionsService

  @Test
  fun `test toUp when product id is blank`() {
    val uri = URI.create("/products/position/up?productId=")
    mockMvc.patch(uri).andExpect {
      status { isBadRequest() }
      content {
        contentType(MediaType.APPLICATION_JSON)
        json(objectMapper.writeValueAsString(RestError("product id is blank")))
      }
    }
  }

  @Test
  fun `test toDown when product id is blank`() {
    val uri = URI.create("/products/position/down?productId=")
    mockMvc.patch(uri).andExpect {
      status { isBadRequest() }
      content {
        contentType(MediaType.APPLICATION_JSON)
        json(objectMapper.writeValueAsString(RestError("product id is blank")))
      }
    }
  }

  @Test
  fun `test toUp when product does not exist`() {
    val randomProductId = "42"
    val restError = RestError("product $randomProductId does not exist")

    `when`(productsPositionsService.toUp(randomProductId)).thenReturn(
      ProductDoesNotExistProductsPositionsResult(restError.error)
    )

    val uri = URI.create("/products/position/up?productId=$randomProductId")
    mockMvc.patch(uri).andExpect {
      status { isNotFound() }
      content {
        contentType(MediaType.APPLICATION_JSON)
        json(objectMapper.writeValueAsString(restError))
      }
    }
  }

  @Test
  fun `test toDown when product does not exist`() {
    val randomProductId = "42"
    val restError = RestError("product $randomProductId was does not exist")

    `when`(productsPositionsService.toDown(randomProductId)).thenReturn(
      ProductDoesNotExistProductsPositionsResult(restError.error)
    )

    val uri = URI.create("/products/position/down?productId=$randomProductId")
    mockMvc.patch(uri).andExpect {
      status { isNotFound() }
      content {
        contentType(MediaType.APPLICATION_JSON)
        json(objectMapper.writeValueAsString(restError))
      }
    }
  }
}
