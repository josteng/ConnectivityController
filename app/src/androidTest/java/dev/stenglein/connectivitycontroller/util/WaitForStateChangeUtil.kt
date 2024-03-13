package dev.stenglein.connectivitycontroller.util

import kotlinx.coroutines.delay

/**
 * Waits for a state change to occur.
 * @param stateProvider A function that returns the current state.
 * @param expectedState The state that is expected to be reached.
 * @param stepsToWait The number of steps to wait for the state change.
 * @param timeMillisToWait The time to wait between steps.
 * @return True if the expected state was reached, false otherwise.
 */
suspend fun waitForStateChange(
    stateProvider: () -> Boolean,
    expectedState: Boolean,
    stepsToWait: Int = 100,
    timeMillisToWait: Long = 100
): Boolean {
    for (i in 0..<stepsToWait) {
        if (stateProvider() == expectedState) {
            return true
        }
        delay(timeMillisToWait)
    }
    return false
}