package dev.stenglein.connectivitycontroller.ui

import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.stenglein.alarmairplanetoggle.ui.BaseSection
import dev.stenglein.alarmairplanetoggle.ui.ScaffoldCustom
import dev.stenglein.alarmairplanetoggle.ui.SectionEntry
import dev.stenglein.connectivitycontroller.R
import dev.stenglein.connectivitycontroller.data.ConnectivityAction
import dev.stenglein.connectivitycontroller.data.source.connectivity.FakeConnectivityRepository
import dev.stenglein.connectivitycontroller.ui.theme.ConnectivityControllerTheme


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainUi(
    viewModel: MainViewModel,
    requestBluetoothPermission: () -> Unit,  // Passing down function instead of using Accompanist Permissions API in this simple app
    openUrlInBrowser: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ConnectivityControllerTheme {
        val scrollBehavior =
            TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
        val snackbarHostState = remember { SnackbarHostState() }

        ScaffoldCustom(
            modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = { MainTopAppBar(scrollBehavior, openUrlInBrowser, viewModel::showInfoDialog) },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            sections = listOf(
                {
                    UsageSection(
                        showRequestBluetoothPermission = uiState.showMissingBluetoothPermission,
                        requestBluetoothPermission = requestBluetoothPermission
                    )
                },
                {
                    TestingSection(
                        changeWiFiState = viewModel::changeWifiState,
                        changeBluetoothState = viewModel::changeBluetoothState
                    )
                })
        )

        uiState.userMessage?.let { userMessage ->
            LaunchedEffect(snackbarHostState, viewModel, userMessage) {
                snackbarHostState.showSnackbar(userMessage, duration = SnackbarDuration.Long)
                viewModel.snackbarMessageShown()
            }
        }

        if (uiState.showInfoDialog) {
            InfoDialog(closeDialog = { viewModel.closeInfoDialog() })
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MainTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    openUrlInBrowser: (String) -> Unit,
    openInfoDialog: () -> Unit
) {
    LargeTopAppBar(title = { Text("Connectivity Controller") },
        scrollBehavior = scrollBehavior,
        actions = {
            IconButton(onClick = { openUrlInBrowser("https://github.com/josteng/ConnectivityController") }) {
                Icon(
                    painter = painterResource(id = R.drawable.github_mark),
                    modifier = Modifier.scale(0.5f),
                    contentDescription = "GitHub"
                )
            }
            IconButton(onClick = openInfoDialog) {
                Icon(Icons.Filled.Info, contentDescription = "Info")
            }
        })
}

@Composable
fun UsageSection(
    showRequestBluetoothPermission: Boolean,
    requestBluetoothPermission: () -> Unit,
) {
    BaseSection(headline = "Usage overview") {
        SectionEntry(
            title = "How to use",
            description = "This app makes it easier for other apps to change the state of WiFi and Bluetooth on newer versions of Android. It listens for broadcast messages from other apps to make these changes. More details can be found in the GitHub repository."
        )
        SectionEntry(
            title = "Permissions",
            description = "This app requires permissions to control WiFi and Bluetooth. On Android 13 and later, the permission to control Bluetooth needs to be granted manually."
        ) {
            AnimatedContent(
                targetState = showRequestBluetoothPermission,
                label = "bluetooth permission granted"
            ) { showRequestBluetoothPermission ->
                if (showRequestBluetoothPermission) {
                    MissingBluetoothPermissionCard(requestBluetoothPermission)
                } else {
                    AllPermissionsGrantedCard()
                }
            }
        }
    }
}


@Composable
private fun TestingSection(
    changeWiFiState: (ConnectivityAction) -> Unit,
    changeBluetoothState: (ConnectivityAction) -> Unit
) {
    BaseSection(headline = "Testing") {
        ThreeButtonsSection(
            title = "WiFi",
            description = "Test the app's control over WiFi by enabling, disabling, or toggling.",
            button1Text = "Enable",
            button1OnClick = { changeWiFiState(ConnectivityAction.ENABLE) },
            button2Text = "Disable",
            button2OnClick = { changeWiFiState(ConnectivityAction.DISABLE) },
            button3Text = "Toggle",
            button3OnClick = { changeWiFiState(ConnectivityAction.TOGGLE) }
        )

        ThreeButtonsSection(
            title = "Bluetooth",
            description = "Test the app's control over Bluetooth by enabling, disabling, or toggling.",
            button1Text = "Enable",
            button1OnClick = { changeBluetoothState(ConnectivityAction.ENABLE) },
            button2Text = "Disable",
            button2OnClick = { changeBluetoothState(ConnectivityAction.DISABLE) },
            button3Text = "Toggle",
            button3OnClick = { changeBluetoothState(ConnectivityAction.TOGGLE) }
        )
    }
}


@Composable
private fun MissingBluetoothPermissionCard(requestBluetoothPermission: () -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(), colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.error
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Warning,
                    contentDescription = "Warning"
                )
                Text("Missing Bluetooth permission")
            }

            ElevatedButton(
                onClick = requestBluetoothPermission,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.elevatedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Grant Bluetooth permission")
            }
            Text("If the permission request using the button above is unsuccessful, you can alternatively use the ADB tools on your computer with the following command: 'adb shell pm grant dev.stenglein.connectivitycontroller android.permission.BLUETOOTH_CONNECT'.")

        }
    }
}


@Composable
private fun AllPermissionsGrantedCard() {
    ElevatedCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = "Checkmark"
            )
            Text("All permissions granted")
        }
    }
}


@Composable
fun InfoDialog(
    closeDialog: () -> Unit
) {
    val context = LocalContext.current
    AlertDialog(title = { Text("About Connectivity Controller") },
        icon = { /* TODO: Add icon */ },
        text = {
            val versionName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(
                    context.packageName, PackageManager.PackageInfoFlags.of(0)
                ).versionName
            } else {
                // Deprecated since API 33, but new method not working below API 33
                @Suppress("DEPRECATION") context.packageManager.getPackageInfo(
                    context.packageName,
                    0
                ).versionName
            }
            Column {
                Text("Version: $versionName")
                Text("Developer: Jonas Stenglein")
                Spacer(modifier = Modifier.height(16.dp))
                Text("For more information, visit the GitHub repository.")
            }
        },

        onDismissRequest = closeDialog,
        confirmButton = {
            TextButton(onClick = closeDialog) {
                Text("Close")
            }
        })
}


@Preview
@Composable
fun MainUiPreview() {
    val viewModel = MainViewModel(FakeConnectivityRepository())
    MainUi(
        viewModel = viewModel,
        requestBluetoothPermission = { viewModel.updateBluetoothPermissionState(true) },
        openUrlInBrowser = {}
    )
}

@Preview
@Composable
fun MainUiPreviewPermissionGranted() {
    val viewModel = MainViewModel(FakeConnectivityRepository())
    viewModel.updateBluetoothPermissionState(true)
    MainUi(
        viewModel = viewModel,
        requestBluetoothPermission = { viewModel.updateBluetoothPermissionState(true) },
        openUrlInBrowser = {}
    )
}