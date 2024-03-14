package dev.stenglein.connectivitycontroller.data.source.connectivity

import dev.stenglein.connectivitycontroller.data.ConnectivityAction

/**
 * Class containing functions to enable, disable and toggle WiFi and Bluetooth.
 */
interface ConnectivityRepository {
    suspend fun changeWifiState(action: ConnectivityAction)

    suspend fun changeBluetoothState(action: ConnectivityAction)

    fun isBluetoothSupported(): Boolean
}