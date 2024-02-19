package com.andreygorshkov24

import org.springframework.http.MediaType

interface ThemeFolder : Child, Nameable, Descriptionable

interface Theme : Child, Nameable, Descriptionable

interface Product : Child, Nameable, Descriptionable {
  var content: String
  var contentType: ProductContentType
}

enum class ProductContentType(val value: String) {
  IMAGE_GIF(MediaType.IMAGE_GIF_VALUE), IMAGE_JPG(MediaType.IMAGE_JPEG_VALUE), IMAGE_PNG(MediaType.IMAGE_PNG_VALUE), TEXT_HTML(MediaType.TEXT_HTML_VALUE);
}

interface Child : Identifiable, Positionable

interface Identifiable {
  var id: String
}

interface Nameable {
  var name: String
}

interface Descriptionable {
  var description: String
}

interface Positionable {
  var parentId: String?
  var position: Int
}
