package se.supernovait.anya.app.presentation

import kotlinx.serialization.Serializable

interface ParameterizedRoute {
    val param: String
        get() = ""
}

@Serializable
sealed class AnyaRoute(val useTopBar: Boolean = true, val useBottomBar: Boolean = false) : ParameterizedRoute {
    @Serializable
    data object Start : AnyaRoute(useTopBar = false, useBottomBar = true)
    @Serializable
    data object Welcome : AnyaRoute(useTopBar = false)

    companion object {
        fun getStartScreen(isAuthenticated: Boolean): AnyaRoute {
            return if (isAuthenticated) Start else Welcome
        }

        fun parse(route: String?, defaultRoute: AnyaRoute = Welcome): AnyaRoute {
            return when (route?.substringBefore("?")) {
                Start::class.qualifiedName -> Start
                Welcome::class.qualifiedName -> Welcome
                else -> defaultRoute
            }
        }
    }
}
