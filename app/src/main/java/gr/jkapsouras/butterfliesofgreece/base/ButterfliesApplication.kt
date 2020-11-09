package gr.jkapsouras.butterfliesofgreece.base

import android.app.Application
import android.content.res.Configuration
import gr.jkapsouras.butterfliesofgreece.base.di.butterfliesModule
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class ButterfliesApplication : Application() {
    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    override fun onCreate() {
        super.onCreate()
        // Required initialization logic here!
        startKoin {
            androidContext(this@ButterfliesApplication)
            androidLogger(level = Level.ERROR)
            modules(
                butterfliesModule
            )
        }
    }

    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    override fun onConfigurationChanged ( newConfig : Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    override fun onLowMemory() {
        super.onLowMemory()
    }
}