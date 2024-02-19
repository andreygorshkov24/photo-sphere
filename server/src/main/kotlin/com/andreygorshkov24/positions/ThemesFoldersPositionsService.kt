package com.andreygorshkov24.positions

interface ThemesFoldersPositionsService {

  fun toUp(themeId: String): ThemesFoldersPositionsResult

  fun toDown(themeId: String): ThemesFoldersPositionsResult

  sealed class ThemesFoldersPositionsResult(val error: String?) {

    class ThemeFolderDoesNotExistThemesFoldersPositionsResult(error: String) :
      ThemesFoldersPositionsResult(error)

    class SucceedThemesFoldersPositionsResult() : ThemesFoldersPositionsResult(null)
  }
}
