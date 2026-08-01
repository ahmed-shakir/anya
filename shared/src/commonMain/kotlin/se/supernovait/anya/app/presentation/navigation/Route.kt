package se.supernovait.anya.app.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Route : NavigationRoute {

    @Serializable
    data object Welcome : Route {
        override val showTopBar = false
    }

    @Serializable
    data object Info: Route

    @Serializable
    data object Start : Route {
        override val showTopBar = false
        override val showBottomBar = true
    }

    @Serializable
    data class Import(val type: String, val data: String): Route

    @Serializable
    data object CensoredText: Route

    @Serializable
    data object Owner: Route

    @Serializable
    data class OwnerProfile(val id: Long, val previewData: String? = null): Route

    @Serializable
    data class Cat(val ownerId: Long? = null): Route

    @Serializable
    data class CatProfile(val id: Long, val previewData: String? = null): Route

    @Serializable
    data class MedicalRecord(val catId: Long = 0L): Route

    @Serializable
    data class MedicalRecordEntry(val id: Long): Route

    companion object {
        fun startScreen(isAuthenticated: Boolean): Route {
            println("StartScreen - isAuthenticated: $isAuthenticated")
            return if (isAuthenticated) Start else Welcome
        }

        fun parse(route: String?, defaultRoute: Route = Welcome): Route {
            return when (route?.substringBefore("/")?.substringBefore("?")) {
                Welcome::class.qualifiedName -> Welcome
                Info::class.qualifiedName -> Info
                Start::class.qualifiedName -> Start
                Import::class.qualifiedName -> Import(type = "", data = "")
                CensoredText::class.qualifiedName -> CensoredText
                Owner::class.qualifiedName -> Owner
                OwnerProfile::class.qualifiedName -> OwnerProfile(id = 0)
                Cat::class.qualifiedName -> Cat()
                CatProfile::class.qualifiedName -> CatProfile(id = 0)
                MedicalRecord::class.qualifiedName -> MedicalRecord(catId = 0)
                MedicalRecordEntry::class.qualifiedName -> MedicalRecordEntry(id = 0)
                else -> defaultRoute
            }
        }
    }
}
