package com.andreygorshkov24.rest

data class RestThemeFolder(
  val id: String,
  val name: String,
  val description: String,
)

data class RestTheme(
  val id: String,
  val name: String,
  val description: String,
)

data class RestProduct(
  val id: String, val name: String, val description: String, val content: String, val contentType: String
)

data class RestError(val error: String)
