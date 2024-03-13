package dev.stenglein.connectivitycontroller

import android.content.Context
import android.net.wifi.WifiManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import dev.stenglein.connectivitycontroller.util.waitForStateChange
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class WiFiBluetoothTest {

    @get:Rule
    var runtimePermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.BLUETOOTH_CONNECT)

    @Test
    fun wifi_shouldUpdate_whenChangedManually() {
        val wiFiManager =
            InstrumentationRegistry.getInstrumentation().targetContext.getSystemService(
                Context.WIFI_SERVICE
            ) as WifiManager

        wiFiManager.isWifiEnabled = true
        runBlocking {
            assertTrue(waitForStateChange(wiFiManager::isWifiEnabled, true))
        }
        assertTrue(wiFiManager.isWifiEnabled)

        // Change back
        wiFiManager.isWifiEnabled = false
        runBlocking {
            assertTrue(waitForStateChange(wiFiManager::isWifiEnabled, false))
        }
        assertFalse(wiFiManager.isWifiEnabled)
    }

    @Test
    fun bluetooth_shouldUpdate_whenChangedManually() {
        val bluetoothAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter()

        bluetoothAdapter.enable()
        runBlocking {
            assertTrue(waitForStateChange(bluetoothAdapter::isEnabled, true))
        }
        assertTrue(bluetoothAdapter.isEnabled)

        bluetoothAdapter.disable()
        runBlocking {
            assertTrue(waitForStateChange(bluetoothAdapter::isEnabled, false))
        }
        assertFalse(bluetoothAdapter.isEnabled)
    }
}