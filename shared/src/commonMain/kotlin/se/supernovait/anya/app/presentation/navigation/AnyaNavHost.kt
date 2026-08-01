package se.supernovait.anya.app.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import se.supernovait.anya.app.presentation.app.auth.AuthenticationManager
import se.supernovait.anya.core.domain.manager.DeviceManager
import se.supernovait.anya.core.domain.network.NetworkStatus

@Composable
fun AnyaNavHost(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    startDestination: Route,
    authManager: AuthenticationManager,
    deviceManager: DeviceManager,
    networkStatus: NetworkStatus?
) {
    NavHost(navController = navController, startDestination = startDestination) {
        welcomeGraph(navController, snackbarHostState)
        infoGraph(deviceManager, networkStatus)
        startGraph(navController, authManager)
        importGraph(navController, snackbarHostState)
        censoredTextGraph(snackbarHostState)
        ownerGraph(navController, snackbarHostState, authManager)
        catGraph(navController, snackbarHostState)
        medicalRecordGraph(navController, snackbarHostState)
    }
}
