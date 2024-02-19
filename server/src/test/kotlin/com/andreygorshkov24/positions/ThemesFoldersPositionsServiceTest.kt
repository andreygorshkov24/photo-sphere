package com.andreygorshkov24.positions

import com.andreygorshkov24.*
import com.andreygorshkov24.positions.ThemesFoldersPositionsService.ThemesFoldersPositionsResult.ThemeFolderDoesNotExistThemesFoldersPositionsResult
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ThemesFoldersPositionsServiceTest {

  private lateinit var themesFolders: List<ThemeFolder>
  private lateinit var themesFoldersService: ThemesFoldersService

  private lateinit var themesFoldersPositionsService: ThemesFoldersPositionsService

  @BeforeEach
  fun beforeEach() {
    val themesFoldersCount = 5
    themesFolders = (0 until themesFoldersCount).map { i -> FakeThemeFolder("theme_folder_$i", "Theme folder #$i", "Theme folder #$i", themesFoldersCount - i - 1) }
    themesFoldersService = FakeThemesFoldersService(themesFolders)

    themesFoldersPositionsService = ThemesFoldersPositionsServiceImpl(themesFoldersService)
  }

  @Test
  fun `test toUp and toDown together`() {
    repeat(4) { themesFoldersPositionsService.toDown(themesFolders[0].id) }
    repeat(3) { themesFoldersPositionsService.toDown(themesFolders[1].id) }
    repeat(2) { themesFoldersPositionsService.toDown(themesFolders[2].id) }
    repeat(1) { themesFoldersPositionsService.toDown(themesFolders[3].id) }

    assertEquals(4, themesFolders[4].position)
    assertEquals(3, themesFolders[3].position)
    assertEquals(2, themesFolders[2].position)
    assertEquals(1, themesFolders[1].position)
    assertEquals(0, themesFolders[0].position)

    repeat(4) { themesFoldersPositionsService.toUp(themesFolders[0].id) }
    repeat(3) { themesFoldersPositionsService.toUp(themesFolders[1].id) }
    repeat(2) { themesFoldersPositionsService.toUp(themesFolders[2].id) }
    repeat(1) { themesFoldersPositionsService.toUp(themesFolders[3].id) }

    assertEquals(4, themesFolders[0].position)
    assertEquals(3, themesFolders[1].position)
    assertEquals(2, themesFolders[2].position)
    assertEquals(1, themesFolders[3].position)
    assertEquals(0, themesFolders[4].position)
  }

  @Test
  fun `test toUp on bound`() {
    themesFoldersPositionsService.toUp(themesFolders[0].id)

    assertEquals(4, themesFolders[0].position)
    assertEquals(3, themesFolders[1].position)
    assertEquals(2, themesFolders[2].position)
    assertEquals(1, themesFolders[3].position)
    assertEquals(0, themesFolders[4].position)
  }

  @Test
  fun `test toDown on bound`() {
    themesFoldersPositionsService.toDown(themesFolders[4].id)

    assertEquals(4, themesFolders[0].position)
    assertEquals(3, themesFolders[1].position)
    assertEquals(2, themesFolders[2].position)
    assertEquals(1, themesFolders[3].position)
    assertEquals(0, themesFolders[4].position)
  }

  @Test
  fun `test toUp`() {
    val targetThemeIndex = 0

    themesFoldersPositionsService.toDown(themesFolders[targetThemeIndex].id)

    assertEquals(4, themesFolders[1].position)
    assertEquals(3, themesFolders[targetThemeIndex].position)
    assertEquals(2, themesFolders[2].position)
    assertEquals(1, themesFolders[3].position)
    assertEquals(0, themesFolders[4].position)

    themesFoldersPositionsService.toDown(themesFolders[targetThemeIndex].id)

    assertEquals(4, themesFolders[1].position)
    assertEquals(3, themesFolders[2].position)
    assertEquals(2, themesFolders[targetThemeIndex].position)
    assertEquals(1, themesFolders[3].position)
    assertEquals(0, themesFolders[4].position)

    themesFoldersPositionsService.toDown(themesFolders[targetThemeIndex].id)

    assertEquals(4, themesFolders[1].position)
    assertEquals(3, themesFolders[2].position)
    assertEquals(2, themesFolders[3].position)
    assertEquals(1, themesFolders[targetThemeIndex].position)
    assertEquals(0, themesFolders[4].position)

    themesFoldersPositionsService.toDown(themesFolders[0].id)

    assertEquals(4, themesFolders[1].position)
    assertEquals(3, themesFolders[2].position)
    assertEquals(2, themesFolders[3].position)
    assertEquals(1, themesFolders[4].position)
    assertEquals(0, themesFolders[targetThemeIndex].position)
  }

  @Test
  fun `test toDown`() {
    val targetThemeIndex = 4

    themesFoldersPositionsService.toUp(themesFolders[targetThemeIndex].id)

    assertEquals(4, themesFolders[0].position)
    assertEquals(3, themesFolders[1].position)
    assertEquals(2, themesFolders[2].position)
    assertEquals(1, themesFolders[targetThemeIndex].position)
    assertEquals(0, themesFolders[3].position)

    themesFoldersPositionsService.toUp(themesFolders[targetThemeIndex].id)

    assertEquals(4, themesFolders[0].position)
    assertEquals(3, themesFolders[1].position)
    assertEquals(2, themesFolders[targetThemeIndex].position)
    assertEquals(1, themesFolders[2].position)
    assertEquals(0, themesFolders[3].position)

    themesFoldersPositionsService.toUp(themesFolders[targetThemeIndex].id)

    assertEquals(4, themesFolders[0].position)
    assertEquals(3, themesFolders[targetThemeIndex].position)
    assertEquals(2, themesFolders[1].position)
    assertEquals(1, themesFolders[2].position)
    assertEquals(0, themesFolders[3].position)

    themesFoldersPositionsService.toUp(themesFolders[targetThemeIndex].id)

    assertEquals(4, themesFolders[targetThemeIndex].position)
    assertEquals(3, themesFolders[0].position)
    assertEquals(2, themesFolders[1].position)
    assertEquals(1, themesFolders[2].position)
    assertEquals(0, themesFolders[3].position)
  }

  @Test
  fun `test toUp when theme does not exist`() {
    val randomThemeId = "42"

    val result = themesFoldersPositionsService.toUp(randomThemeId)

    assertEquals(ThemeFolderDoesNotExistThemesFoldersPositionsResult::class.java, result::class.java)
    assertEquals("theme folder $randomThemeId does not exist", result.error)
  }

  @Test
  fun `test toDown when theme does not exist`() {
    val randomThemeId = "42"

    val result = themesFoldersPositionsService.toDown(randomThemeId)

    assertEquals(ThemeFolderDoesNotExistThemesFoldersPositionsResult::class.java, result::class.java)
    assertEquals("theme folder $randomThemeId does not exist", result.error)
  }
}
