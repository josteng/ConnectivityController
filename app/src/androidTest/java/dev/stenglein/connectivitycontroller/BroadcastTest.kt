package dev.stenglein.connectivitycontroller

import android.content.ComponentName
import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.stenglein.connectivitycontroller.data.ConnectivityAction
import dev.stenglein.connectivitycontroller.data.source.connectivity.ConnectivityRepository
import dev.stenglein.connectivitycontroller.data.source.connectivity.FakeConnectivityRepository
import dev.stenglein.connectivitycontroller.util.waitForStateChange
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest  // Change testInstrumentationRunner = "dev.stenglein.connectivitycontroller.util.CustomTestRunner" in build.gradle or use Robolectric's @Config annotation
@RunWith(AndroidJUnit4::class)
class BroadcastTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var connectivityRepository: ConnectivityRepository

    lateinit var fakeConnectivityRepository: FakeConnectivityRepository

    @Before
    fun init() {
        hiltRule.inject()
        fakeConnectivityRepository = connectivityRepository as FakeConnectivityRepository
    }

    @Test
    fun broadcast_shouldEnableWiFi_whenBroadcastSent() {
        broadcast_shouldUpdateConnectivity_whenBroadcastSent(
            "ENABLE_WIFI",
            expectedState = true,
            isWifi = true
        )
    }

    @Test
    fun broadcast_shouldDisableWiFi_whenBroadcastSent() {
        broadcast_shouldUpdateConnectivity_whenBroadcastSent(
            "DISABLE_WIFI",
            expectedState = false,
            isWifi = true
        )
    }

    @Test
    fun broadcast_shouldToggleWiFi_whenBroadcastSent() {
        val initialWifiState = fakeConnectivityRepository.wifiState
        broadcast_shouldUpdateConnectivity_whenBroadcastSent(
            "TOGGLE_WIFI",
            expectedState = !initialWifiState,
            isWifi = true
        )
        broadcast_shouldUpdateConnectivity_whenBroadcastSent(
            "TOGGLE_WIFI",
            expectedState = initialWifiState,
            isWifi = true
        )
        assertEquals(initialWifiState, fakeConnectivityRepository.wifiState)
    }

    @Test
    fun broadcast_shouldEnableBluetooth_whenBroadcastSent() {
        broadcast_shouldUpdateConnectivity_whenBroadcastSent(
            "ENABLE_BLUETOOTH",
            expectedState = true,
            isWifi = false
        )
    }

    @Test
    fun broadcast_shouldDisableBluetooth_whenBroadcastSent() {
        broadcast_shouldUpdateConnectivity_whenBroadcastSent(
            "DISABLE_BLUETOOTH",
            expectedState = false,
            isWifi = false
        )
    }

    @Test
    fun broadcast_shouldToggleBluetooth_whenBroadcastSent() {
        val initialBluetoothState = fakeConnectivityRepository.bluetoothState
        broadcast_shouldUpdateConnectivity_whenBroadcastSent(
            "TOGGLE_BLUETOOTH",
            expectedState = !initialBluetoothState,
            isWifi = false
        )
        broadcast_shouldUpdateConnectivity_whenBroadcastSent(
            "TOGGLE_BLUETOOTH",
            expectedState = initialBluetoothState,
            isWifi = false
        )
        assertEquals(initialBluetoothState, fakeConnectivityRepository.bluetoothState)
    }

    private fun broadcast_shouldUpdateConnectivity_whenBroadcastSent(
        action: String,
        expectedState: Boolean,
        isWifi: Boolean
    ) {
        runBlocking {
            // Change to wrong state to ensure the broadcast changes it if not TOGGLE_WIFI or TOGGLE_BLUETOOTH
            if (action != "TOGGLE_WIFI" && action != "TOGGLE_BLUETOOTH") {
                val connectivityAction =
                    if (expectedState) ConnectivityAction.DISABLE else ConnectivityAction.ENABLE
                if (isWifi) {
                    connectivityRepository.changeWifiState(connectivityAction)
                    assertTrue(fakeConnectivityRepository.wifiState != expectedState)
                } else {
                    connectivityRepository.changeBluetoothState(connectivityAction)
                    assertTrue(fakeConnectivityRepository.bluetoothState != expectedState)
                }
            }
        }

        val intent = Intent("dev.stenglein.connectivitycontroller.$action")
        intent.component = ComponentName(
            "dev.stenglein.connectivitycontroller",
            "dev.stenglein.connectivitycontroller.ChangeConnectivityReceiver"
        )
        InstrumentationRegistry.getInstrumentation().targetContext.sendBroadcast(intent)

        // Wait for the broadcast to be processed
        runBlocking {
            val isStateChanged = waitForStateChange(
                if (isWifi) fakeConnectivityRepository::wifiState else fakeConnectivityRepository::bluetoothState,
                expectedState
            )
            assertTrue(isStateChanged)
        }
    }
}
