package com.andreygorshkov24

class FakeThemesFoldersService(
  private val themeFolders: List<ThemeFolder>
) : ThemesFoldersService {

  override fun findAll(): List<ThemeFolder> {
    return themeFolders.sortedBy { themeFolder -> themeFolder.position }.reversed()
  }

  override fun find(id: String): ThemeFolder? {
    return themeFolders.firstOrNull { themeFolder -> themeFolder.id == id }
  }
}
