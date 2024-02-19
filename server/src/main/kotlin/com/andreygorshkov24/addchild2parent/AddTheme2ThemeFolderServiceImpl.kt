package com.andreygorshkov24.addchild2parent

import com.andreygorshkov24.*
import com.andreygorshkov24.addchild2parent.AddTheme2ThemeFolderService.AddTheme2ThemeFolderResult
import com.andreygorshkov24.addchild2parent.AddTheme2ThemeFolderService.AddTheme2ThemeFolderResult.*
import org.springframework.stereotype.Service

@Service
class AddTheme2ThemeFolderServiceImpl(
  private val themesService: ThemesService, private val themesFoldersService: ThemesFoldersService
) : AddTheme2ThemeFolderService, AddChild2ParentService<Theme, ThemeFolder>() {

  override fun addTheme2ThemeFolder(
    themeId: String, themeFolderId: String
  ): AddTheme2ThemeFolderResult {
    return when (addChild2Parent(themeId, themeFolderId)) {
      Result.PARENT_NOT_FOUND -> ThemeFolderDoesNotExistAddTheme2ThemeFolderResult("theme folder $themeFolderId does not exist")
      Result.CHILD_NOT_FOUND -> ThemeDoesNotExistAddTheme2ThemeFolderResult("theme $themeId does not exist")
      Result.SUCCEEDED -> SucceededAddTheme2ThemeFolderResult()
    }
  }

  override fun findParent(parentId: String): ThemeFolder? = themesFoldersService.find(parentId)

  override fun findChildren(): Iterable<Theme> = themesService.findAll()
}
