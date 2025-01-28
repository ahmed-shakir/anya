package se.supernovait.anya

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import se.supernovait.anya.di.initKoin

class AnyaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@AnyaApplication)
            androidLogger()
        }
    }
}
