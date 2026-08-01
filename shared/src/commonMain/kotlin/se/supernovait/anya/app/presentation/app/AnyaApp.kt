package se.supernovait.anya.app.presentation.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.koin.compose.koinInject
import se.supernovait.anya.app.domain.navigation.DeepLinkHandler
import se.supernovait.anya.app.presentation.app.auth.AuthenticationManager
import se.supernovait.anya.app.presentation.app.auth.AuthenticationState
import se.supernovait.anya.app.presentation.app.auth.LocalAuthState
import se.supernovait.anya.app.presentation.app.topbar.AppTopBar
import se.supernovait.anya.app.presentation.app.topbar.LocalTopBarState
import se.supernovait.anya.app.presentation.app.topbar.TopBarState
import se.supernovait.anya.app.presentation.navigation.AnyaNavHost
import se.supernovait.anya.app.presentation.navigation.Route
import se.supernovait.anya.core.domain.manager.DeviceManager
import se.supernovait.anya.core.domain.network.NetworkHandler
import se.supernovait.anya.core.presentation.common.AnyaIcon
import se.supernovait.anya.core.presentation.common.action.FabState
import se.supernovait.anya.core.presentation.common.action.LocalFabState
import se.supernovait.anya.core.presentation.common.network.NetworkIndicator

@Composable
fun AnyaApp(navController: NavHostController = rememberNavController()) {
    val authManager: AuthenticationManager = koinInject<AuthenticationManager>()
    val networkHandler: NetworkHandler = koinInject<NetworkHandler>()
    val deepLinkHandler: DeepLinkHandler = koinInject<DeepLinkHandler>()
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
        deepLinkHandler.events.collect { route ->
            navController.navigate(route)
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
            Column(modifier = Modifier.padding(innerPadding)) {
                NetworkIndicator(type = networkStatus?.type)
                AnyaNavHost(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    startDestination = startScreen,
                    authManager = authManager,
                    deviceManager = deviceManager,
                    networkStatus = networkStatus
                )
            }
        }
    }
}
