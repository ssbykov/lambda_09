plugins {
    kotlin("jvm") version "1.9.21"
    application
    jacoco
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("junit:junit:4.13.2")
}

//tasks.test {
//    useJUnitPlatform()
//}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}