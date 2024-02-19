package com.andreygorshkov24.rest.upload

import com.andreygorshkov24.rest.upload.UploadService.UploadRequest
import com.andreygorshkov24.rest.upload.UploadService.UploadResult.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletResponse.*
import org.springframework.web.bind.annotation.*

@RestController
class UploadRestController(
  private val uploadService: UploadService, private val request: HttpServletRequest, private val response: HttpServletResponse
) {

  @CrossOrigin
  @PostMapping("/api/v1/uploads")
  fun upload(): Map<String, Any?> {
    return when (val result = uploadService.upload(UploadRequest(request))) {
      is SucceedUploadResult -> {
        response.status = SC_CREATED
        mapOf("uploadedFilenames" to result.uploadedFilenames)
      }

      is UnexpectedContentType -> {
        error(SC_BAD_REQUEST, result.error!!)
      }

      is ExceededFileUploadSize -> {
        error(SC_BAD_REQUEST, result.error!!)
      }

      is UnexpectedError -> {
        error(SC_INTERNAL_SERVER_ERROR, result.error!!)
      }
    }
  }

  private fun error(status: Int, error: String): Map<String, Any?> {
    response.status = status
    return mapOf("error" to error)
  }
}
