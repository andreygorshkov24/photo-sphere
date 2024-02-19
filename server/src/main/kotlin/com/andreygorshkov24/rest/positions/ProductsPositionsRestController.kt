package com.andreygorshkov24.rest.positions

import com.andreygorshkov24.positions.ProductsPositionsService
import com.andreygorshkov24.positions.ProductsPositionsService.ProductsPositionsResult
import com.andreygorshkov24.positions.ProductsPositionsService.ProductsPositionsResult.ProductDoesNotExistProductsPositionsResult
import com.andreygorshkov24.positions.ProductsPositionsService.ProductsPositionsResult.SucceedProductsPositionsResult
import com.andreygorshkov24.rest.RestError
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletResponse.*
import org.springframework.web.bind.annotation.*

@RestController
class ProductsPositionsRestController(
  private val response: HttpServletResponse, private val productsPositionsService: ProductsPositionsService
) {

  @CrossOrigin
  @PatchMapping("/products/position/up")
  fun toUp(
    @RequestParam("productId", required = true) productId: String
  ): RestError? {
    if (productId.isBlank()) {
      response.status = SC_BAD_REQUEST
      return RestError("product id is blank")
    }

    val productsPositionsResult = productsPositionsService.toUp(productId)
    return handleProductsPositionsResult(productsPositionsResult)
  }

  @CrossOrigin
  @PatchMapping("/products/position/down")
  fun toDown(
    @RequestParam("productId", required = true) productId: String
  ): RestError? {
    if (productId.isBlank()) {
      response.status = SC_BAD_REQUEST
      return RestError("product id is blank")
    }

    val productsPositionsResult = productsPositionsService.toDown(productId)
    return handleProductsPositionsResult(productsPositionsResult)
  }

  private fun handleProductsPositionsResult(productsPositionsResult: ProductsPositionsResult): RestError? {
    return when (productsPositionsResult) {
      is ProductDoesNotExistProductsPositionsResult -> {
        response.status = SC_NOT_FOUND
        return RestError(productsPositionsResult.error!!)
      }

      is SucceedProductsPositionsResult -> {
        response.status = SC_OK
        null
      }
    }
  }
}
