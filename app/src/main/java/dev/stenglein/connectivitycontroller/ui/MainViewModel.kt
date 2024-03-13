package dev.stenglein.connectivitycontroller.ui

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.stenglein.connectivitycontroller.data.ConnectivityAction
import dev.stenglein.connectivitycontroller.data.source.connectivity.ConnectivityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class MainUiState(
    val userMessage: String? = null,
    // Whether to show the missing bluetooth permission section or the Bluetooth section
    val showMissingBluetoothPermission: Boolean = true,
    val showInfoDialog: Boolean = false
)

@HiltViewModel
@Stable  // Reduce recompositions when passing ViewModel down to other composables
class MainViewModel @Inject constructor(
    private val connectivityRepository: ConnectivityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    fun addSnackbarMessage(message: String) = _uiState.update { it.copy(userMessage = message) }

    fun snackbarMessageShown() = _uiState.update { it.copy(userMessage = null) }

    fun changeWifiState(action: ConnectivityAction) {
        viewModelScope.launch {
            connectivityRepository.changeWifiState(action)
        }
    }

    fun changeBluetoothState(action: ConnectivityAction) {
        viewModelScope.launch {
            connectivityRepository.changeBluetoothState(action)
        }
    }

    fun updateBluetoothPermissionState(hasPermission: Boolean) {
        _uiState.update { it.copy(showMissingBluetoothPermission = !hasPermission) }
    }

    fun showInfoDialog() {
        _uiState.update { it.copy(showInfoDialog = true) }
    }

    fun closeInfoDialog() {
        _uiState.update { it.copy(showInfoDialog = false) }
    }
}