package com.andreygorshkov24

import com.andreygorshkov24.ThemesFoldersService.Companion.ROOT_THEME_FOLDER_ID
import com.andreygorshkov24.ThemesService.Companion.ROOT_THEME_ID
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FakeInit {

  @Bean
  fun themesFoldersService(): ThemesFoldersService {
    val rootThemeFolder = FakeThemeFolder(ROOT_THEME_FOLDER_ID, "Theme folder #$ROOT_THEME_FOLDER_ID", "Theme folder #$ROOT_THEME_FOLDER_ID", 0)

    val themesFolderCount = 3
    val themeFolders = (0 until 3).map { i -> FakeThemeFolder("theme_folder_$i", "Theme folder #$i", "Theme folder #$i", themesFolderCount - i) } + rootThemeFolder

    return FakeThemesFoldersService(themeFolders)
  }

  @Bean
  fun themesService(themesFoldersService: ThemesFoldersService): ThemesService {
    val rootThemeFolder = themesFoldersService.find(ROOT_THEME_FOLDER_ID)!!

    val rootTheme = FakeTheme(ROOT_THEME_ID, "Theme #$ROOT_THEME_ID", "Theme #$ROOT_THEME_ID", rootThemeFolder.id, 0)

    val themesCount = 9
    val themes = (0 until themesCount).map { i -> FakeTheme("theme_$i", "Theme #$i", "Theme #$i", rootThemeFolder.id, themesCount - i) } + rootTheme

    return FakeThemesService(themes)
  }

  @Bean
  fun productsService(themesService: ThemesService): ProductsService {
    val rootTheme = themesService.find(ROOT_THEME_ID)!!

    val productsCount = 27
    val products =
      (0 until productsCount).map { i -> FakeProduct("product_$i", "Product #$i", "Product #$i", rootTheme.id, productsCount - i, "<p>Lorem ipsum dollar #$i</p>", ProductContentType.TEXT_HTML) }

    return FakeProductsService(products)
  }
}
