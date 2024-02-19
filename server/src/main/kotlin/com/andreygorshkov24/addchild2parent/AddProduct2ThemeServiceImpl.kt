package com.andreygorshkov24.addchild2parent

import com.andreygorshkov24.*
import com.andreygorshkov24.addchild2parent.AddProduct2ThemeService.AddProduct2ThemeResult
import com.andreygorshkov24.addchild2parent.AddProduct2ThemeService.AddProduct2ThemeResult.*
import org.springframework.stereotype.Service

@Service
class AddProduct2ThemeServiceImpl(
  private val productsService: ProductsService, private val themesService: ThemesService
) : AddProduct2ThemeService, AddChild2ParentService<Product, Theme>() {

  override fun addProduct2Theme(productId: String, themeId: String): AddProduct2ThemeResult {
    return when (addChild2Parent(productId, themeId)) {
      Result.PARENT_NOT_FOUND -> ThemeDoesNotExistAddProduct2ThemeResult("theme $themeId does not exist")
      Result.CHILD_NOT_FOUND -> ProductDoesNotExistAddProduct2ThemeResult("product $productId does not exist")
      Result.SUCCEEDED -> SucceededAddProduct2ThemeResult()
    }
  }

  override fun findParent(parentId: String): Theme? = themesService.find(parentId)

  override fun findChildren(): List<Product> = productsService.findAll()
}