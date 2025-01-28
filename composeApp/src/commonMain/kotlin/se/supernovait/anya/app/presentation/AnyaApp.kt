package se.supernovait.anya.app.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.koin.compose.koinInject
import se.supernovait.anya.app.presentation.censored_text.CensoredTextScreen
import se.supernovait.anya.app.presentation.censored_text.CensoredTextState
import se.supernovait.anya.app.presentation.info.InfoScreen
import se.supernovait.anya.app.presentation.info.InfoScreenState
import se.supernovait.anya.app.presentation.start.StartScreen
import se.supernovait.anya.app.presentation.start.StartScreenAction
import se.supernovait.anya.app.presentation.welcome.WelcomeScreen
import se.supernovait.anya.app.presentation.welcome.WelcomeScreenAction
import se.supernovait.anya.core.domain.util.DeviceManager

/**
 * Scaffold allows you to implement a UI with the basic Material Design layout structure.
 * Scaffold provides slots for the most common top-level Material components, such as TopAppBar, BottomAppBar, FloatingActionButton, and Drawer.
 */
@Composable
fun AnyaApp(navController: NavHostController = rememberNavController()) {
    val deviceManager: DeviceManager = koinInject<DeviceManager>()
    val snackbarHostState = remember { SnackbarHostState() }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = AnyaRoute.parse(backStackEntry?.destination?.route, AnyaRoute.getStartScreen(false))

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            if (currentScreen.useTopBar) {
                TopBar(
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() })
            }
        }
    ) {
        innerPadding ->
        NavHost(navController = navController, startDestination = AnyaRoute.getStartScreen(false), modifier = Modifier.padding(innerPadding)) {
            composable<AnyaRoute.Welcome> {
                WelcomeScreen(onAction = { action ->
                    when(action) {
                        WelcomeScreenAction.OnStartButtonClick -> {
                            navController.popBackStack()
                            navController.navigate(AnyaRoute.Start)
                        }
                        WelcomeScreenAction.OnInfoButtonClick -> navController.navigate(AnyaRoute.Info)
                    }
                })
            }
            composable<AnyaRoute.Info> {
                InfoScreen(
                    uiState = InfoScreenState(
                        platform = deviceManager.getPlatform(),
                        batteryLevel = "${deviceManager.getBatteryLevel()}%"
                    )
                )
            }
            composable<AnyaRoute.Start> {
                StartScreen(onAction = { action ->
                    when(action) {
                        StartScreenAction.OnCensoredTextButtonClick -> navController.navigate(AnyaRoute.CensoredText)
                    }
                })
            }
            composable<AnyaRoute.CensoredText> {
                val censoredTextUiState = CensoredTextState()
                CensoredTextScreen(state = censoredTextUiState, onAction = { })
            }
        }
    }
}
