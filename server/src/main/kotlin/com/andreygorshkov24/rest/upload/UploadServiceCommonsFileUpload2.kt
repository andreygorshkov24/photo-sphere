package com.andreygorshkov24.rest.upload

import com.andreygorshkov24.rest.upload.UploadService.UploadRequest
import com.andreygorshkov24.rest.upload.UploadService.UploadResult
import com.andreygorshkov24.rest.upload.UploadService.UploadResult.*
import org.apache.commons.fileupload2.core.FileItemInput
import org.apache.commons.fileupload2.core.FileUploadSizeException
import org.apache.commons.fileupload2.jakarta.JakartaServletFileUpload
import org.apache.commons.fileupload2.jakarta.JakartaServletFileUpload.isMultipartContent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.file.Files
import java.nio.file.LinkOption.NOFOLLOW_LINKS
import java.nio.file.Paths
import java.nio.file.StandardOpenOption.CREATE_NEW
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
import kotlin.io.path.exists
import kotlin.io.path.isDirectory

class UploadServiceCommonsFileUpload2(
  private val baseDir: String, private val contentType2Extension: Map<String, String>, private val fileSizeMax_: Long
) : UploadService {

  override fun upload(request: UploadRequest): UploadResult {
    val baseDirPath = Paths.get(baseDir)
    if (!baseDirPath.exists(NOFOLLOW_LINKS) || !baseDirPath.isDirectory()) {
      return UnexpectedError("base directory does not exist or base directory is not a directory")
    }

    if (!isMultipartContent(request.request)) {
      return UnexpectedContentType("unexpected content type: ${request.request.contentType}, use only multipart/form-data")
    }

    try {
      val uploadedFilenames = mutableListOf<String>()

      val itemIterator = JakartaServletFileUpload().apply { this.fileSizeMax = fileSizeMax_ }.getItemIterator(request.request)

      while (itemIterator.hasNext()) {
        val next = itemIterator.next()
        if (next.contentType != null && !contentType2Extension.keys.contains(next.contentType)) {
          return UnexpectedContentType("unexpected content type: ${next.contentType}, use only ${contentType2Extension.keys.joinToString()}")
        }

        val uploadedFilename = createUploadedFilename(next.name, next.contentType)

        createFile(next, uploadedFilename)

        uploadedFilenames.add(uploadedFilename)
      }
      return SucceedUploadResult(uploadedFilenames)
    } catch (e: FileUploadSizeException) {
      LOGGER.trace("exceeded file upload size: ${e.message}", e)
      return ExceededFileUploadSize(e.message ?: "exceeded file upload size")
    } catch (e: IOException) {
      LOGGER.trace("unexpected error: ${e.message}", e)
      return UnexpectedError(e.message ?: "unexpected error")
    }
  }

  private fun createUploadedFilename(
    name: String?, contentType: String = TEXT_HTML_CONTENT_TYPE
  ): String {
    val timestampedName = if (name != null) "$name-${timestamp()}" else timestamp()
    val extension = contentType2Extension[contentType]
    return "$timestampedName.$extension"
  }

  private fun createFile(fileItemInput: FileItemInput, filename: String) {
    Files.newOutputStream(Paths.get(baseDir, filename), CREATE_NEW).use { outputStream ->
      fileItemInput.inputStream.transferTo(outputStream)
    }
  }

  private fun timestamp(): String = TIMESTAMP_FORMATTER.format(now())

  companion object {
    const val TEXT_HTML_CONTENT_TYPE = "text/html"
    val LOGGER: Logger = LoggerFactory.getLogger(UploadServiceCommonsFileUpload2::class.java)
    val TIMESTAMP_FORMATTER: DateTimeFormatter = ISO_LOCAL_DATE_TIME
  }
}
