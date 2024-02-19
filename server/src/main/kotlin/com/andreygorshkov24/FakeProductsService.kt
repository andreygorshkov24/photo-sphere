package com.andreygorshkov24

class FakeProductsService(
  private val products: List<Product>
) : ProductsService {

  override fun findAll(themeId: String?): List<Product> {
    return themeId?.let {
      products.filter { product -> product.parentId == themeId }.sortedBy { product -> product.position }.reversed()
    } ?: products
  }

  override fun find(id: String): Product? {
    return products.firstOrNull { product -> product.id == id }
  }
}
