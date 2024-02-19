package com.andreygorshkov24.rest

import com.andreygorshkov24.ProductsService
import org.springframework.web.bind.annotation.*

@RestController
class ProductsRestController(
  private val productsService: ProductsService
) {

  @CrossOrigin
  @GetMapping("/products")
  fun fetchAll(
    @RequestParam("themeId", required = false) themeId: String?
  ): List<RestProduct> {
    return productsService.findAll(themeId).map { product ->
      RestProduct(
        product.id, product.name, product.description, product.content, product.contentType.value
      )
    }
  }
}
