package dk.holonet.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dk.holonet.configuration.PluginService
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel(
    private val pluginService: PluginService
) : ViewModel() {

    private val _state: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val state: StateFlow<UiState> = _state.asStateFlow()

    private var serverJob: Job? = null

    init {
        viewModelScope.launch {
            pluginService.initialize()
        }

        viewModelScope.launch {
            pluginService.modules.collect { loadedModules ->
                _state.emit(UiState.Loaded(loadedModules))
            }
        }
    }
}