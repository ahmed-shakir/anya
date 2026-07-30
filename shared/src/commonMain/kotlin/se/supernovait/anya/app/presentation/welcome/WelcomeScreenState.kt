package se.supernovait.anya.app.presentation.welcome

data class WelcomeScreenState(
    val showSignUpForm: Boolean = false,
    val showSignInForm: Boolean = false,
    val username: String = ""
)
