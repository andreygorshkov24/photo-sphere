package com.andreygorshkov24

import com.andreygorshkov24.rest.upload.UploadRestController
import com.andreygorshkov24.rest.upload.UploadServiceCommonsFileUpload2Configuration
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest

@WebMvcTest(UploadRestController::class, UploadServiceCommonsFileUpload2Configuration::class)
class UploadRestControllerTest {

  @Test
  fun testUpload() {
    val content = """
    -----------------------------15045832833120233291273997644
    Content-Disposition: form-data; name="file"; filename="Hello world.txt"
    Content-Type: text/plain

    Hello world!
    -----------------------------15045832833120233291273997644
    Content-Disposition: form-data; name="text"

    Hello world!
    -----------------------------15045832833120233291273997644--
    """.trimIndent()
  }
}
