package com.example.demo

import android.app.Application
import com.example.demo.data.OpenFeatureInitializer
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class DemoApplication : Application() {
    @Inject
    lateinit var openFeatureInitializer: OpenFeatureInitializer

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()

        // Initialize OpenFeature provider asynchronously
        // This happens once per app start and manages its own loading state
        applicationScope.launch {
            openFeatureInitializer.initialize()
        }
    }
}
