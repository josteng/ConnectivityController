package dev.stenglein.connectivitycontroller

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.stenglein.connectivitycontroller.data.ConnectivityAction
import dev.stenglein.connectivitycontroller.data.source.connectivity.ConnectivityRepository
import dev.stenglein.connectivitycontroller.data.source.connectivity.FakeConnectivityRepository
import dev.stenglein.connectivitycontroller.ui.MainUi
import dev.stenglein.connectivitycontroller.ui.MainViewModel
import dev.stenglein.connectivitycontroller.util.waitForStateChange
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainActivityUITest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var connectivityRepository: ConnectivityRepository

    private lateinit var fakeConnectivityRepository: FakeConnectivityRepository

    @Before
    fun init() {
        hiltRule.inject()
        fakeConnectivityRepository = connectivityRepository as FakeConnectivityRepository
    }

    @Test
    fun mainUI_showInfo_whenClicked() {
        composeTestRule.setContent {
            MainUi(
                viewModel = MainViewModel(FakeConnectivityRepository()),
                requestBluetoothPermission = { },
                openUrlInBrowser = { })
        }

        // Assert info button exists but About text does not
        composeTestRule.onNodeWithContentDescription("Info").assertExists()
        composeTestRule.onNodeWithText("About Connectivity Controller").assertDoesNotExist()


        // Click the info button
        composeTestRule.onNodeWithContentDescription("Info").performClick()

        // Assert About text is displayed
        composeTestRule.onNodeWithText("About Connectivity Controller").assertExists()
    }

    @Test
    fun mainUI_changeWiFiState_whenButtonClicked() {
        val viewModel = MainViewModel(fakeConnectivityRepository)
        composeTestRule.setContent {
            MainUi(viewModel = viewModel, requestBluetoothPermission = { }, openUrlInBrowser = { })
        }

        runBlocking {
            fakeConnectivityRepository.changeWifiState(ConnectivityAction.DISABLE)
            assert(!fakeConnectivityRepository.wifiState)
        }

        // Enable WiFi
        composeTestRule.onNodeWithTag("EnableWiFi").assertExists()

        // Need to scroll to be able to click (otherwise no warning but on click function is not called)
        composeTestRule.onNodeWithTag("EnableWiFi").performScrollTo()
        composeTestRule.onNodeWithTag("EnableWiFi")
            .assertIsDisplayed()  // Necessary to be able to click

        composeTestRule.onNodeWithTag("EnableWiFi").performClick()
        runBlocking {
            assert(waitForStateChange({ fakeConnectivityRepository.wifiState }, true))
        }

        // Disable WiFi
        composeTestRule.onNodeWithTag("DisableWiFi").assertExists()
        composeTestRule.onNodeWithTag("DisableWiFi").performClick()
        runBlocking {
            assert(waitForStateChange({ fakeConnectivityRepository.wifiState }, false))
        }

        // Toggle WiFi
        composeTestRule.onNodeWithTag("ToggleWiFi").assertExists()
        composeTestRule.onNodeWithTag("ToggleWiFi").performClick()
        runBlocking {
            assert(waitForStateChange({ fakeConnectivityRepository.wifiState }, true))
        }
        composeTestRule.onNodeWithTag("ToggleWiFi").performClick()
        runBlocking {
            assert(waitForStateChange({ fakeConnectivityRepository.wifiState }, false))
        }
    }

    @Test
    fun mainUI_changeBluetoothState_whenButtonClicked() {
        val viewModel = MainViewModel(fakeConnectivityRepository)
        composeTestRule.setContent {
            MainUi(viewModel = viewModel, requestBluetoothPermission = { }, openUrlInBrowser = { })
        }

        runBlocking {
            fakeConnectivityRepository.changeBluetoothState(ConnectivityAction.DISABLE)
            assert(!fakeConnectivityRepository.bluetoothState)
        }

        // Enable Bluetooth
        composeTestRule.onNodeWithTag("EnableBluetooth").assertExists()

        // Need to scroll to be able to click (otherwise no warning but on click function is not called)
        composeTestRule.onNodeWithTag("EnableBluetooth").performScrollTo()
        composeTestRule.onNodeWithTag("EnableBluetooth").assertIsDisplayed()

        composeTestRule.onNodeWithTag("EnableBluetooth").performClick()
        runBlocking {
            assert(waitForStateChange({ fakeConnectivityRepository.bluetoothState }, true))
        }

        // Disable Bluetooth
        composeTestRule.onNodeWithTag("DisableBluetooth").assertExists()
        composeTestRule.onNodeWithTag("DisableBluetooth").performClick()
        runBlocking {
            assert(waitForStateChange({ fakeConnectivityRepository.bluetoothState }, false))
        }

        // Toggle Bluetooth
        composeTestRule.onNodeWithTag("ToggleBluetooth").assertExists()
        composeTestRule.onNodeWithTag("ToggleBluetooth").performClick()
        runBlocking {
            assert(waitForStateChange({ fakeConnectivityRepository.bluetoothState }, true))
        }
        composeTestRule.onNodeWithTag("ToggleBluetooth").performClick()
        runBlocking {
            assert(waitForStateChange({ fakeConnectivityRepository.bluetoothState }, false))
        }
    }
}