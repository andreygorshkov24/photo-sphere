package com.andreygorshkov24.positions

interface ThemesPositionsService {

  fun toUp(themeId: String): ThemesPositionsResult

  fun toDown(themeId: String): ThemesPositionsResult

  sealed class ThemesPositionsResult(val error: String?) {

    class ThemeDoesNotExistThemesPositionsResult(error: String) : ThemesPositionsResult(error)

    class SucceedThemesPositionsResult() : ThemesPositionsResult(null)
  }
}
