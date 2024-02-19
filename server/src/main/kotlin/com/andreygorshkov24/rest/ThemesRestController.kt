package com.andreygorshkov24.rest

import com.andreygorshkov24.ThemesService
import org.springframework.web.bind.annotation.*

@RestController
class ThemesRestController(
  private val themesService: ThemesService
) {

  @CrossOrigin
  @GetMapping("/themes")
  fun fetchAll(
    @RequestParam("themeFolderId", required = false) themeFolderId: String?
  ): List<RestTheme> {
    return themesService.findAll(themeFolderId).map { theme ->
      RestTheme(theme.id, theme.name, theme.description)
    }
  }
}
