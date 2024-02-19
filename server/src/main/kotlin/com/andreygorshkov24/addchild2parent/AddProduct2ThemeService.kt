package com.andreygorshkov24.addchild2parent

interface AddProduct2ThemeService {

  fun addProduct2Theme(productId: String, themeId: String): AddProduct2ThemeResult

  sealed class AddProduct2ThemeResult(val error: String?) {

    class ThemeDoesNotExistAddProduct2ThemeResult(error: String) : AddProduct2ThemeResult(error)

    class ProductDoesNotExistAddProduct2ThemeResult(error: String) : AddProduct2ThemeResult(error)

    class SucceededAddProduct2ThemeResult() : AddProduct2ThemeResult(null)
  }
}
