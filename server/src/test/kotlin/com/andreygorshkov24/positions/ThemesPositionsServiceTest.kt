package com.andreygorshkov24.positions

import com.andreygorshkov24.*
import com.andreygorshkov24.ThemesFoldersService.Companion.ROOT_THEME_FOLDER_ID
import com.andreygorshkov24.positions.ThemesPositionsService.ThemesPositionsResult.ThemeDoesNotExistThemesPositionsResult
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ThemesPositionsServiceTest {

  private lateinit var themes: List<Theme>
  private lateinit var themesService: ThemesService

  private lateinit var themesPositionsService: ThemesPositionsService

  @BeforeEach
  fun beforeEach() {
    val rootThemeFolder = FakeThemeFolder(ROOT_THEME_FOLDER_ID, "Theme folder #$ROOT_THEME_FOLDER_ID", "Theme folder #$ROOT_THEME_FOLDER_ID", 0)

    val themesCount = 5
    themes = (0 until themesCount).map { i -> FakeTheme("theme_$i", "Theme #$i", "Theme #$i", rootThemeFolder.id, themesCount - i - 1) }
    themesService = FakeThemesService(themes)

    themesPositionsService = ThemesPositionsServiceImpl(themesService)
  }

  @Test
  fun `test toUp and toDown together`() {
    repeat(4) { themesPositionsService.toDown(themes[0].id) }
    repeat(3) { themesPositionsService.toDown(themes[1].id) }
    repeat(2) { themesPositionsService.toDown(themes[2].id) }
    repeat(1) { themesPositionsService.toDown(themes[3].id) }

    assertEquals(4, themes[4].position)
    assertEquals(3, themes[3].position)
    assertEquals(2, themes[2].position)
    assertEquals(1, themes[1].position)
    assertEquals(0, themes[0].position)

    repeat(4) { themesPositionsService.toUp(themes[0].id) }
    repeat(3) { themesPositionsService.toUp(themes[1].id) }
    repeat(2) { themesPositionsService.toUp(themes[2].id) }
    repeat(1) { themesPositionsService.toUp(themes[3].id) }

    assertEquals(4, themes[0].position)
    assertEquals(3, themes[1].position)
    assertEquals(2, themes[2].position)
    assertEquals(1, themes[3].position)
    assertEquals(0, themes[4].position)
  }

  @Test
  fun `test toUp on bound`() {
    themesPositionsService.toUp(themes[0].id)

    assertEquals(4, themes[0].position)
    assertEquals(3, themes[1].position)
    assertEquals(2, themes[2].position)
    assertEquals(1, themes[3].position)
    assertEquals(0, themes[4].position)
  }

  @Test
  fun `test toDown on bound`() {
    themesPositionsService.toDown(themes[4].id)

    assertEquals(4, themes[0].position)
    assertEquals(3, themes[1].position)
    assertEquals(2, themes[2].position)
    assertEquals(1, themes[3].position)
    assertEquals(0, themes[4].position)
  }

  @Test
  fun `test toUp`() {
    val targetThemeIndex = 0

    themesPositionsService.toDown(themes[targetThemeIndex].id)

    assertEquals(4, themes[1].position)
    assertEquals(3, themes[targetThemeIndex].position)
    assertEquals(2, themes[2].position)
    assertEquals(1, themes[3].position)
    assertEquals(0, themes[4].position)

    themesPositionsService.toDown(themes[targetThemeIndex].id)

    assertEquals(4, themes[1].position)
    assertEquals(3, themes[2].position)
    assertEquals(2, themes[targetThemeIndex].position)
    assertEquals(1, themes[3].position)
    assertEquals(0, themes[4].position)

    themesPositionsService.toDown(themes[targetThemeIndex].id)

    assertEquals(4, themes[1].position)
    assertEquals(3, themes[2].position)
    assertEquals(2, themes[3].position)
    assertEquals(1, themes[targetThemeIndex].position)
    assertEquals(0, themes[4].position)

    themesPositionsService.toDown(themes[0].id)

    assertEquals(4, themes[1].position)
    assertEquals(3, themes[2].position)
    assertEquals(2, themes[3].position)
    assertEquals(1, themes[4].position)
    assertEquals(0, themes[targetThemeIndex].position)
  }

  @Test
  fun `test toDown`() {
    val targetThemeIndex = 4

    themesPositionsService.toUp(themes[targetThemeIndex].id)

    assertEquals(4, themes[0].position)
    assertEquals(3, themes[1].position)
    assertEquals(2, themes[2].position)
    assertEquals(1, themes[targetThemeIndex].position)
    assertEquals(0, themes[3].position)

    themesPositionsService.toUp(themes[targetThemeIndex].id)

    assertEquals(4, themes[0].position)
    assertEquals(3, themes[1].position)
    assertEquals(2, themes[targetThemeIndex].position)
    assertEquals(1, themes[2].position)
    assertEquals(0, themes[3].position)

    themesPositionsService.toUp(themes[targetThemeIndex].id)

    assertEquals(4, themes[0].position)
    assertEquals(3, themes[targetThemeIndex].position)
    assertEquals(2, themes[1].position)
    assertEquals(1, themes[2].position)
    assertEquals(0, themes[3].position)

    themesPositionsService.toUp(themes[targetThemeIndex].id)

    assertEquals(4, themes[targetThemeIndex].position)
    assertEquals(3, themes[0].position)
    assertEquals(2, themes[1].position)
    assertEquals(1, themes[2].position)
    assertEquals(0, themes[3].position)
  }

  @Test
  fun `test toUp when theme does not exist`() {
    val randomThemeId = "42"

    val result = themesPositionsService.toUp(randomThemeId)

    assertEquals(ThemeDoesNotExistThemesPositionsResult::class.java, result::class.java)
    assertEquals("theme $randomThemeId does not exist", result.error)
  }

  @Test
  fun `test toDown when theme does not exist`() {
    val randomThemeId = "42"

    val result = themesPositionsService.toDown(randomThemeId)

    assertEquals(ThemeDoesNotExistThemesPositionsResult::class.java, result::class.java)
    assertEquals("theme $randomThemeId does not exist", result.error)
  }
}
