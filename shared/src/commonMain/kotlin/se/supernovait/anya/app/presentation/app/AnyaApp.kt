package se.supernovait.anya.app.presentation.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.app_icon
import anya.shared.generated.resources.app_logo_content_description
import anya.shared.generated.resources.network_status_loading_label
import anya.shared.generated.resources.network_status_offline_label
import anya.shared.generated.resources.network_status_online_label
import anya.shared.generated.resources.network_status_restricted_label
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import se.supernovait.anya.app.presentation.app.auth.AuthenticationManager
import se.supernovait.anya.app.presentation.app.theme.spacing
import se.supernovait.anya.app.presentation.app.topbar.AppTopBar
import se.supernovait.anya.app.presentation.app.topbar.LocalTopBarState
import se.supernovait.anya.app.presentation.app.topbar.TopBarState
import se.supernovait.anya.app.presentation.info.InfoScreen
import se.supernovait.anya.app.presentation.info.InfoScreenState
import se.supernovait.anya.app.presentation.navigation.Route
import se.supernovait.anya.core.domain.manager.DeviceManager
import se.supernovait.anya.core.domain.network.NetworkHandler
import se.supernovait.anya.core.domain.network.NetworkStatusType

@Composable
fun AnyaApp(navController: NavHostController = rememberNavController()) {
    val authManager: AuthenticationManager = koinInject<AuthenticationManager>()
    val networkHandler: NetworkHandler = koinInject<NetworkHandler>()
    val networkStatus by networkHandler.connectivity.collectAsStateWithLifecycle(null)
    val deviceManager: DeviceManager = koinInject<DeviceManager>()
    val topBarState = remember { TopBarState() }
    val snackbarHostState = remember { SnackbarHostState() }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val startScreen = Route.startScreen(authManager.isAuthenticated())
    val currentScreen = Route.parse(backStackEntry?.destination?.route, startScreen)

    LaunchedEffect(Unit) {
        topBarState.reset()
    }

    CompositionLocalProvider(
        LocalTopBarState provides topBarState
    ) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            topBar = {
                if (currentScreen.showTopBar) {
                    AppTopBar(state = topBarState, navigateUp = { navController.navigateUp() })
                }
            }
        ) { innerPadding ->
            NavHost(navController = navController, startDestination = startScreen, modifier = Modifier.padding(innerPadding)) {
                composable<Route.Welcome> {
                    // TODO: implement
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize().padding(innerPadding)
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.app_icon),
                            contentDescription = stringResource(Res.string.app_logo_content_description),
                            modifier = Modifier
                                .fillMaxWidth(0.4f)
                                .align(Alignment.CenterHorizontally)
                                .padding(top = MaterialTheme.spacing.x3Large)
                        )
                    }
                }
                composable<Route.Info> {
                    val networkStatusText = when(networkStatus?.type) {
                        NetworkStatusType.OFFLINE -> stringResource(Res.string.network_status_offline_label)
                        NetworkStatusType.ONLINE -> stringResource(Res.string.network_status_online_label, networkStatus!!.networkType)
                        NetworkStatusType.RESTRICTED -> stringResource(Res.string.network_status_restricted_label, networkStatus!!.networkType)
                        else -> stringResource(Res.string.network_status_loading_label)
                    }
                    InfoScreen(
                        uiState = InfoScreenState(
                            platform = deviceManager.getPlatform(),
                            batteryLevel = "${deviceManager.getBatteryLevel()}%",
                            networkStatus = networkStatusText
                        )
                    )
                }
                composable<Route.Start> {
                    Text("Start Screen") // TODO: implement
                }
            }
        }
    }
}
