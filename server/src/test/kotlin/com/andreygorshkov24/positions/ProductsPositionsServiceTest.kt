package com.andreygorshkov24.positions

import com.andreygorshkov24.*
import com.andreygorshkov24.ThemesFoldersService.Companion.ROOT_THEME_FOLDER_ID
import com.andreygorshkov24.ThemesService.Companion.ROOT_THEME_ID
import com.andreygorshkov24.positions.ProductsPositionsService.ProductsPositionsResult.ProductDoesNotExistProductsPositionsResult
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ProductsPositionsServiceTest {

  private lateinit var products: List<Product>
  private lateinit var productsService: ProductsService

  private lateinit var productsPositionsService: ProductsPositionsService

  @BeforeEach
  fun beforeEach() {
    val rootThemeFolder = FakeThemeFolder(ROOT_THEME_FOLDER_ID, "Theme folder #$ROOT_THEME_FOLDER_ID", "Theme folder #$ROOT_THEME_FOLDER_ID", 0)

    val rootTheme = FakeTheme(ROOT_THEME_ID, "Theme #$ROOT_THEME_ID", "Theme #$ROOT_THEME_ID", rootThemeFolder.id, 0)

    val productsCount = 5
    products = (0 until productsCount).map { i -> FakeProduct("product_$i", "Product #$i", "Product #$i", rootTheme.id, productsCount - i - 1, "<h1>Hello world!</h1>", ProductContentType.TEXT_HTML) }
    productsService = FakeProductsService(products)

    productsPositionsService = ProductsPositionsServiceImpl(productsService)
  }

  @Test
  fun `test toUp and toDown together`() {
    repeat(4) { productsPositionsService.toDown(products[0].id) }
    repeat(3) { productsPositionsService.toDown(products[1].id) }
    repeat(2) { productsPositionsService.toDown(products[2].id) }
    repeat(1) { productsPositionsService.toDown(products[3].id) }

    assertEquals(4, products[4].position)
    assertEquals(3, products[3].position)
    assertEquals(2, products[2].position)
    assertEquals(1, products[1].position)
    assertEquals(0, products[0].position)

    repeat(4) { productsPositionsService.toUp(products[0].id) }
    repeat(3) { productsPositionsService.toUp(products[1].id) }
    repeat(2) { productsPositionsService.toUp(products[2].id) }
    repeat(1) { productsPositionsService.toUp(products[3].id) }

    assertEquals(4, products[0].position)
    assertEquals(3, products[1].position)
    assertEquals(2, products[2].position)
    assertEquals(1, products[3].position)
    assertEquals(0, products[4].position)
  }

  @Test
  fun `test toUp on bound`() {
    productsPositionsService.toUp(products[0].id)

    assertEquals(4, products[0].position)
    assertEquals(3, products[1].position)
    assertEquals(2, products[2].position)
    assertEquals(1, products[3].position)
    assertEquals(0, products[4].position)
  }

  @Test
  fun `test toDown on bound`() {
    productsPositionsService.toDown(products[4].id)

    assertEquals(4, products[0].position)
    assertEquals(3, products[1].position)
    assertEquals(2, products[2].position)
    assertEquals(1, products[3].position)
    assertEquals(0, products[4].position)
  }

  @Test
  fun `test toUp`() {
    val targetThemeIndex = 0

    productsPositionsService.toDown(products[targetThemeIndex].id)

    assertEquals(4, products[1].position)
    assertEquals(3, products[targetThemeIndex].position)
    assertEquals(2, products[2].position)
    assertEquals(1, products[3].position)
    assertEquals(0, products[4].position)

    productsPositionsService.toDown(products[targetThemeIndex].id)

    assertEquals(4, products[1].position)
    assertEquals(3, products[2].position)
    assertEquals(2, products[targetThemeIndex].position)
    assertEquals(1, products[3].position)
    assertEquals(0, products[4].position)

    productsPositionsService.toDown(products[targetThemeIndex].id)

    assertEquals(4, products[1].position)
    assertEquals(3, products[2].position)
    assertEquals(2, products[3].position)
    assertEquals(1, products[targetThemeIndex].position)
    assertEquals(0, products[4].position)

    productsPositionsService.toDown(products[0].id)

    assertEquals(4, products[1].position)
    assertEquals(3, products[2].position)
    assertEquals(2, products[3].position)
    assertEquals(1, products[4].position)
    assertEquals(0, products[targetThemeIndex].position)
  }

  @Test
  fun `test toDown`() {
    val targetThemeIndex = 4

    productsPositionsService.toUp(products[targetThemeIndex].id)

    assertEquals(4, products[0].position)
    assertEquals(3, products[1].position)
    assertEquals(2, products[2].position)
    assertEquals(1, products[targetThemeIndex].position)
    assertEquals(0, products[3].position)

    productsPositionsService.toUp(products[targetThemeIndex].id)

    assertEquals(4, products[0].position)
    assertEquals(3, products[1].position)
    assertEquals(2, products[targetThemeIndex].position)
    assertEquals(1, products[2].position)
    assertEquals(0, products[3].position)

    productsPositionsService.toUp(products[targetThemeIndex].id)

    assertEquals(4, products[0].position)
    assertEquals(3, products[targetThemeIndex].position)
    assertEquals(2, products[1].position)
    assertEquals(1, products[2].position)
    assertEquals(0, products[3].position)

    productsPositionsService.toUp(products[targetThemeIndex].id)

    assertEquals(4, products[targetThemeIndex].position)
    assertEquals(3, products[0].position)
    assertEquals(2, products[1].position)
    assertEquals(1, products[2].position)
    assertEquals(0, products[3].position)
  }

  @Test
  fun `test toUp when product does not exist`() {
    val randomProductId = "42"

    val result = productsPositionsService.toUp(randomProductId)

    assertEquals(ProductDoesNotExistProductsPositionsResult::class.java, result::class.java)
    assertEquals("product $randomProductId does not exist", result.error)
  }

  @Test
  fun `test toDown when product does not exist`() {
    val randomProductId = "42"

    val result = productsPositionsService.toDown(randomProductId)

    assertEquals(ProductDoesNotExistProductsPositionsResult::class.java, result::class.java)
    assertEquals("product $randomProductId does not exist", result.error)
  }
}
