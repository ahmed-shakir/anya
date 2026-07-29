package se.supernovait.anya.app.util

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import se.supernovait.anya.app.di.getTestModule
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@OptIn(ExperimentalCoroutinesApi::class)
actual abstract class AnyaBaseTest actual constructor() : KoinTest {

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    actual fun startTestKoin() {
        // On iOS, we avoid setMain(testDispatcher) for UI tests to prevent deadlocks
        // with the native main loop used by runComposeUiTest.
        // ViewModels will use the native Dispatchers.Main.
        startKoin {
            modules(getTestModule())
        }
    }

    @AfterTest
    actual fun stopTestKoin() {
        stopKoin()
    }

    actual fun advanceTime() {
        // Since we don't setMain, this only advances the local testDispatcher.
        // For UI tests, rely on Compose's internal waiting or yield().
        testDispatcher.scheduler.advanceUntilIdle()
    }
}
