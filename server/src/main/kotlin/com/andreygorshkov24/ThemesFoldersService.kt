package com.andreygorshkov24

interface ThemesFoldersService {

  fun findAll(): List<ThemeFolder>

  fun find(id: String): ThemeFolder?

  companion object {
    const val ROOT_THEME_FOLDER_ID = "root"
  }
}
