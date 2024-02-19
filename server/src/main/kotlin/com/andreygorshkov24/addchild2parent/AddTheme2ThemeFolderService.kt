package com.andreygorshkov24.addchild2parent

interface AddTheme2ThemeFolderService {

  fun addTheme2ThemeFolder(themeId: String, themeFolderId: String): AddTheme2ThemeFolderResult

  sealed class AddTheme2ThemeFolderResult(val error: String?) {

    class ThemeFolderDoesNotExistAddTheme2ThemeFolderResult(error: String) : AddTheme2ThemeFolderResult(error)

    class ThemeDoesNotExistAddTheme2ThemeFolderResult(error: String) : AddTheme2ThemeFolderResult(error)

    class SucceededAddTheme2ThemeFolderResult() : AddTheme2ThemeFolderResult(null)
  }
}
