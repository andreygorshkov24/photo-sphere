package com.andreygorshkov24

import com.andreygorshkov24.ThemesFoldersService.Companion.ROOT_THEME_FOLDER_ID
import com.andreygorshkov24.ThemesService.Companion.ROOT_THEME_ID
import com.andreygorshkov24.rest.RestProduct
import com.andreygorshkov24.rest.ProductsRestController
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest
@ContextConfiguration(classes = [ProductsRestController::class, ObjectMapperInit::class])
class ProductsRestControllerTest {

  @Autowired
  private lateinit var objectMapper: ObjectMapper

  @Autowired
  private lateinit var mockMvc: MockMvc

  @MockBean
  private lateinit var productsService: ProductsService

  @Test
  fun `test fetchAll`() {
    val rootThemeFolder = FakeThemeFolder(ROOT_THEME_FOLDER_ID, "Theme folder #$ROOT_THEME_FOLDER_ID", "Theme folder #$ROOT_THEME_FOLDER_ID", 0)

    val rootTheme = FakeTheme(ROOT_THEME_ID, "Theme #$ROOT_THEME_ID", "Theme #$ROOT_THEME_ID", rootThemeFolder.id, 0)

    val products = (0 until 27).map { i -> FakeProduct("product_$i", "Product #$i", "Product #$i", rootTheme.id, i + 1, "<p>Lorem ipsum dollar #$i</p>", ProductContentType.TEXT_HTML) }

    `when`(productsService.findAll(ROOT_THEME_ID)).thenReturn(products)

    mockMvc.get("/products?themeId=$ROOT_THEME_ID").andExpect {
      status { isOk() }
      content {
        contentType(MediaType.APPLICATION_JSON)
        json(objectMapper.writeValueAsString(products.map { product ->
          RestProduct(product.id, product.name, product.description, product.content, product.contentType.value)
        }))
      }
    }
  }
}
