package dev.stenglein.connectivitycontroller

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import dagger.hilt.android.AndroidEntryPoint
import dev.stenglein.connectivitycontroller.ui.MainUi
import dev.stenglein.connectivitycontroller.ui.MainViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                viewModel.updateBluetoothPermissionState(true)
            } else {
                viewModel.addSnackbarMessage("Bluetooth permission not granted. Please grant the permission to enable, disable or toggle Bluetooth.")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Draw behind the system bars: enableEdgeToEdge() before super.onCreate())
        // + Remove status bar code in Theme.kt
        // + Add android:windowSoftInputMode="adjustResize" to application in AndroidManifest.xml
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Initial check for Bluetooth permission
        checkForBluetoothPermission()

        setContent {
            MainUi(
                viewModel = viewModel,  // Pass down view model for simplicity
                requestBluetoothPermission = this::requestBluetoothPermission,
                openUrlInBrowser = this::openUrlInBrowser,
                modifier = Modifier.fillMaxSize()  // No safeDrawingPadding() needed
            )
        }
    }

    override fun onResume() {
        super.onResume()

        // Check if user has granted the permission while the app was in the background
        // (e.g. after user was redirected to settings)
        checkForBluetoothPermission()
    }

    private fun checkForBluetoothPermission() {
        val isPermissionGranted = ActivityCompat.checkSelfPermission(
            this, Manifest.permission.BLUETOOTH_CONNECT
        ) == PackageManager.PERMISSION_GRANTED

        viewModel.updateBluetoothPermissionState(isPermissionGranted)
    }

    /**
     * Request the Bluetooth permission from the user.
     * Check whether the permission is already granted before calling this method.
     */
    private fun requestBluetoothPermission() {
        // Check if modal should be shown to explain why the permission is needed
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            )
        ) {
            requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
        } else {
            Toast.makeText(
                this,
                "Go to Permissions -> Nearby devices -> Allow to enable Bluetooth",
                Toast.LENGTH_LONG
            ).show()

            // Open settings to allow the user to grant the permission
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }
    }

    private fun openUrlInBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}
