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
include(":data:auth:impl:firebase")
include(":data:settings")
include(":data:settings:mock")
include(":data:settings:impl")
include(":data:settings:impl:datastore")
include(":data:books")
include(":data:books:mock")
include(":data:books:impl")
include(":data:books:impl:remote")
include(":data:books:impl:remote:firestore")
include(":data:books:impl:local")
include(":data:books:impl:local:files")
include(":data:books:impl:remote:storage")
include(":data:books:impl:local:room")
