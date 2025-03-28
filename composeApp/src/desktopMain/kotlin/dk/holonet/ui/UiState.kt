package dk.holonet.ui

import dk.holonet.core.HoloNetModule

sealed class UiState {
    data object Loading : UiState()
    data class Loaded(val modules: List<HoloNetModule>) : UiState()
    data class Error(val message: String) : UiState()
}