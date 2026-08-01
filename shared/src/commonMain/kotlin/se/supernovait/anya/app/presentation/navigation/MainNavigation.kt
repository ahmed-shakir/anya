package se.supernovait.anya.app.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.network_status_loading_label
import anya.shared.generated.resources.network_status_offline_label
import anya.shared.generated.resources.network_status_online_label
import anya.shared.generated.resources.network_status_restricted_label
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import se.supernovait.anya.app.domain.model.ShareType
import se.supernovait.anya.app.presentation.app.auth.AuthenticationManager
import se.supernovait.anya.app.presentation.app.handleAppEvents
import se.supernovait.anya.app.presentation.censored_text.CensoredTextViewModel
import se.supernovait.anya.app.presentation.censored_text.screen.CensoredTextScreen
import se.supernovait.anya.app.presentation.import.ImportScreen
import se.supernovait.anya.app.presentation.import.ImportScreenEvent
import se.supernovait.anya.app.presentation.import.ImportViewModel
import se.supernovait.anya.app.presentation.info.InfoScreen
import se.supernovait.anya.app.presentation.info.InfoScreenState
import se.supernovait.anya.app.presentation.start.StartScreen
import se.supernovait.anya.app.presentation.start.StartScreenEvent
import se.supernovait.anya.app.presentation.welcome.WelcomeScreen
import se.supernovait.anya.app.presentation.welcome.WelcomeScreenEvent
import se.supernovait.anya.app.presentation.welcome.WelcomeViewModel
import se.supernovait.anya.core.domain.manager.DeviceManager
import se.supernovait.anya.core.domain.network.NetworkStatus
import se.supernovait.anya.core.domain.network.NetworkStatusType

fun NavGraphBuilder.welcomeGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    composable<Route.Welcome> {
        val viewModel: WelcomeViewModel = koinViewModel<WelcomeViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        handleAppEvents(
            events = viewModel.events,
            snackbarHostState = snackbarHostState,
            navController = navController
        )

        WelcomeScreen(uiState = uiState, onEvent = { event ->
            when (event) {
                WelcomeScreenEvent.NavigateToInfo -> navController.navigate(Route.Info)
                else -> viewModel.onEvent(event)
            }
        })
    }
}

fun NavGraphBuilder.infoGraph(
    deviceManager: DeviceManager,
    networkStatus: NetworkStatus?
) {
    composable<Route.Info> {
        val networkStatusText = when (networkStatus?.type) {
            NetworkStatusType.OFFLINE -> stringResource(Res.string.network_status_offline_label)
            NetworkStatusType.ONLINE -> stringResource(Res.string.network_status_online_label, networkStatus.networkType)
            NetworkStatusType.RESTRICTED -> stringResource(Res.string.network_status_restricted_label, networkStatus.networkType)
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
}

fun NavGraphBuilder.startGraph(
    navController: NavHostController,
    authManager: AuthenticationManager
) {
    composable<Route.Start> {
        StartScreen(onEvent = { action ->
            when (action) {
                StartScreenEvent.NavigateToCatScreen -> navController.navigate(Route.Cat())
                StartScreenEvent.NavigateToCensoredTextScreen -> navController.navigate(Route.CensoredText)
                StartScreenEvent.NavigateToInfoScreen -> navController.navigate(Route.Info)
                StartScreenEvent.NavigateToOwnerScreen -> navController.navigate(Route.Owner)
                StartScreenEvent.NavigateToProfileScreen -> navController.navigate(
                    Route.OwnerProfile(authManager.getCurrentUser()?.id ?: 0L)
                )
                StartScreenEvent.SignOut -> authManager.logout()
            }
        })
    }
}

fun NavGraphBuilder.importGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    composable<Route.Import> {
        val viewModel: ImportViewModel = koinViewModel<ImportViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        handleAppEvents(
            events = viewModel.events,
            snackbarHostState = snackbarHostState,
            navController = navController
        )

        ImportScreen(uiState = uiState, onEvent = { event ->
            when (event) {
                ImportScreenEvent.Cancel -> navController.popBackStack()
                ImportScreenEvent.ViewDetails -> {
                    when (uiState.type) {
                        ShareType.CAT -> navController.navigate(Route.CatProfile(id = 0, previewData = uiState.data))
                        ShareType.OWNER -> navController.navigate(Route.OwnerProfile(id = 0, previewData = uiState.data))
                        null -> {}
                    }
                }
                else -> viewModel.onEvent(event)
            }
        })
    }
}

fun NavGraphBuilder.censoredTextGraph(
    snackbarHostState: SnackbarHostState
) {
    composable<Route.CensoredText> {
        val viewModel: CensoredTextViewModel = koinViewModel<CensoredTextViewModel>()

        handleAppEvents(events = viewModel.events, snackbarHostState = snackbarHostState)

        CensoredTextScreen(viewModel = viewModel, onEvent = viewModel::onEvent)
    }
}
