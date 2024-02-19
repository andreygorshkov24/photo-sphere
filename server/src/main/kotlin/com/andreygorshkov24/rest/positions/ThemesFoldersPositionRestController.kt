package com.andreygorshkov24.rest.positions

import com.andreygorshkov24.positions.ThemesFoldersPositionsService
import com.andreygorshkov24.positions.ThemesFoldersPositionsService.ThemesFoldersPositionsResult
import com.andreygorshkov24.positions.ThemesFoldersPositionsService.ThemesFoldersPositionsResult.SucceedThemesFoldersPositionsResult
import com.andreygorshkov24.positions.ThemesFoldersPositionsService.ThemesFoldersPositionsResult.ThemeFolderDoesNotExistThemesFoldersPositionsResult
import com.andreygorshkov24.rest.RestError
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletResponse.*
import org.springframework.web.bind.annotation.*

@RestController
class ThemesFoldersPositionRestController(
  private val response: HttpServletResponse, private val themesFoldersPositionsService: ThemesFoldersPositionsService
) {

  @CrossOrigin
  @PatchMapping("/themesfolders/position/up")
  fun toUp(
    @RequestParam("themeFolderId", required = true) themeFolderId: String
  ): RestError? {
    if (themeFolderId.isBlank()) {
      response.status = SC_BAD_REQUEST
      return RestError("theme folder id is blank")
    }

    val themesPositionsResult = themesFoldersPositionsService.toUp(themeFolderId)
    return handleThemesPositionsResult(themesPositionsResult)
  }

  @CrossOrigin
  @PatchMapping("/themesfolders/position/down")
  fun toDown(
    @RequestParam("themeFolderId", required = true) themeFolderId: String
  ): RestError? {
    if (themeFolderId.isBlank()) {
      response.status = SC_BAD_REQUEST
      return RestError("theme folder id is blank")
    }

    val themesPositionsResult = themesFoldersPositionsService.toDown(themeFolderId)
    return handleThemesPositionsResult(themesPositionsResult)
  }

  private fun handleThemesPositionsResult(themesFoldersPositionsResult: ThemesFoldersPositionsResult): RestError? {
    return when (themesFoldersPositionsResult) {
      is ThemeFolderDoesNotExistThemesFoldersPositionsResult -> {
        response.status = SC_NOT_FOUND
        return RestError(themesFoldersPositionsResult.error!!)
      }

      is SucceedThemesFoldersPositionsResult -> {
        response.status = SC_OK
        null
      }
    }
  }
}
