import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.2.0"
    id("org.spongepowered.plugin") version "0.8.1"
    id("com.github.johnrengelman.shadow") version "2.0.1"
}

group = "ru.endlesscode"
version = "0.0.1"
description = "Simple plugin for creating items with custom lore"

repositories {
    mavenCentral()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compileOnly(group = "org.spongepowered", name = "spongeapi", version = "5.2.+")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
