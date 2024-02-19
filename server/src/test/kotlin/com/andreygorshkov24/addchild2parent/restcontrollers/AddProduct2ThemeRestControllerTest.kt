package com.andreygorshkov24.addchild2parent.restcontrollers

import com.andreygorshkov24.rest.RestError
import com.andreygorshkov24.ObjectMapperInit
import com.andreygorshkov24.addchild2parent.AddProduct2ThemeService
import com.andreygorshkov24.addchild2parent.AddProduct2ThemeService.AddProduct2ThemeResult.ProductDoesNotExistAddProduct2ThemeResult
import com.andreygorshkov24.addchild2parent.AddProduct2ThemeService.AddProduct2ThemeResult.ThemeDoesNotExistAddProduct2ThemeResult
import com.andreygorshkov24.rest.addchild2parent.AddProduct2ThemeRestController
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
@ContextConfiguration(classes = [AddProduct2ThemeRestController::class, ObjectMapperInit::class])
class AddProduct2ThemeRestControllerTest {

  @Autowired
  private lateinit var objectMapper: ObjectMapper

  @Autowired
  private lateinit var mockMvc: MockMvc

  @MockBean
  private lateinit var addProduct2ThemeService: AddProduct2ThemeService

  @Test
  fun `test addProduct2Theme when product id is blank`() {
    val uri = URI.create("/themes/products/add?productId=&themeId=42")
    mockMvc.patch(uri).andExpect {
      status { isBadRequest() }
      content {
        contentType(MediaType.APPLICATION_JSON)
        json(objectMapper.writeValueAsString(RestError("product id is blank")))
      }
    }
  }

  @Test
  fun `test addProduct2Theme when theme id is blank`() {
    val uri = URI.create("/themes/products/add?productId=42&themeId=")
    mockMvc.patch(uri).andExpect {
      status { isBadRequest() }
      content {
        contentType(MediaType.APPLICATION_JSON)
        json(objectMapper.writeValueAsString(RestError("theme id is blank")))
      }
    }
  }

  @Test
  fun `test addProduct2Theme when product does not exist`() {
    val randomProductId = "42"
    val restError = RestError("product $randomProductId does not exist")

    `when`(addProduct2ThemeService.addProduct2Theme(randomProductId, "42")).thenReturn(
      ProductDoesNotExistAddProduct2ThemeResult(restError.error)
    )

    val uri = URI.create("/themes/products/add?productId=$randomProductId&themeId=42")
    mockMvc.patch(uri).andExpect {
      status { isNotFound() }
      content {
        contentType(MediaType.APPLICATION_JSON)
        json(objectMapper.writeValueAsString(restError))
      }
    }
  }

  @Test
  fun `test addProduct2Theme when theme does not exist`() {
    val randomThemeId = "42"
    val restError = RestError("theme $randomThemeId does not exist")

    `when`(addProduct2ThemeService.addProduct2Theme("42", randomThemeId)).thenReturn(
      ThemeDoesNotExistAddProduct2ThemeResult(restError.error)
    )

    val uri = URI.create("/themes/products/add?productId=42&themeId=$randomThemeId")
    mockMvc.patch(uri).andExpect {
      status { isNotFound() }
      content {
        contentType(MediaType.APPLICATION_JSON)
        json(objectMapper.writeValueAsString(restError))
      }
    }
  }
}
