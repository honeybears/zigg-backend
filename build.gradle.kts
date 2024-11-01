plugins {
	id("org.springframework.boot") version "3.3.1"
	id("io.spring.dependency-management") version "1.1.5"
	id("org.asciidoctor.jvm.convert") version "3.3.2"
	kotlin("plugin.jpa") version "1.9.24"
	kotlin("plugin.allopen") version "1.9.24"
	kotlin("jvm") version "1.9.24"
	kotlin("plugin.spring") version "1.9.24"
}

group = "soma.achoom"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(21))
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

extra["snippetsDir"] = file("build/generated-snippets")

dependencies {
	implementation("org.flywaydb:flyway-mysql")
	implementation("org.flywaydb:flyway-core")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE")
	implementation("org.springframework.boot:spring-boot-starter-aop")
	implementation("org.springframework.cloud:spring-cloud-gcp-starter:1.2.5.RELEASE")
	implementation("org.springframework.cloud:spring-cloud-gcp-storage:1.2.5.RELEASE")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.7.3")
	implementation("com.google.firebase:firebase-admin:9.2.0")
	implementation("com.fasterxml.jackson.core:jackson-core:2.16.1")
	implementation("jakarta.persistence:jakarta.persistence-api:3.1.0") // Use jakarta.persistence-api for JPA 3.x
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.json:json:20231013")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5")

	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
	runtimeOnly("com.mysql:mysql-connector-j")
	runtimeOnly("com.h2database:h2")

	annotationProcessor("org.projectlombok:lombok")
	annotationProcessor("com.querydsl:querydsl-jpa:5.0.0:jakarta")
	annotationProcessor("jakarta.annotation:jakarta.annotation-api")
	annotationProcessor("jakarta.persistence:jakarta.persistence-api")

	testImplementation("io.mockk:mockk:1.13.5")
	implementation("mysql:mysql-connector-java:8.0.33")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.test {
	outputs.dir(project.extra["snippetsDir"]!!)
	val jasyptPassword: String? = project.findProperty("jasypt.encryptor.password") as String?
	if (jasyptPassword != null) {
		systemProperty("jasypt.encryptor.password", jasyptPassword)
	} else {
		throw GradleException("Jasypt password is required for tests.")
	}
}

tasks.asciidoctor {
	inputs.dir(project.extra["snippetsDir"]!!)
	dependsOn(tasks.test)
}
allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.Embeddable")
	annotation("jakarta.persistence.MappedSuperclass")
}
