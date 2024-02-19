package com.andreygorshkov24.rest.upload

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType

@ConfigurationProperties("photo-sphere.upload")
@Configuration
class UploadServiceCommonsFileUpload2Configuration {
  val baseDir: String = ""
  val contentType2Ext: Map<String, String> = mapOf(
    MediaType.TEXT_HTML_VALUE to "html",
    MediaType.TEXT_PLAIN_VALUE to "txt",
    MediaType.IMAGE_GIF_VALUE to "gif",
    MediaType.IMAGE_JPEG_VALUE to "jpg",
    MediaType.IMAGE_PNG_VALUE to "png",
  )
  var fileSizeMax: Long = 10L * 1024 * 1024

  @Bean
  fun commonsFileUpload2UploadService(): UploadServiceCommonsFileUpload2 {
    return UploadServiceCommonsFileUpload2(baseDir, contentType2Ext, fileSizeMax)
  }
}
