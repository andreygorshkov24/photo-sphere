package com.andreygorshkov24.rest.positions

import com.andreygorshkov24.positions.ThemesPositionsService
import com.andreygorshkov24.positions.ThemesPositionsService.ThemesPositionsResult
import com.andreygorshkov24.positions.ThemesPositionsService.ThemesPositionsResult.SucceedThemesPositionsResult
import com.andreygorshkov24.positions.ThemesPositionsService.ThemesPositionsResult.ThemeDoesNotExistThemesPositionsResult
import com.andreygorshkov24.rest.RestError
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletResponse.*
import org.springframework.web.bind.annotation.*

@RestController
class ThemesPositionRestController(
  private val response: HttpServletResponse, private val themesPositionsService: ThemesPositionsService
) {

  @CrossOrigin
  @PatchMapping("/themes/position/up")
  fun toUp(
    @RequestParam("themeId", required = true) themeId: String
  ): RestError? {
    if (themeId.isBlank()) {
      response.status = SC_BAD_REQUEST
      return RestError("theme id is blank")
    }

    val themesPositionsResult = themesPositionsService.toUp(themeId)
    return handleThemesPositionsResult(themesPositionsResult)
  }

  @CrossOrigin
  @PatchMapping("/themes/position/down")
  fun toDown(
    @RequestParam("themeId", required = true) themeId: String
  ): RestError? {
    if (themeId.isBlank()) {
      response.status = SC_BAD_REQUEST
      return RestError("theme id is blank")
    }

    val themesPositionsResult = themesPositionsService.toDown(themeId)
    return handleThemesPositionsResult(themesPositionsResult)
  }

  private fun handleThemesPositionsResult(themesPositionsResult: ThemesPositionsResult): RestError? {
    return when (themesPositionsResult) {
      is ThemeDoesNotExistThemesPositionsResult -> {
        response.status = SC_NOT_FOUND
        return RestError(themesPositionsResult.error!!)
      }

      is SucceedThemesPositionsResult -> {
        response.status = SC_OK
        null
      }
    }
  }
}
