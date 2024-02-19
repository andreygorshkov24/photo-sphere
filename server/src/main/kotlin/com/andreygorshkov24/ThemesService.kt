package com.andreygorshkov24

interface ThemesService {

  fun findAll(themeFolderId: String? = null): List<Theme>

  fun find(id: String): Theme?

  companion object {
    const val ROOT_THEME_ID = "root"
  }
}
