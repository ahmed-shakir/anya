package se.supernovait.anya.app.presentation.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.network_status_loading_label
import anya.shared.generated.resources.network_status_offline_label
import anya.shared.generated.resources.network_status_online_label
import anya.shared.generated.resources.network_status_restricted_label
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import se.supernovait.anya.app.presentation.app.auth.AuthenticationManager
import se.supernovait.anya.app.presentation.app.auth.AuthenticationState
import se.supernovait.anya.app.presentation.app.auth.LocalAuthState
import se.supernovait.anya.app.presentation.app.topbar.AppTopBar
import se.supernovait.anya.app.presentation.app.topbar.LocalTopBarState
import se.supernovait.anya.app.presentation.app.topbar.TopBarState
import se.supernovait.anya.app.presentation.info.InfoScreen
import se.supernovait.anya.app.presentation.info.InfoScreenState
import se.supernovait.anya.app.presentation.navigation.Route
import se.supernovait.anya.app.presentation.owner.OwnerScreenEvent
import se.supernovait.anya.app.presentation.owner.OwnerViewModel
import se.supernovait.anya.app.presentation.owner.screen.OwnerProfileScreen
import se.supernovait.anya.app.presentation.start.StartScreen
import se.supernovait.anya.app.presentation.start.StartScreenEvent
import se.supernovait.anya.app.presentation.welcome.WelcomeScreen
import se.supernovait.anya.app.presentation.welcome.WelcomeScreenEvent
import se.supernovait.anya.app.presentation.welcome.WelcomeViewModel
import se.supernovait.anya.core.domain.manager.DeviceManager
import se.supernovait.anya.core.domain.network.NetworkHandler
import se.supernovait.anya.core.domain.network.NetworkStatusType
import se.supernovait.anya.core.presentation.common.AnyaIcon
import se.supernovait.anya.core.presentation.common.action.FabState
import se.supernovait.anya.core.presentation.common.action.LocalFabState

@Composable
fun AnyaApp(navController: NavHostController = rememberNavController()) {
    val authManager: AuthenticationManager = koinInject<AuthenticationManager>()
    val networkHandler: NetworkHandler = koinInject<NetworkHandler>()
    val authState by authManager.authState.collectAsStateWithLifecycle()
    val networkStatus by networkHandler.connectivity.collectAsStateWithLifecycle(null)
    val deviceManager: DeviceManager = koinInject<DeviceManager>()
    val topBarState = remember { TopBarState() }
    val fabState = remember { FabState() }
    val snackbarHostState = remember { SnackbarHostState() }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val startScreen = Route.startScreen(authManager.isAuthenticated())
    val currentScreen = Route.parse(backStackEntry?.destination?.route, startScreen)

    LaunchedEffect(authState) {
        if (authState is AuthenticationState.NotAuthenticated && currentScreen != Route.Welcome) {
            navController.navigate(Route.Welcome) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    LaunchedEffect(Unit) {
        topBarState.reset()
    }

    CompositionLocalProvider(
        LocalAuthState provides authState,
        LocalTopBarState provides topBarState,
        LocalFabState provides fabState
    ) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            topBar = {
                if (currentScreen.showTopBar) {
                    AppTopBar(state = topBarState, navigateUp = { navController.navigateUp() })
                }
            },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                val icon = fabState.icon
                val contentDescription = fabState.contentDescription
                val onClick = fabState.onClick
                if (icon != null && onClick != null) {
                    FloatingActionButton(onClick = onClick) {
                        AnyaIcon(icon = icon, contentDescription = contentDescription)
                    }
                }
            }
        ) { innerPadding ->
            NavHost(navController = navController, startDestination = startScreen, modifier = Modifier.padding(innerPadding)) {
                composable<Route.Welcome> {
                    val viewModel: WelcomeViewModel = koinViewModel<WelcomeViewModel>()
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                    handleAppEvents(
                        events = viewModel.events,
                        snackbarHostState = snackbarHostState,
                        navController = navController
                    )

                    WelcomeScreen(uiState = uiState, onEvent = { event ->
                        when(event) {
                            WelcomeScreenEvent.NavigateToInfo -> navController.navigate(Route.Info)
                            else -> viewModel.onEvent(event)
                        }
                    })
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
                    StartScreen(onEvent = { action ->
                        when(action) {
                            StartScreenEvent.NavigateToCatScreen -> navController.navigate(Route.Cat())
                            StartScreenEvent.NavigateToCensoredTextScreen -> navController.navigate(Route.CensoredText)
                            StartScreenEvent.NavigateToInfoScreen -> navController.navigate(Route.Info)
                            StartScreenEvent.NavigateToOwnerScreen -> navController.navigate(Route.Owner)
                            StartScreenEvent.NavigateToProfileScreen -> navController.navigate(Route.OwnerProfile(authManager.getCurrentUser()?.id ?: 0L))
                            StartScreenEvent.SignOut -> authManager.logout()
                        }
                    })
                }

                composable<Route.CensoredText> {
                    Text("Route.CensoredText")
                }

                composable<Route.Owner> {
                    Text("Route.Owner")
                }

                composable<Route.OwnerProfile> {
                    val viewModel: OwnerViewModel = koinViewModel<OwnerViewModel>()
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                    handleAppEvents(
                        events = viewModel.events,
                        snackbarHostState = snackbarHostState,
                        navController = navController
                    )

                    viewModel.onEvent(OwnerScreenEvent.LoadOwner)
                    OwnerProfileScreen(uiState = uiState, onEvent = { event ->
                        when(event) {
                            is OwnerScreenEvent.NavigateToCats -> navController.navigate(Route.Cat(ownerId = event.ownerId))
                            OwnerScreenEvent.SignOut -> authManager.logout()
                            else -> viewModel.onEvent(event)
                        }
                    })
                }

                composable<Route.Cat> {
                    Text("Route.Cat")
                }

                composable<Route.CatProfile> {
                    Text("Route.CatProfile")
                }
            }
        }
    }
}
