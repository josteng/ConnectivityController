package dev.stenglein.connectivitycontroller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dev.stenglein.connectivitycontroller.data.ConnectivityAction
import dev.stenglein.connectivitycontroller.data.source.connectivity.ConnectivityRepositoryImpl
import kotlinx.coroutines.runBlocking


/**
 * BroadcastReceiver to handle incoming broadcasts to change connectivity.
 */
class ChangeConnectivityReceiver : BroadcastReceiver() {
    companion object {
        const val ENABLE_WIFI = "dev.stenglein.connectivitycontroller.ENABLE_WIFI"
        const val DISABLE_WIFI = "dev.stenglein.connectivitycontroller.DISABLE_WIFI"
        const val TOGGLE_WIFI = "dev.stenglein.connectivitycontroller.TOGGLE_WIFI"
        const val ENABLE_BLUETOOTH = "dev.stenglein.connectivitycontroller.ENABLE_BLUETOOTH"
        const val DISABLE_BLUETOOTH = "dev.stenglein.connectivitycontroller.DISABLE_BLUETOOTH"
        const val TOGGLE_BLUETOOTH = "dev.stenglein.connectivitycontroller.TOGGLE_BLUETOOTH"

        const val TAG_CHANGE_CONNECTIVITY_RECEIVER = "ChangeConnectivityReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG_CHANGE_CONNECTIVITY_RECEIVER, "Received broadcast: ${intent?.action}")

        val connectivityRepository = ConnectivityRepositoryImpl.newInstance(context!!)

        runBlocking {
            when (intent?.action) {
                ENABLE_WIFI -> connectivityRepository.changeWifiState(ConnectivityAction.ENABLE)
                DISABLE_WIFI -> connectivityRepository.changeWifiState(ConnectivityAction.DISABLE)
                TOGGLE_WIFI -> connectivityRepository.changeWifiState(ConnectivityAction.TOGGLE)
                ENABLE_BLUETOOTH -> connectivityRepository.changeBluetoothState(ConnectivityAction.ENABLE)
                DISABLE_BLUETOOTH -> connectivityRepository.changeBluetoothState(ConnectivityAction.DISABLE)
                TOGGLE_BLUETOOTH -> connectivityRepository.changeBluetoothState(ConnectivityAction.TOGGLE)
            }
        }
    }
}