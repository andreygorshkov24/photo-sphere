package com.andreygorshkov24

import com.andreygorshkov24.ThemesFoldersService.Companion.ROOT_THEME_FOLDER_ID
import com.andreygorshkov24.ThemesService.Companion.ROOT_THEME_ID
import com.andreygorshkov24.rest.RestTheme
import com.andreygorshkov24.rest.ThemesRestController
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest
@ContextConfiguration(classes = [ThemesRestController::class, ObjectMapperInit::class])
class ThemesRestControllerTest {

  @Autowired
  private lateinit var objectMapper: ObjectMapper

  @Autowired
  private lateinit var mockMvc: MockMvc

  @MockBean
  private lateinit var themesService: ThemesService

  @Test
  fun `test fetchAll`() {
    val rootThemeFolder = FakeThemeFolder(ROOT_THEME_FOLDER_ID, "Theme folder #$ROOT_THEME_FOLDER_ID", "Theme folder #$ROOT_THEME_FOLDER_ID", 0)

    val rootTheme = FakeTheme(ROOT_THEME_ID, "Theme #$ROOT_THEME_ID", "Theme #$ROOT_THEME_ID", rootThemeFolder.id, 0)

    val themes = (0 until 9).map { i -> FakeTheme("theme_$i", "Theme #$i", "Theme #$i", rootThemeFolder.id, i + 1) } + rootTheme

    `when`(themesService.findAll(ROOT_THEME_FOLDER_ID)).thenReturn(themes)

    mockMvc.get("/themes?themeFolderId=$ROOT_THEME_FOLDER_ID").andExpect {
      status { isOk() }
      content {
        contentType(MediaType.APPLICATION_JSON)
        json(objectMapper.writeValueAsString(themes.map { theme ->
          RestTheme(theme.id, theme.name, theme.description)
        }))
      }
    }
  }
}
