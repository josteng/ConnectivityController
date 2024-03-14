package dev.stenglein.connectivitycontroller.data.source.connectivity

import dev.stenglein.connectivitycontroller.data.ConnectivityAction


/**
 * Connectivity repository without any functionality to use in Compose previews.
 */
class FakeConnectivityRepository : ConnectivityRepository {
    var wifiState: Boolean = false
        private set

    var bluetoothState: Boolean = false
        private set

    private var isBluetoothSupported: Boolean = true

    override suspend fun changeWifiState(action: ConnectivityAction) {
        wifiState = when (action) {
            ConnectivityAction.ENABLE -> true
            ConnectivityAction.DISABLE -> false
            ConnectivityAction.TOGGLE -> !wifiState
        }
    }

    override suspend fun changeBluetoothState(action: ConnectivityAction) {
        if (!isBluetoothSupported) {
            return
        }

        bluetoothState = when (action) {
            ConnectivityAction.ENABLE -> true
            ConnectivityAction.DISABLE -> false
            ConnectivityAction.TOGGLE -> !bluetoothState
        }
    }

    override fun isBluetoothSupported(): Boolean {
        return isBluetoothSupported
    }

    fun setBluetoothSupported(supported: Boolean) {
        isBluetoothSupported = supported
    }
}