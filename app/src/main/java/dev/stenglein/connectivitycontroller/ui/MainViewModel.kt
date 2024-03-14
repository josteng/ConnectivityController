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
    val bluetoothUiState: BluetoothUiState = BluetoothUiState.HIDE_BLUETOOTH_OPTIONS,
    val showInfoDialog: Boolean = false
)

enum class BluetoothUiState {
    SHOW_PERMISSION_REQUEST,  // If bluetooth is supported but permission is missing
    SHOW_BLUETOOTH_OPTIONS,  // If bluetooth is supported and permission is granted
    HIDE_BLUETOOTH_OPTIONS  // If bluetooth is not supported
}

@HiltViewModel
@Stable  // Reduce recompositions when passing ViewModel down to other composables
class MainViewModel @Inject constructor(
    private val connectivityRepository: ConnectivityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                bluetoothUiState =
                if (connectivityRepository.isBluetoothSupported())
                    BluetoothUiState.SHOW_PERMISSION_REQUEST  // Set permission in activity if granted
                else BluetoothUiState.HIDE_BLUETOOTH_OPTIONS
            )
        }
    }

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
        if (_uiState.value.bluetoothUiState == BluetoothUiState.HIDE_BLUETOOTH_OPTIONS) {
            addSnackbarMessage("Error: Tried to update Bluetooth permission state when Bluetooth is unsupported")
        } else {
            _uiState.update {
                it.copy(
                    bluetoothUiState =
                    if (hasPermission) BluetoothUiState.SHOW_BLUETOOTH_OPTIONS
                    else BluetoothUiState.SHOW_PERMISSION_REQUEST
                )
            }
        }
    }

    fun showInfoDialog() {
        _uiState.update { it.copy(showInfoDialog = true) }
    }

    fun closeInfoDialog() {
        _uiState.update { it.copy(showInfoDialog = false) }
    }
}