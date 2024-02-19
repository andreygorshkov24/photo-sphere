package com.andreygorshkov24

data class FakeThemeFolder(
  override var id: String,
  override var name: String,
  override var description: String,
  override var position: Int,
) : ThemeFolder {
  override var parentId: String?
    get() = null
    set(value) {}
}

data class FakeTheme(
  override var id: String, override var name: String, override var description: String, var themeFolderId: String?, var themeFolderPosition: Int
) : Theme {
  override var parentId: String?
    get() = themeFolderId
    set(value) {
      this.themeFolderId = value
    }
  override var position: Int
    get() = themeFolderPosition
    set(value) {
      this.themeFolderPosition = value
    }
}

data class FakeProduct(
  override var id: String,
  override var name: String,
  override var description: String,
  var themeId: String?,
  var themePosition: Int,
  override var content: String,
  override var contentType: ProductContentType
) : Product {
  override var parentId: String?
    get() = themeId
    set(parentId) {
      themeId = parentId
    }
  override var position: Int
    get() = themePosition
    set(position) {
      themePosition = position
    }
}
