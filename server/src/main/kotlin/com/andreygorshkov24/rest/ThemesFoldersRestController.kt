package com.andreygorshkov24.rest

import com.andreygorshkov24.ThemesFoldersService
import org.springframework.web.bind.annotation.*

@RestController
class ThemesFoldersRestController(
  private val themesFoldersService: ThemesFoldersService
) {

  @CrossOrigin
  @GetMapping("/themesfolders")
  fun fetchAll(): List<RestThemeFolder> {
    return themesFoldersService.findAll().map { themeFolder ->
      RestThemeFolder(themeFolder.id, themeFolder.name, themeFolder.description)
    }
  }
}
