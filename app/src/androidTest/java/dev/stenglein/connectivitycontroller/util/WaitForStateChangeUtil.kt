package dev.stenglein.connectivitycontroller.util

import kotlinx.coroutines.delay

/**
 * Waits for a state change to occur.
 * @param stateProvider A function that returns the current state.
 * @param expectedState The state that is expected to be reached.
 * @param stepsToWait The number of steps to wait for the state change.
 *                    Default is 200 as managed Gradle devices tend to take a while to update
 *                    states, especially the network connectivity states.
 * @param timeMillisOneStep The time to wait between steps.
 * @return True if the expected state was reached, false otherwise.
 */
suspend fun waitForStateChange(
    stateProvider: () -> Boolean,
    expectedState: Boolean,
    stepsToWait: Int = 200,
    timeMillisOneStep: Long = 100
): Boolean {
    for (i in 1..stepsToWait) {
        if (stateProvider() == expectedState) {
            return true
        }
        delay(timeMillisOneStep)
    }
    return false
}