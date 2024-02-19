package com.andreygorshkov24.positions

interface ProductsPositionsService {

  fun toUp(productId: String): ProductsPositionsResult

  fun toDown(productId: String): ProductsPositionsResult

  sealed class ProductsPositionsResult(val error: String?) {

    class ProductDoesNotExistProductsPositionsResult(error: String) : ProductsPositionsResult(error)

    class SucceedProductsPositionsResult() : ProductsPositionsResult(null)
  }
}
