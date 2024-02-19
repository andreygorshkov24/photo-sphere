package com.andreygorshkov24.positions.restcontrollers

import com.andreygorshkov24.rest.RestError
import com.andreygorshkov24.ObjectMapperInit
import com.andreygorshkov24.positions.ThemesFoldersPositionsService
import com.andreygorshkov24.positions.ThemesFoldersPositionsService.ThemesFoldersPositionsResult.ThemeFolderDoesNotExistThemesFoldersPositionsResult
import com.andreygorshkov24.rest.positions.ThemesFoldersPositionRestController
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
@ContextConfiguration(classes = [ThemesFoldersPositionRestController::class, ObjectMapperInit::class])
class ThemesFoldersPositionRestControllerTest {

  @Autowired
  private lateinit var objectMapper: ObjectMapper

  @Autowired
  private lateinit var mockMvc: MockMvc

  @MockBean
  private lateinit var themesFoldersPositionsService: ThemesFoldersPositionsService

  @Test
  fun `test toUp when theme id is blank`() {
    val uri = URI.create("/themesfolders/position/up?themeFolderId=")
    mockMvc.patch(uri).andExpect {
      status { isBadRequest() }
      content {
        contentType(MediaType.APPLICATION_JSON)
        json(objectMapper.writeValueAsString(RestError("theme folder id is blank")))
      }
    }
  }

  @Test
  fun `test toDown when theme id is blank`() {
    val uri = URI.create("/themesfolders/position/down?themeFolderId=")
    mockMvc.patch(uri).andExpect {
      status { isBadRequest() }
      content {
        contentType(MediaType.APPLICATION_JSON)
        json(objectMapper.writeValueAsString(RestError("theme folder id is blank")))
      }
    }
  }

  @Test
  fun `test toUp when theme does not exist`() {
    val randomThemeFolderId = "42"
    val restError = RestError("theme $randomThemeFolderId does not exist")

    `when`(themesFoldersPositionsService.toUp(randomThemeFolderId)).thenReturn(ThemeFolderDoesNotExistThemesFoldersPositionsResult(restError.error))

    val uri = URI.create("/themesfolders/position/up?themeFolderId=$randomThemeFolderId")
    mockMvc.patch(uri).andExpect {
      status { isNotFound() }
      content {
        contentType(MediaType.APPLICATION_JSON)
        json(objectMapper.writeValueAsString(restError))
      }
    }
  }

  @Test
  fun `test toDown when theme does not exist`() {
    val randomThemeFolderId = "42"
    val restError = RestError("theme $randomThemeFolderId was does not exist")

    `when`(themesFoldersPositionsService.toDown(randomThemeFolderId)).thenReturn(ThemeFolderDoesNotExistThemesFoldersPositionsResult(restError.error))

    val uri = URI.create("/themesfolders/position/down?themeFolderId=$randomThemeFolderId")
    mockMvc.patch(uri).andExpect {
      status { isNotFound() }
      content {
        contentType(MediaType.APPLICATION_JSON)
        json(objectMapper.writeValueAsString(restError))
      }
    }
  }
}
