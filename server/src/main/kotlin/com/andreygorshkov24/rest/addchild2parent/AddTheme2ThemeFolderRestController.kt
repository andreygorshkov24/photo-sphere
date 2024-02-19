package com.andreygorshkov24.rest.addchild2parent

import com.andreygorshkov24.addchild2parent.AddTheme2ThemeFolderService
import com.andreygorshkov24.addchild2parent.AddTheme2ThemeFolderService.AddTheme2ThemeFolderResult.*
import com.andreygorshkov24.rest.RestError
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletResponse.*
import org.springframework.web.bind.annotation.*

@RestController
class AddTheme2ThemeFolderRestController(
  private val response: HttpServletResponse, private val addTheme2ThemeFolderService: AddTheme2ThemeFolderService
) {

  @CrossOrigin
  @PatchMapping("/themesfolders/themes/add")
  fun addTheme2ThemeFolder(
    @RequestParam("themeId", required = true) themeId: String, @RequestParam("themeFolderId", required = true) themeFolderId: String
  ): RestError? {
    if (themeId.isBlank()) {
      response.status = SC_BAD_REQUEST
      return RestError("theme id is blank")
    }

    if (themeFolderId.isBlank()) {
      response.status = SC_BAD_REQUEST
      return RestError("theme folder id is blank")
    }

    val addTheme2ThemeFolderResult = addTheme2ThemeFolderService.addTheme2ThemeFolder(themeId, themeFolderId)
    return when (addTheme2ThemeFolderResult) {
      is ThemeFolderDoesNotExistAddTheme2ThemeFolderResult -> {
        response.status = SC_NOT_FOUND
        return RestError(addTheme2ThemeFolderResult.error!!)
      }

      is ThemeDoesNotExistAddTheme2ThemeFolderResult -> {
        response.status = SC_NOT_FOUND
        return RestError(addTheme2ThemeFolderResult.error!!)
      }

      is SucceededAddTheme2ThemeFolderResult -> {
        response.status = SC_OK
        null
      }
    }
  }
}
