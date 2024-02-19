package com.andreygorshkov24.addchild2parent.restcontrollers

import com.andreygorshkov24.rest.RestError
import com.andreygorshkov24.ObjectMapperInit
import com.andreygorshkov24.addchild2parent.AddTheme2ThemeFolderService
import com.andreygorshkov24.addchild2parent.AddTheme2ThemeFolderService.AddTheme2ThemeFolderResult.ThemeDoesNotExistAddTheme2ThemeFolderResult
import com.andreygorshkov24.addchild2parent.AddTheme2ThemeFolderService.AddTheme2ThemeFolderResult.ThemeFolderDoesNotExistAddTheme2ThemeFolderResult
import com.andreygorshkov24.rest.addchild2parent.AddTheme2ThemeFolderRestController
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.patch
import java.net.URI

@WebMvcTest
@ContextConfiguration(classes = [AddTheme2ThemeFolderRestController::class, ObjectMapperInit::class])
class AddTheme2ThemeFolderRestControllerTest {

  @Autowired
  private lateinit var objectMapper: ObjectMapper

  @Autowired
  private lateinit var mockMvc: MockMvc

  @MockBean
  private lateinit var addTheme2ThemeFolderService: AddTheme2ThemeFolderService

  @Test
  fun `test addTheme2ThemeFolder when theme id is blank`() {
    val uri = URI.create("/themesfolders/themes/add?themeId=&themeFolderId=42")
    mockMvc.patch(uri).andExpect {
      status { isBadRequest() }
      content {
        contentType(MediaType.APPLICATION_JSON)
        json(objectMapper.writeValueAsString(RestError("theme id is blank")))
      }
    }
  }

  @Test
  fun `test addTheme2ThemeFolder when themeFolder id is blank`() {
    val uri = URI.create("/themesfolders/themes/add?themeId=42&themeFolderId=")
    mockMvc.patch(uri).andExpect {
      status { isBadRequest() }
      content {
        contentType(MediaType.APPLICATION_JSON)
        json(objectMapper.writeValueAsString(RestError("theme folder id is blank")))
      }
    }
  }

  @Test
  fun `test addTheme2ThemeFolder when theme does not exist`() {
    val randomThemeId = "42"
    val restError = RestError("theme $randomThemeId does not exist")

    `when`(addTheme2ThemeFolderService.addTheme2ThemeFolder(randomThemeId, "42")).thenReturn(
      ThemeDoesNotExistAddTheme2ThemeFolderResult(restError.error)
    )

    val uri = URI.create("/themesfolders/themes/add?themeId=$randomThemeId&themeFolderId=42")
    mockMvc.patch(uri).andExpect {
      status { isNotFound() }
      content {
        contentType(MediaType.APPLICATION_JSON)
        json(objectMapper.writeValueAsString(restError))
      }
    }
  }

  @Test
  fun `test addTheme2ThemeFolder when themeFolder does not exist`() {
    val randomThemeFolderId = "42"
    val restError = RestError("theme folder $randomThemeFolderId does not exist")

    `when`(
      addTheme2ThemeFolderService.addTheme2ThemeFolder(
        "42", randomThemeFolderId
      )
    ).thenReturn(ThemeFolderDoesNotExistAddTheme2ThemeFolderResult(restError.error))

    val uri = URI.create("/themesfolders/themes/add?themeId=42&themeFolderId=$randomThemeFolderId")
    mockMvc.patch(uri).andExpect {
      status { isNotFound() }
      content {
        contentType(MediaType.APPLICATION_JSON)
        json(objectMapper.writeValueAsString(restError))
      }
    }
  }
}
