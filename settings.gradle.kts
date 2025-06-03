pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        // Remova jcenter() se ainda estiver aqui
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Remova jcenter() se ainda estiver aqui


        maven {
            url = uri("https://artifacts.mercadolibre.com/repository/android-releases")
        }
    }
}

rootProject.name = "DeliveryApp"
include(":app")