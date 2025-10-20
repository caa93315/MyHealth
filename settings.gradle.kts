pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("com.android.application") version "8.1.2"
        id("org.jetbrains.kotlin.android") version "1.8.22"
    }
}

dependencyResolutionManagement {
    dependencyResolutionManagement {
        repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
        repositories {
            google()
            mavenCentral()
        }
    }

    rootProject.name = "Health"
    include(":app")
}