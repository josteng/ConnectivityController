package dev.stenglein.connectivitycontroller.data.source.connectivity

import dev.stenglein.connectivitycontroller.data.ConnectivityAction


/**
 * Connectivity repository without any functionality to use in Compose previews.
 */
class FakeConnectivityRepository : ConnectivityRepository {
    override suspend fun changeWifiState(action: ConnectivityAction) {}

    override suspend fun changeBluetoothState(action: ConnectivityAction) {}
}