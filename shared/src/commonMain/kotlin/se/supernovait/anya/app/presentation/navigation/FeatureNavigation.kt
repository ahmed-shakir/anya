package se.supernovait.anya.app.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import org.koin.compose.viewmodel.koinViewModel
import se.supernovait.anya.app.presentation.app.auth.AuthenticationManager
import se.supernovait.anya.app.presentation.app.handleAppEvents
import se.supernovait.anya.app.presentation.cat.CatScreenEvent
import se.supernovait.anya.app.presentation.cat.CatViewModel
import se.supernovait.anya.app.presentation.cat.screen.CatProfileScreen
import se.supernovait.anya.app.presentation.cat.screen.CatScreen
import se.supernovait.anya.app.presentation.medical_record.MedicalRecordScreenEvent
import se.supernovait.anya.app.presentation.medical_record.MedicalRecordViewModel
import se.supernovait.anya.app.presentation.medical_record.screen.MedicalRecordEntryScreen
import se.supernovait.anya.app.presentation.medical_record.screen.MedicalRecordScreen
import se.supernovait.anya.app.presentation.owner.OwnerScreenEvent
import se.supernovait.anya.app.presentation.owner.OwnerViewModel
import se.supernovait.anya.app.presentation.owner.screen.OwnerProfileScreen
import se.supernovait.anya.app.presentation.owner.screen.OwnerScreen

fun NavGraphBuilder.ownerGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    authManager: AuthenticationManager
) {
    composable<Route.Owner> {
        val viewModel: OwnerViewModel = koinViewModel<OwnerViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        handleAppEvents(events = viewModel.events, snackbarHostState = snackbarHostState)

        OwnerScreen(uiState = uiState, onEvent = { event ->
            when (event) {
                is OwnerScreenEvent.NavigateToOwner -> navController.navigate(Route.OwnerProfile(event.id))
                else -> viewModel.onEvent(event)
            }
        })
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
            when (event) {
                is OwnerScreenEvent.NavigateToCats -> navController.navigate(Route.Cat(ownerId = event.ownerId))
                OwnerScreenEvent.SignOut -> authManager.logout()
                else -> viewModel.onEvent(event)
            }
        })
    }
}

fun NavGraphBuilder.catGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    composable<Route.Cat> {
        val viewModel: CatViewModel = koinViewModel<CatViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        handleAppEvents(events = viewModel.events, snackbarHostState = snackbarHostState)

        CatScreen(uiState = uiState, onEvent = { event ->
            when (event) {
                is CatScreenEvent.NavigateToCat -> navController.navigate(Route.CatProfile(event.id))
                is CatScreenEvent.NavigateToOwner -> navController.navigate(Route.OwnerProfile(event.id))
                else -> viewModel.onEvent(event)
            }
        })
    }

    composable<Route.CatProfile> {
        val viewModel: CatViewModel = koinViewModel<CatViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        handleAppEvents(
            events = viewModel.events,
            snackbarHostState = snackbarHostState,
            navController = navController
        )

        viewModel.onEvent(CatScreenEvent.LoadCat)
        CatProfileScreen(uiState = uiState, onEvent = { event ->
            when (event) {
                is CatScreenEvent.NavigateToOwner -> navController.navigate(Route.OwnerProfile(event.id))
                is CatScreenEvent.NavigateToMedicalRecord -> navController.navigate(Route.MedicalRecord(event.catId))
                else -> viewModel.onEvent(event)
            }
        })
    }
}

fun NavGraphBuilder.medicalRecordGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    composable<Route.MedicalRecord> {
        val viewModel: MedicalRecordViewModel = koinViewModel<MedicalRecordViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        handleAppEvents(events = viewModel.events, snackbarHostState = snackbarHostState)

        MedicalRecordScreen(uiState = uiState, onEvent = { event ->
            when (event) {
                is MedicalRecordScreenEvent.NavigateToRecord -> {
                    navController.navigate(Route.MedicalRecordEntry(event.id))
                }
                is MedicalRecordScreenEvent.NavigateToOwner -> {
                    navController.navigate(Route.OwnerProfile(event.id))
                }
                else -> viewModel.onEvent(event)
            }
        })
    }

    composable<Route.MedicalRecordEntry> {
        val viewModel: MedicalRecordViewModel = koinViewModel<MedicalRecordViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        handleAppEvents(
            events = viewModel.events,
            snackbarHostState = snackbarHostState,
            navController = navController
        )

        viewModel.onEvent(MedicalRecordScreenEvent.LoadRecord)
        MedicalRecordEntryScreen(uiState = uiState, onEvent = viewModel::onEvent)
    }
}
