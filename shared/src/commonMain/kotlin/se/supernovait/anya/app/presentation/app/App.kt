package se.supernovait.anya.app.presentation.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.PreviewLightDark
import anya.shared.generated.resources.Res
import anya.shared.generated.resources.error_initialization_database_action_primary_label
import anya.shared.generated.resources.error_initialization_database_action_secondary_label
import anya.shared.generated.resources.error_initialization_database_description
import anya.shared.generated.resources.error_initialization_database_title
import anya.shared.generated.resources.error_initialization_network_action_primary_label
import anya.shared.generated.resources.error_initialization_network_action_secondary_label
import anya.shared.generated.resources.error_initialization_network_description
import anya.shared.generated.resources.error_initialization_network_title
import anya.shared.generated.resources.error_initialization_preferences_action_primary_label
import anya.shared.generated.resources.error_initialization_preferences_action_secondary_label
import anya.shared.generated.resources.error_initialization_preferences_description
import anya.shared.generated.resources.error_initialization_preferences_title
import anya.shared.generated.resources.error_initialization_unknown_action_primary_label
import anya.shared.generated.resources.error_initialization_unknown_description
import anya.shared.generated.resources.error_initialization_unknown_title
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import se.supernovait.anya.app.domain.model.initialization.InitializationErrorType
import se.supernovait.anya.app.presentation.app.initialization.AppInitializationState
import se.supernovait.anya.app.presentation.app.initialization.AppInitializer
import se.supernovait.anya.app.presentation.app.theme.AnyaTheme
import se.supernovait.anya.core.domain.model.RecoveryOption
import se.supernovait.anya.core.presentation.common.error.ErrorScreen
import se.supernovait.anya.core.presentation.common.error.ErrorScreenHandler
import se.supernovait.anya.core.presentation.common.error.ErrorScreenState
import se.supernovait.anya.core.presentation.common.preview.ScreenPreviewContainer

@Composable
fun App() {
    AnyaTheme {
        AppContent()
    }
}

/**
 * Displays either the main app (AnyaApp) or an error screen based on initialization state.
 *
 * - If initialization is successful: shows AnyaApp with full navigation
 * - If initialization fails: shows ErrorScreen with error-specific recovery options:
 *   - Network error: Retry or Go Offline
 *   - Database error: Retry or Clear App Data
 *   - Preferences error: Retry or Reset Preferences
 *   - Unknown error: Retry
 */
@Composable
private fun AppContent() {
    val appInitializer: AppInitializer = koinInject()
    val initState by appInitializer.appInitState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    when (initState) {
        is AppInitializationState.Success -> {
            AnyaApp()
        }

        is AppInitializationState.Error -> {
            val errorState = initState as AppInitializationState.Error
            val errorScreenState = getErrorScreenState(errorState)

            ErrorScreen(
                title = errorScreenState.title,
                description = errorScreenState.description,
                primaryActionLabel = errorScreenState.primaryActionLabel,
                secondaryActionLabel = errorScreenState.secondaryActionLabel,
                handler = ErrorScreenHandler(
                    onRetry = {
                        coroutineScope.launch {
                            appInitializer.retryWithRecovery(RecoveryOption.RETRY)
                        }
                    },
                    onSecondaryAction = if (errorScreenState.secondaryAction != null) {
                        {
                            coroutineScope.launch {
                                appInitializer.retryWithRecovery(errorScreenState.secondaryAction)
                            }
                        }
                    } else null
                )
            )
        }

        is AppInitializationState.Initializing -> {
            // Initialization still in progress
            // On Android: splash screen is kept visible via setKeepOnScreenCondition
            // On iOS: LaunchScreen is kept visible (no Compose content rendered)
            // This composable is not rendered, so the system splash stays visible
        }
    }
}


@Composable
private fun getErrorScreenState(errorState: AppInitializationState.Error): ErrorScreenState {
    return when (errorState.errorType) {
        InitializationErrorType.NETWORK -> {
            ErrorScreenState(
                stringResource(Res.string.error_initialization_network_title),
                stringResource(Res.string.error_initialization_network_description),
                stringResource(Res.string.error_initialization_network_action_primary_label),
                stringResource(Res.string.error_initialization_network_action_secondary_label),
                RecoveryOption.OFFLINE_MODE
            )
        }

        InitializationErrorType.DATABASE -> {
            ErrorScreenState(
                stringResource(Res.string.error_initialization_database_title),
                stringResource(Res.string.error_initialization_database_description),
                stringResource(Res.string.error_initialization_database_action_primary_label),
                stringResource(Res.string.error_initialization_database_action_secondary_label),
                RecoveryOption.CLEAR_APP_DATA
            )
        }

        InitializationErrorType.PREFERENCES -> {
            ErrorScreenState(
                stringResource(Res.string.error_initialization_preferences_title),
                stringResource(Res.string.error_initialization_preferences_description),
                stringResource(Res.string.error_initialization_preferences_action_primary_label),
                stringResource(Res.string.error_initialization_preferences_action_secondary_label),
                RecoveryOption.RESET_PREFERENCES
            )
        }

        InitializationErrorType.UNKNOWN -> {
            ErrorScreenState(
                stringResource(Res.string.error_initialization_unknown_title),
                stringResource(Res.string.error_initialization_unknown_description),
                stringResource(Res.string.error_initialization_unknown_action_primary_label)
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    ScreenPreviewContainer {
        App()
    }
}
