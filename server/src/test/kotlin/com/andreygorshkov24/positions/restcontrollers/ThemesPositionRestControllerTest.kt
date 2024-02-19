package com.andreygorshkov24.positions.restcontrollers

import com.andreygorshkov24.rest.RestError
import com.andreygorshkov24.ObjectMapperInit
import com.andreygorshkov24.positions.ThemesPositionsService
import com.andreygorshkov24.positions.ThemesPositionsService.ThemesPositionsResult.ThemeDoesNotExistThemesPositionsResult
import com.andreygorshkov24.rest.positions.ThemesPositionRestController
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
@ContextConfiguration(classes = [ThemesPositionRestController::class, ObjectMapperInit::class])
class ThemesPositionRestControllerTest {

  @Autowired
  private lateinit var objectMapper: ObjectMapper

  @Autowired
  private lateinit var mockMvc: MockMvc

  @MockBean
  private lateinit var themesPositionsService: ThemesPositionsService

  @Test
  fun `test toUp when theme id is blank`() {
    val uri = URI.create("/themes/position/up?themeId=")
    mockMvc.patch(uri).andExpect {
      status { isBadRequest() }
      content {
        contentType(MediaType.APPLICATION_JSON)
        json(objectMapper.writeValueAsString(RestError("theme id is blank")))
      }
    }
  }

  @Test
  fun `test toDown when theme id is blank`() {
    val uri = URI.create("/themes/position/down?themeId=")
    mockMvc.patch(uri).andExpect {
      status { isBadRequest() }
      content {
        contentType(MediaType.APPLICATION_JSON)
        json(objectMapper.writeValueAsString(RestError("theme id is blank")))
      }
    }
  }

  @Test
  fun `test toUp when theme does not exist`() {
    val randomThemeId = "42"
    val restError = RestError("theme $randomThemeId does not exist")

    `when`(themesPositionsService.toUp(randomThemeId)).thenReturn(ThemeDoesNotExistThemesPositionsResult(restError.error))

    val uri = URI.create("/themes/position/up?themeId=$randomThemeId")
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
    val randomThemeId = "42"
    val restError = RestError("theme $randomThemeId was does not exist")

    `when`(themesPositionsService.toDown(randomThemeId)).thenReturn(ThemeDoesNotExistThemesPositionsResult(restError.error))

    val uri = URI.create("/themes/position/down?themeId=$randomThemeId")
    mockMvc.patch(uri).andExpect {
      status { isNotFound() }
      content {
        contentType(MediaType.APPLICATION_JSON)
        json(objectMapper.writeValueAsString(restError))
      }
    }
  }
}
