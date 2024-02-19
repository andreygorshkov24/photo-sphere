package com.andreygorshkov24.rest.upload

import jakarta.servlet.http.HttpServletRequest

interface UploadService {

  fun upload(request: UploadRequest): UploadResult

  data class UploadRequest(val request: HttpServletRequest)

  sealed class UploadResult(val uploadedFilenames: List<String>, val error: String?) {

    class SucceedUploadResult(fileNames: List<String>) : UploadResult(fileNames, null)

    class UnexpectedContentType(message: String) : UploadResult(emptyList(), message)

    class ExceededFileUploadSize(message: String) : UploadResult(emptyList(), message)

    class UnexpectedError(message: String) : UploadResult(emptyList(), message)
  }
}
