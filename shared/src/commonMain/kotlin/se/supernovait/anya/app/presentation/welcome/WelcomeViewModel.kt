package se.supernovait.anya.app.presentation.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.supernovait.anya.app.domain.mapper.mapToEntity
import se.supernovait.anya.app.domain.repository.AuthRepository
import se.supernovait.anya.app.presentation.app.AppEvent
import se.supernovait.anya.app.presentation.owner.state.OwnerState
import se.supernovait.anya.app.presentation.owner.state.isUserValid
import se.supernovait.anya.core.domain.model.error.NetworkError

class WelcomeViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(WelcomeScreenState())
    val uiState = _uiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = WelcomeScreenState()
        )

    private val _events = Channel<AppEvent>()
    val events = _events.receiveAsFlow()

    fun onEvent(event: WelcomeScreenEvent) {
        when(event) {
            is WelcomeScreenEvent.SignUp -> signUp(event.profile)
            is WelcomeScreenEvent.SignIn -> signIn(event.username)
            is WelcomeScreenEvent.OnUsernameChange -> updateUsername(event.username)
            WelcomeScreenEvent.ShowSignUpForm -> toggleSignupForm(true)
            WelcomeScreenEvent.HideSignUpForm -> toggleSignupForm(false)
            WelcomeScreenEvent.ShowSignInForm -> toggleSignInForm(true)
            WelcomeScreenEvent.HideSignInForm -> toggleSignInForm(false)
            else -> {
                viewModelScope.launch {
                    _events.send(AppEvent.Error(NetworkError.UNKNOWN))
                }
            }
        }
    }

    private fun signUp(profile: OwnerState) {
        toggleSignupForm(showForm = false)

        if(profile.isUserValid()) {
            viewModelScope.launch {
                val result = authRepository.signUp(profile.mapToEntity())
                if (result.isSuccess) {
                    _events.send(AppEvent.SignIn)
                } else {
                    _events.send(AppEvent.Error(NetworkError.BAD_REQUEST))
                }
            }
        } else {
            viewModelScope.launch {
                _events.send(AppEvent.Error(NetworkError.BAD_REQUEST))
            }
        }
    }

    private fun signIn(username: String) {
        toggleSignInForm(showForm = false)

        viewModelScope.launch {
            val result = authRepository.signIn(username)
            if (result.isSuccess) {
                _events.send(AppEvent.SignIn)
            } else {
                _events.send(AppEvent.Error(NetworkError.UNAUTHORIZED))
            }
        }
    }

    private fun updateUsername(username: String) {
        _uiState.update { currentState -> currentState.copy(username = username) }
    }

    private fun toggleSignupForm(showForm: Boolean) {
        _uiState.update { currentState -> currentState.copy(showSignUpForm = showForm) }
    }

    private fun toggleSignInForm(showForm: Boolean) {
        _uiState.update { currentState -> currentState.copy(username = "", showSignInForm = showForm) }
    }
}
