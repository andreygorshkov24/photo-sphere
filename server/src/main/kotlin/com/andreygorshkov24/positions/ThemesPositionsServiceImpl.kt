package com.andreygorshkov24.positions

import com.andreygorshkov24.Theme
import com.andreygorshkov24.ThemesService
import com.andreygorshkov24.positions.Result.CHILD_NOT_FOUND
import com.andreygorshkov24.positions.Result.SUCCEEDED
import com.andreygorshkov24.positions.ThemesPositionsService.ThemesPositionsResult
import com.andreygorshkov24.positions.ThemesPositionsService.ThemesPositionsResult.SucceedThemesPositionsResult
import com.andreygorshkov24.positions.ThemesPositionsService.ThemesPositionsResult.ThemeDoesNotExistThemesPositionsResult
import org.springframework.stereotype.Service

@Service
class ThemesPositionsServiceImpl(
  private val themesService: ThemesService
) : ThemesPositionsService, PositionsService<Theme>() {

  override fun toUp(themeId: String): ThemesPositionsResult {
    val result = targetUp(themeId)
    return handleResult(themeId, result)
  }

  override fun toDown(themeId: String): ThemesPositionsResult {
    val result = targetDown(themeId)
    return handleResult(themeId, result)
  }

  private fun handleResult(themeId: String, result: Result): ThemesPositionsResult {
    return when (result) {
      CHILD_NOT_FOUND -> ThemeDoesNotExistThemesPositionsResult("theme $themeId does not exist")
      SUCCEEDED -> SucceedThemesPositionsResult()
    }
  }

  override fun findTarget(targetId: String): Theme? = themesService.find(targetId)

  override fun findTargetRelatives(parentId: String?): List<Theme> = themesService.findAll(parentId)
}
