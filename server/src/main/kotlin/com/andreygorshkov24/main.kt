package com.andreygorshkov24

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class Main

fun main(args: Array<String>) {
  val sa = SpringApplication(Main::class.java)
  sa.setBanner { _, _, out -> out?.println("photo-sphere") }
  sa.run(*args)
}
