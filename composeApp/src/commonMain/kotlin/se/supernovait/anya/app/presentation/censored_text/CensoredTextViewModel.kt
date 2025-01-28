package se.supernovait.anya.app.presentation.censored_text

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import se.supernovait.anya.app.domain.InsultCensorDataSource
import se.supernovait.anya.app.presentation.AnyaEvent
import se.supernovait.anya.core.domain.util.onError
import se.supernovait.anya.core.domain.util.onSuccess

class CensoredTextViewModel(
    private val prefs: DataStore<Preferences>,
    private val client: InsultCensorDataSource,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CensoredTextState())
    val uiState: StateFlow<CensoredTextState> = _uiState.asStateFlow()

    private val _events = Channel<AnyaEvent>()
    val events = _events.receiveAsFlow()

    companion object {
        const val CENSORED_TEXT_COUNTER_KEY = "censored_text_counter"
    }

    init {
        initCounter()
    }

    fun onAction(action: CensoredTextAction) {
        when(action) {
            is CensoredTextAction.OnUncensoredTextUpdate -> setUncensoredText(action.text)
            CensoredTextAction.OnCensorButtonClick -> censorText()
        }
    }

    private fun setCensoredText(text: String) {
        _uiState.update { currentState -> currentState.copy(censoredText = text) }
    }

    private fun setUncensoredText(text: String) {
        _uiState.update { currentState -> currentState.copy(uncensoredText = text) }
    }

    private fun setLoading(isLoading: Boolean) {
        _uiState.update { currentState -> currentState.copy(isLoading = isLoading) }
    }

    private fun initCounter() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _uiState.update { currentState -> currentState.copy(counter = getCounter()) }
            }
        }
    }

    private suspend fun getCounter(): Int {
        return prefs
            .data
            .map {
                val counterKey = intPreferencesKey(CENSORED_TEXT_COUNTER_KEY)
                it[counterKey] ?: 0
            }
            .first()
    }

    private suspend fun incrementCounter() {
        prefs.edit { dataStore ->
            val counterKey = intPreferencesKey(CENSORED_TEXT_COUNTER_KEY)
            val counter = getCounter() + 1
            dataStore[counterKey] = counter
            _uiState.update { currentState -> currentState.copy(counter = counter) }
        }
    }

    private fun censorText() {
        viewModelScope.launch {
            setLoading(true)

            client.censorWords(_uiState.value.uncensoredText)
                .onSuccess {
                    setCensoredText(it)
                }
                .onError { error ->
                    _events.send(AnyaEvent.Error(error))
                }
            setLoading(false)
            incrementCounter()
        }
    }
}
