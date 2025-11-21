pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "AvitoTestBookReader"
include(":app")
include(":feature")
include(":domain")
include(":core")
include(":data")
include(":domain:auth")
include(":domain:books")
include(":domain:settings")
include(":feature:splash")
include(":feature:auth")
include(":feature:profile")
include(":feature:reader")
include(":feature:uploader")
include(":feature:books")
include(":data:auth")
include(":data:auth:mock")
include(":data:auth:impl")
include(":data:settings")
include(":data:settings:mock")
include(":data:settings:impl")
include(":data:books")
include(":data:books:mock")
include(":data:books:impl")
include(":core:ui")
include(":core:database")
include(":core:datastore")
include(":data:auth:impl:datasources")
include(":data:auth:impl:datasources:firebase")
include(":data:settings:impl:datasources")
include(":data:settings:impl:datasources:datastore")
include(":data:books:impl:datasources")
include(":data:books:impl:datasources")
include(":data:books:impl:datasources:localfiles")
include(":data:books:impl:datasources:room")
include(":data:books:impl:datasources:firestore")
include(":data:books:impl:datasources:firebase_storage")
