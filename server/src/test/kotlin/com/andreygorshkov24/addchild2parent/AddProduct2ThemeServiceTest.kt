package com.andreygorshkov24.addchild2parent

import com.andreygorshkov24.*
import com.andreygorshkov24.ThemesFoldersService.Companion.ROOT_THEME_FOLDER_ID
import com.andreygorshkov24.ThemesService.Companion.ROOT_THEME_ID
import com.andreygorshkov24.addchild2parent.AddProduct2ThemeService.AddProduct2ThemeResult.ProductDoesNotExistAddProduct2ThemeResult
import com.andreygorshkov24.addchild2parent.AddProduct2ThemeService.AddProduct2ThemeResult.ThemeDoesNotExistAddProduct2ThemeResult
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AddProduct2ThemeServiceTest {

  private lateinit var products: List<Product>
  private lateinit var productsService: ProductsService

  private lateinit var themes: List<Theme>
  private lateinit var themesService: ThemesService

  private lateinit var addProduct2ThemeService: AddProduct2ThemeService

  @BeforeEach
  fun beforeEach() {
    val rootThemeFolder = FakeThemeFolder(ROOT_THEME_FOLDER_ID, "Theme folder #${ROOT_THEME_FOLDER_ID}", "Theme folder #${ROOT_THEME_FOLDER_ID}", 0)

    val rootTheme = FakeTheme(ROOT_THEME_ID, "Theme #${ROOT_THEME_ID}", "Theme #${ROOT_THEME_ID}", rootThemeFolder.id, 0)

    val themesCount = 3
    themes = (0 until themesCount).map { i -> FakeTheme("theme_$i", "Theme #$i", "Theme #$i", rootThemeFolder.id, themesCount - i) } + rootTheme
    themesService = FakeThemesService(themes)

    val productsCount = 3
    products =
      (0 until productsCount).map { i -> FakeProduct("product_$i", "Product #$i", "Product #$i", rootThemeFolder.id, productsCount - i, "<p>Lorem ipsum dollar #$i</p>", ProductContentType.TEXT_HTML) }
    productsService = FakeProductsService(products)

    addProduct2ThemeService = AddProduct2ThemeServiceImpl(productsService, themesService)
  }

  @Test
  fun `test addProduct2Theme`() {
    addProduct2ThemeService.addProduct2Theme(products[0].id, themes[0].id)
    addProduct2ThemeService.addProduct2Theme(products[1].id, themes[1].id)
    addProduct2ThemeService.addProduct2Theme(products[2].id, themes[2].id)

    assertEquals(themes[0].id, products[0].parentId)
    assertEquals(0, products[0].position)

    assertEquals(themes[1].id, products[1].parentId)
    assertEquals(0, products[1].position)

    assertEquals(themes[2].id, products[2].parentId)
    assertEquals(0, products[2].position)

    val lastIndex = themes.lastIndex
    addProduct2ThemeService.addProduct2Theme(products[0].id, themes[lastIndex].id)
    addProduct2ThemeService.addProduct2Theme(products[1].id, themes[lastIndex].id)
    addProduct2ThemeService.addProduct2Theme(products[2].id, themes[lastIndex].id)

    assertEquals(themes[lastIndex].id, products[0].parentId)
    assertEquals(0, products[0].position)

    assertEquals(themes[lastIndex].id, products[1].parentId)
    assertEquals(1, products[1].position)

    assertEquals(themes[lastIndex].id, products[2].parentId)
    assertEquals(2, products[2].position)
  }

  @Test
  fun `test addProduct2Theme when theme does not exist`() {
    val randomThemeId = "42"

    val addProduct2ThemeResult = addProduct2ThemeService.addProduct2Theme(themes[0].id, randomThemeId)

    assertEquals(ThemeDoesNotExistAddProduct2ThemeResult::class.java, addProduct2ThemeResult::class.java)
    assertEquals("theme $randomThemeId does not exist", addProduct2ThemeResult.error)
  }

  @Test
  fun `test addProduct2Theme when product does not exist`() {
    val randomProductId = "42"

    val addProduct2ThemeResult = addProduct2ThemeService.addProduct2Theme(randomProductId, themes[themes.lastIndex].id)

    assertEquals(ProductDoesNotExistAddProduct2ThemeResult::class.java, addProduct2ThemeResult::class.java)
    assertEquals("product $randomProductId does not exist", addProduct2ThemeResult.error)
  }
}
