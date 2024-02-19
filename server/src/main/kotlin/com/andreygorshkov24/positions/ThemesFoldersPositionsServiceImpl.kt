package com.andreygorshkov24.positions

import com.andreygorshkov24.ThemeFolder
import com.andreygorshkov24.ThemesFoldersService
import com.andreygorshkov24.positions.ThemesFoldersPositionsService.ThemesFoldersPositionsResult
import com.andreygorshkov24.positions.ThemesFoldersPositionsService.ThemesFoldersPositionsResult.SucceedThemesFoldersPositionsResult
import com.andreygorshkov24.positions.ThemesFoldersPositionsService.ThemesFoldersPositionsResult.ThemeFolderDoesNotExistThemesFoldersPositionsResult
import org.springframework.stereotype.Service

@Service
class ThemesFoldersPositionsServiceImpl(
  private val themesFoldersService: ThemesFoldersService
) : ThemesFoldersPositionsService, PositionsService<ThemeFolder>() {

  override fun toUp(themeFolderId: String): ThemesFoldersPositionsResult {
    val result = targetUp(themeFolderId)
    return handleResult(themeFolderId, result)
  }

  override fun toDown(themeFolderId: String): ThemesFoldersPositionsResult {
    val result = targetDown(themeFolderId)
    return handleResult(themeFolderId, result)
  }

  private fun handleResult(
    themeFolderId: String, result: Result
  ): ThemesFoldersPositionsResult {
    return when (result) {
      Result.CHILD_NOT_FOUND -> ThemeFolderDoesNotExistThemesFoldersPositionsResult("theme folder $themeFolderId does not exist")
      Result.SUCCEEDED -> SucceedThemesFoldersPositionsResult()
    }
  }

  override fun findTarget(targetId: String): ThemeFolder? = themesFoldersService.find(targetId)

  override fun findTargetRelatives(parentId: String?): Iterable<ThemeFolder> = themesFoldersService.findAll()
}