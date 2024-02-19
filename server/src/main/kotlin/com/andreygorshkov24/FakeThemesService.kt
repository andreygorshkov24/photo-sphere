package com.andreygorshkov24

class FakeThemesService(
  private val themes: List<Theme>
) : ThemesService {

  override fun findAll(themeFolderId: String?): List<Theme> {
    return themeFolderId?.let {
      themes.filter { theme -> theme.parentId == themeFolderId }.sortedBy { theme -> theme.position }.reversed()
    } ?: themes
  }

  override fun find(id: String): Theme? {
    return themes.firstOrNull { theme -> theme.id == id }
  }
}
