package se.supernovait.anya.app.util

import org.koin.test.KoinTest

expect abstract class AnyaBaseTest() : KoinTest {
    fun startTestKoin()
    fun stopTestKoin()
    fun advanceTime()
}
