package com.andreygorshkov24

interface ProductsService {

  fun findAll(themeId: String? = null): List<Product>

  fun find(id: String): Product?
}
