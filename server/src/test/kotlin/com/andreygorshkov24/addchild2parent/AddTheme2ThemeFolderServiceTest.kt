package com.andreygorshkov24.addchild2parent

import com.andreygorshkov24.*
import com.andreygorshkov24.ThemesFoldersService.Companion.ROOT_THEME_FOLDER_ID
import com.andreygorshkov24.addchild2parent.AddTheme2ThemeFolderService.AddTheme2ThemeFolderResult.ThemeDoesNotExistAddTheme2ThemeFolderResult
import com.andreygorshkov24.addchild2parent.AddTheme2ThemeFolderService.AddTheme2ThemeFolderResult.ThemeFolderDoesNotExistAddTheme2ThemeFolderResult
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AddTheme2ThemeFolderServiceTest {

  private lateinit var themes: List<Theme>
  private lateinit var themesService: ThemesService

  private lateinit var themeFolders: List<ThemeFolder>
  private lateinit var themesFoldersService: ThemesFoldersService

  private lateinit var addTheme2ThemeFolderService: AddTheme2ThemeFolderService

  @BeforeEach
  fun beforeEach() {
    val rootThemeFolder = FakeThemeFolder(ROOT_THEME_FOLDER_ID, "Theme folder #${ROOT_THEME_FOLDER_ID}", "Theme folder #${ROOT_THEME_FOLDER_ID}", 0)

    val themeFoldersCount = 3
    themeFolders = (0 until themeFoldersCount).map { i -> FakeThemeFolder("theme folder_$i", "Theme folder #$i", "Theme folder #$i", themeFoldersCount - i - 1) } + rootThemeFolder
    themesFoldersService = FakeThemesFoldersService(themeFolders)

    val themesCount = 3
    themes = (0 until themesCount).map { i -> FakeTheme("theme_$i", "Theme #$i", "Theme #$i", rootThemeFolder.id, themesCount - i - 1) }
    themesService = FakeThemesService(themes)

    addTheme2ThemeFolderService = AddTheme2ThemeFolderServiceImpl(themesService, themesFoldersService)
  }

  @Test
  fun `test addTheme2ThemeFolder`() {
    addTheme2ThemeFolderService.addTheme2ThemeFolder(themes[0].id, themeFolders[0].id)
    addTheme2ThemeFolderService.addTheme2ThemeFolder(themes[1].id, themeFolders[1].id)
    addTheme2ThemeFolderService.addTheme2ThemeFolder(themes[2].id, themeFolders[2].id)

    assertEquals(themeFolders[0].id, themes[0].parentId)
    assertEquals(0, themes[0].position)

    assertEquals(themeFolders[1].id, themes[1].parentId)
    assertEquals(0, themes[1].position)

    assertEquals(themeFolders[2].id, themes[2].parentId)
    assertEquals(0, themes[2].position)

    addTheme2ThemeFolderService.addTheme2ThemeFolder(
      themes[0].id, themeFolders[themeFolders.lastIndex].id
    )
    addTheme2ThemeFolderService.addTheme2ThemeFolder(
      themes[1].id, themeFolders[themeFolders.lastIndex].id
    )
    addTheme2ThemeFolderService.addTheme2ThemeFolder(
      themes[2].id, themeFolders[themeFolders.lastIndex].id
    )

    assertEquals(themeFolders[themeFolders.lastIndex].id, themes[0].parentId)
    assertEquals(0, themes[0].position)

    assertEquals(themeFolders[themeFolders.lastIndex].id, themes[1].parentId)
    assertEquals(1, themes[1].position)

    assertEquals(themeFolders[themeFolders.lastIndex].id, themes[2].parentId)
    assertEquals(2, themes[2].position)
  }

  @Test
  fun `test addTheme2ThemeFolder when themeFolder was does not exist`() {
    val randomThemeFolderId = "42"

    val addTheme2ThemeFolderResult = addTheme2ThemeFolderService.addTheme2ThemeFolder(themes[0].id, randomThemeFolderId)

    assertEquals(ThemeFolderDoesNotExistAddTheme2ThemeFolderResult::class.java, addTheme2ThemeFolderResult::class.java)
    assertEquals("theme folder $randomThemeFolderId does not exist", addTheme2ThemeFolderResult.error)
  }

  @Test
  fun `test addTheme2ThemeFolder when theme does not exist`() {
    val randomThemeId = "42"

    val addTheme2ThemeFolderResult = addTheme2ThemeFolderService.addTheme2ThemeFolder(randomThemeId, themeFolders[1].id)

    assertEquals(ThemeDoesNotExistAddTheme2ThemeFolderResult::class.java, addTheme2ThemeFolderResult::class.java)
    assertEquals("theme $randomThemeId does not exist", addTheme2ThemeFolderResult.error)
  }
}
