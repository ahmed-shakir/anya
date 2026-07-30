package se.supernovait.anya.app.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Route : NavigationRoute {

    @Serializable
    data object Start : Route {
        override val showTopBar = false
        override val showBottomBar = true
    }

    @Serializable
    data object Welcome : Route {
        override val showTopBar = false
    }

    companion object {
        fun startScreen(isAuthenticated: Boolean): Route {
            println("StartScreen - isAuthenticated: $isAuthenticated")
            return if (isAuthenticated) Start else Welcome
        }

        fun parse(route: String?, defaultRoute: Route = Welcome): Route {
            return when (route?.substringBefore("/")?.substringBefore("?")) {
                Start::class.qualifiedName -> Start
                Welcome::class.qualifiedName -> Welcome
                else -> defaultRoute
            }
        }
    }
}
