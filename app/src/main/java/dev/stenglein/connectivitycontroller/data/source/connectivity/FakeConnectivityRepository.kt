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

    override suspend fun changeWifiState(action: ConnectivityAction) {
        wifiState = when (action) {
            ConnectivityAction.ENABLE -> true
            ConnectivityAction.DISABLE -> false
            ConnectivityAction.TOGGLE -> !wifiState
        }
    }

    override suspend fun changeBluetoothState(action: ConnectivityAction) {
        bluetoothState = when (action) {
            ConnectivityAction.ENABLE -> true
            ConnectivityAction.DISABLE -> false
            ConnectivityAction.TOGGLE -> !bluetoothState
        }
    }
}