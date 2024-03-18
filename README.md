# Connectivity Controller

Connectivity Controller is an Android app that enables controlling WiFi and Bluetooth, overcoming
restrictions introduced in recent Android SDK versions.
External apps can trigger connectivity changes by sending broadcast messages to this app.

## Why?

Starting from Android 9 (SDK level 29), apps cannot enable or disable WiFi, and since Android 13 (
SDK level 33), apps also cannot control Bluetooth without user interaction.
Connectivity Controller addresses this by allowing external apps to target newer SDK levels while
still controlling WiFi and Bluetooth connectivity via broadcast messages sent to this app.

## How?

Connectivity Controller targets Android 8 (SDK level 26), the last version that allows apps to
directly control WiFi and Bluetooth.
By targeting this SDK level, the app can still perform these actions even on devices running newer
versions of Android.
The SDK level means that this app cannot use newer features introduced in later SDK levels, nor can
it be published to stores like Google Play.
However, other apps can target newer SDK levels and use new features while still controlling WiFi
and Bluetooth by sending broadcast messages to the Connectivity Controller app.

For WiFi, the app requires the `CHANGE_WIFI_STATE` and `ACCESS_WIFI_STATE` permissions, enabling it
to manage WiFi connectivity without further user interaction.

For Bluetooth, the app requires the `BLUETOOTH_CONNECT` permission.
On devices below Android 13, this permission is granted automatically.
However, on Android 13 and above, users must manually grant the permission, which is labeled "Nearby
Devices" in the app's settings.

## Feature Overview

The app has the following features:
Feature | Broadcast Action
--- | ---
Enable WiFi | `dev.stenglein.connectivitycontroller.ENABLE_WIFI`
Disable WiFi | `dev.stenglein.connectivitycontroller.DISABLE_WIFI`
Toggle WiFi | `dev.stenglein.connectivitycontroller.TOGGLE_WIFI`
Enable Bluetooth | `dev.stenglein.connectivitycontroller.ENABLE_BLUETOOTH`
Disable Bluetooth | `dev.stenglein.connectivitycontroller.DISABLE_BLUETOOTH`
Toggle Bluetooth | `dev.stenglein.connectivitycontroller.TOGGLE_BLUETOOTH`

In this context, toggling means enabling connectivity if disabled and vice versa.
Broadcast messages sent with the respective broadcast action trigger the respective connectivity
change.

## Getting Started

To use the app:

- Download the app:
    - Download the APK from
      the [releases](https://github.com/josteng/ConnectivityController/releases)
      page.
    - Alternatively, clone the repository and build the app using Android Studio.
- Grant Bluetooth permission:
    - Open the app and tap the "Grant Bluetooth permission" button.
    - Follow the prompt to grant the "Nearby devices" permission.
    - If this does not work, navigate to the system settings and manually grant the "Nearby devices"
      permission to this app.
    - Alternatively, the permission can be granted using
      the [Android Debug Bridge (ADB)](https://developer.android.com/tools/adb):
      ```shell
      adb shell pm grant dev.stenglein.connectivitycontroller android.permission.BLUETOOTH_CONNECT
      ```
- Test the app:
    - Use the buttons in the app to test whether the app can control WiFi and Bluetooth.
    - Send broadcast messages to the app as described below in the [Usage](#usage) section.

## Usage

You can control Connectivity Controller by sending broadcast messages.

- Using ADB:
  ```shell
  adb shell am broadcast -a dev.stenglein.connectivitycontroller.<BROADCAST-ACTION-HERE> -n dev.stenglein.connectivitycontroller/.ChangeConnectivityReceiver
  ```
- In an Android app:
  ```kotlin
  val intent = Intent("dev.stenglein.connectivitycontroller.<BROADCAST-ACTION-HERE>")
  intent.component = ComponentName("dev.stenglein.connectivitycontroller", "dev.stenglein.connectivitycontroller.ChangeConnectivityReceiver")
  sendBroadcast(intent)
  ```

Replace `<BROADCAST-ACTION-HERE>` with one of the actions listed in
the [Feature Overview](#feature-overview) table.

## License

This project is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file for details.
