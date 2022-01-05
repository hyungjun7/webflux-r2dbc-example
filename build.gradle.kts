import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val jjwt_version: String by project
val openapi_version: String by project
val r2dbc_mysql_version: String by project
val dotenv_version: String by project
val jbcrypt_version: String by project

plugins {
  id("org.springframework.boot") version "2.6.0"
  id("io.spring.dependency-management") version "1.0.11.RELEASE"
  kotlin("plugin.noarg") version "1.6.10"
  kotlin("plugin.jpa") version "1.6.10"
  kotlin("jvm") version "1.6.0"
  kotlin("plugin.spring") version "1.6.0"
}

allOpen {
  annotation("javax.persistence.Entity")
  annotation("javax.persistence.MappedSuperclass")
  annotation("javax.persistence.Embeddable")
}

noArg {
  annotation("javax.persistence.Entity")
  annotation("javax.persistence.MappedSuperclass")
  annotation("javax.persistence.Embeddable")
}

group = "info.hyungjun"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
  implementation("io.github.cdimascio:dotenv-kotlin:$dotenv_version")
  implementation("de.svenkubiak:jBCrypt:$jbcrypt_version")
  implementation("org.springdoc:springdoc-openapi-webflux-core:$openapi_version")
  implementation("org.springdoc:springdoc-openapi-webflux-ui:$openapi_version")
  implementation("org.springdoc:springdoc-openapi-kotlin:$openapi_version")
  implementation("io.jsonwebtoken:jjwt-api:$jjwt_version")
  implementation("dev.miku:r2dbc-mysql:$r2dbc_mysql_version")
  runtimeOnly("mysql:mysql-connector-java")
  runtimeOnly("io.jsonwebtoken:jjwt-impl:$jjwt_version")
  runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jjwt_version")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "1.8"
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}
