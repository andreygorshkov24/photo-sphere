package com.andreygorshkov24

import com.andreygorshkov24.ThemesFoldersService.Companion.ROOT_THEME_FOLDER_ID
import com.andreygorshkov24.rest.RestThemeFolder
import com.andreygorshkov24.rest.ThemesFoldersRestController
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
@ContextConfiguration(classes = [ThemesFoldersRestController::class, ObjectMapperInit::class])
class ThemesFoldersRestControllerTest {

  @Autowired
  private lateinit var objectMapper: ObjectMapper

  @Autowired
  private lateinit var mockMvc: MockMvc

  @MockBean
  private lateinit var themesFoldersService: ThemesFoldersService

  @Test
  fun `test fetchAll`() {
    val rootThemeFolder = FakeThemeFolder(ROOT_THEME_FOLDER_ID, "Theme folder #$ROOT_THEME_FOLDER_ID", "Theme folder #$ROOT_THEME_FOLDER_ID", 0)

    val themeFolders = (0 until 3).map { i -> FakeThemeFolder("theme folder_$i", "Theme folder #$i", "Theme folder #$i", 0) } + rootThemeFolder

    `when`(themesFoldersService.findAll()).thenReturn(themeFolders)

    mockMvc.get("/themesfolders").andExpect {
      status { isOk() }
      content {
        contentType(MediaType.APPLICATION_JSON)
        json(objectMapper.writeValueAsString(themeFolders.map { themeFolder ->
          RestThemeFolder(themeFolder.id, themeFolder.name, themeFolder.description)
        }))
      }
    }
  }
}
