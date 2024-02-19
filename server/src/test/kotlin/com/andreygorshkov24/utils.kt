package com.andreygorshkov24

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class ObjectMapperInit {

  @Bean
  fun objectMapper(): ObjectMapper {
    return ObjectMapper()
  }
}

