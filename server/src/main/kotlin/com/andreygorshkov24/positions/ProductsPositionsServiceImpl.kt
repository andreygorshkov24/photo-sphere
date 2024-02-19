package com.andreygorshkov24.positions

import com.andreygorshkov24.Product
import com.andreygorshkov24.ProductsService
import com.andreygorshkov24.positions.ProductsPositionsService.ProductsPositionsResult
import com.andreygorshkov24.positions.ProductsPositionsService.ProductsPositionsResult.ProductDoesNotExistProductsPositionsResult
import com.andreygorshkov24.positions.ProductsPositionsService.ProductsPositionsResult.SucceedProductsPositionsResult
import com.andreygorshkov24.positions.Result.CHILD_NOT_FOUND
import com.andreygorshkov24.positions.Result.SUCCEEDED
import org.springframework.stereotype.Service

@Service
class ProductsPositionsServiceImpl(
  private val productsService: ProductsService
) : ProductsPositionsService, PositionsService<Product>() {

  override fun toUp(productId: String): ProductsPositionsResult {
    val result = targetUp(productId)
    return handleResult(productId, result)
  }

  override fun toDown(productId: String): ProductsPositionsResult {
    val result = targetDown(productId)
    return handleResult(productId, result)
  }

  private fun handleResult(
    productId: String, result: Result
  ): ProductsPositionsResult {
    return when (result) {
      CHILD_NOT_FOUND -> ProductDoesNotExistProductsPositionsResult("product $productId does not exist")
      SUCCEEDED -> SucceedProductsPositionsResult()
    }
  }

  override fun findTarget(targetId: String): Product? = productsService.find(targetId)

  override fun findTargetRelatives(parentId: String?): Iterable<Product> = productsService.findAll(parentId)
}