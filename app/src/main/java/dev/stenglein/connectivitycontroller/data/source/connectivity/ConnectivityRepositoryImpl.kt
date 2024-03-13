package dev.stenglein.connectivitycontroller.data.source.connectivity

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.net.wifi.WifiManager
import dev.stenglein.connectivitycontroller.data.ConnectivityAction


class ConnectivityRepositoryImpl private constructor(
    private val wifiManager: WifiManager, private val bluetoothAdapter: BluetoothAdapter
) : ConnectivityRepository {
    companion object {
        /** Create a new instance of [ConnectivityRepositoryImpl]. */
        fun newInstance(context: Context): ConnectivityRepositoryImpl {
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            return ConnectivityRepositoryImpl(wifiManager, bluetoothAdapter)
        }
    }

    override suspend fun changeWifiState(action: ConnectivityAction) {
        val targetState = when (action) {
            ConnectivityAction.ENABLE -> true
            ConnectivityAction.DISABLE -> false
            ConnectivityAction.TOGGLE -> !wifiManager.isWifiEnabled
        }

        wifiManager.isWifiEnabled = targetState
    }

    override suspend fun changeBluetoothState(action: ConnectivityAction) {
        val targetState = when (action) {
            ConnectivityAction.ENABLE -> true
            ConnectivityAction.DISABLE -> false
            ConnectivityAction.TOGGLE -> !bluetoothAdapter.isEnabled
        }

        if (targetState) {
            bluetoothAdapter.enable()
        } else {
            bluetoothAdapter.disable()
        }
    }
}