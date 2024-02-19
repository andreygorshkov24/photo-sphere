plugins {
  java
  id("org.jetbrains.kotlin.plugin.spring") version "2.0.0-Beta2"
  kotlin("jvm") version "2.0.0-Beta2"
}

group = "com.andreygorshkov24"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web
  implementation("org.springframework.boot:spring-boot-starter-web:3.2.1")
  // https://mvnrepository.com/artifact/org.apache.commons/commons-fileupload2-jakarta
  implementation("org.apache.commons:commons-fileupload2-jakarta:2.0.0-M1")

  val junitVersion = "5.10.0"
  testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
  testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
  testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")

  // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web
  testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.1")
  // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-test
  testImplementation("org.jetbrains.kotlin:kotlin-test:2.0.0-Beta2")
}

tasks.compileKotlin {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "17"
  }
}

tasks.jar {
  manifest {
    attributes(
      "Main-Class" to "com.andreygorshkov24.Main"
    )
  }
}

tasks.test {
  useJUnitPlatform()
}
