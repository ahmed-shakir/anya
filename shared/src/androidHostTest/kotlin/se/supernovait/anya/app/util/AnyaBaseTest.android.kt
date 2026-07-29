package se.supernovait.anya.app.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import se.supernovait.anya.app.di.getTestModule
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
actual abstract class AnyaBaseTest actual constructor() : KoinTest {

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    actual fun startTestKoin() {
        Dispatchers.setMain(testDispatcher)
        startKoin {
            modules(getTestModule())
        }
    }

    @AfterTest
    actual fun stopTestKoin() {
        stopKoin()
        Dispatchers.resetMain()
    }

    actual fun advanceTime() {
        testDispatcher.scheduler.advanceUntilIdle()
    }
}
