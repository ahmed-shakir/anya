package se.supernovait.anya.app.presentation.navigation

interface NavigationRoute {
    val showTopBar: Boolean
        get() = true
    val showBottomBar: Boolean
        get() = false
    val param: String?
        get() = null
}
