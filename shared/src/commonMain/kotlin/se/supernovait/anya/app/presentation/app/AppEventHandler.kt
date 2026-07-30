package se.supernovait.anya.app.presentation.app

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import se.supernovait.anya.app.presentation.navigation.Route
import se.supernovait.anya.core.presentation.util.ObserveAsEvents
import se.supernovait.anya.core.presentation.util.asString

@Composable
fun handleAppEvents(
    events: Flow<AppEvent>,
    snackbarHostState: SnackbarHostState,
    navController: NavHostController? = null
) {
    val scope = rememberCoroutineScope()
    ObserveAsEvents(events = events) { event ->
        when(event) {
            is AppEvent.Error -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.error.asString())
                }
            }
            is AppEvent.Message -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
            AppEvent.NavigateBack -> {
                navController?.popBackStack()
            }
            AppEvent.SignIn -> {
                navController?.let { navigator ->
                    navigator.popBackStack()
                    navigator.navigate(Route.Start)
                }
            }
        }
    }
}
