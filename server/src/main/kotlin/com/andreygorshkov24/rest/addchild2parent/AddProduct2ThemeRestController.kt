package com.andreygorshkov24.rest.addchild2parent

import com.andreygorshkov24.addchild2parent.AddProduct2ThemeService
import com.andreygorshkov24.addchild2parent.AddProduct2ThemeService.AddProduct2ThemeResult.*
import com.andreygorshkov24.rest.RestError
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletResponse.*
import org.springframework.web.bind.annotation.*

@RestController
class AddProduct2ThemeRestController(
  private val response: HttpServletResponse, private val addProduct2ThemeService: AddProduct2ThemeService
) {

  @CrossOrigin
  @PatchMapping("/themes/products/add")
  fun addProduct2Theme(
    @RequestParam("productId", required = true) productId: String, @RequestParam("themeId", required = true) themeId: String
  ): RestError? {
    if (productId.isBlank()) {
      response.status = SC_BAD_REQUEST
      return RestError("product id is blank")
    }

    if (themeId.isBlank()) {
      response.status = SC_BAD_REQUEST
      return RestError("theme id is blank")
    }

    val addProduct2ThemeResult = addProduct2ThemeService.addProduct2Theme(productId, themeId)
    return when (addProduct2ThemeResult) {
      is ThemeDoesNotExistAddProduct2ThemeResult -> {
        response.status = SC_NOT_FOUND
        RestError(addProduct2ThemeResult.error!!)
      }

      is ProductDoesNotExistAddProduct2ThemeResult -> {
        response.status = SC_NOT_FOUND
        RestError(addProduct2ThemeResult.error!!)
      }

      is SucceededAddProduct2ThemeResult -> {
        response.status = SC_OK
        null
      }
    }
  }
}
