package se.supernovait.anya.app.presentation.censored_text

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.supernovait.anya.app.domain.repository.InsultCensorRepository
import se.supernovait.anya.app.presentation.app.AppEvent
import se.supernovait.anya.core.domain.util.Result

class CensoredTextViewModel(
    private val insultCensorRepository: InsultCensorRepository,
) : ViewModel() {
    private val _uncensoredTextState = MutableStateFlow("")
    private val _uiStateFlow = MutableStateFlow<CensoredTextScreenState>(CensoredTextScreenState.Loading)
    val uiStateFlow = _uiStateFlow
        .onStart { initState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = CensoredTextScreenState.Loading
        )

    private val _events = Channel<AppEvent>()
    val events = _events.receiveAsFlow()

    fun onEvent(event: CensoredTextEvent) {
        when(event) {
            is CensoredTextEvent.UpdateUncensoredText -> setUncensoredText(event.text)
            CensoredTextEvent.CensorText -> censorText()
        }
    }

    private fun setUncensoredText(text: String) {
        _uncensoredTextState.update { text }
    }

    private fun initState() {
        viewModelScope.launch {
            _uiStateFlow.emit(CensoredTextScreenState.Success(counter = insultCensorRepository.getCounter()))
        }
    }

    private fun censorText() {
        viewModelScope.launch {
            _uiStateFlow.emit(CensoredTextScreenState.Loading)

            _uiStateFlow.emit(
                when(val resource = insultCensorRepository.censorWords(_uncensoredTextState.value)) {
                    is Result.Success -> {
                        _uncensoredTextState.update { "" }
                        CensoredTextScreenState.Success(
                            censoredText = resource.data,
                            counter = insultCensorRepository.incrementCounter()
                        )
                    }
                    is Result.Failure -> {
                        CensoredTextScreenState.Failure(onRetry = ::censorText)
                    }
                }
            )
        }
    }
}
