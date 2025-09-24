package com.example.demo.data

import android.content.Context
import android.util.Log
import com.spotify.confidence.ConfidenceFactory
import com.spotify.confidence.LoggingLevel
import com.spotify.confidence.openfeature.ConfidenceFeatureProvider
import com.spotify.confidence.openfeature.InitialisationStrategy
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.openfeature.kotlin.sdk.ImmutableContext
import dev.openfeature.kotlin.sdk.OpenFeatureAPI
import dev.openfeature.kotlin.sdk.Value
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

enum class InitializationState {
    LOADING,
    SUCCESS,
    ERROR,
}

@Singleton
class OpenFeatureInitializer
    @Inject
    constructor(
        @ApplicationContext private val applicationContext: Context,
        private val regionDetector: RegionDetector,
        private val regionPreferences: RegionPreferences,
    ) {
        private val _initializationState = MutableStateFlow(InitializationState.LOADING)
        val initializationState: StateFlow<InitializationState> = _initializationState.asStateFlow()

        suspend fun initialize() {
            try {
                val provider =
                    ConfidenceFeatureProvider.create(
                        ConfidenceFactory.create(
                            context = applicationContext,
                            clientSecret = "BEFwpwAWFupTtxEyt7ukdIc5hwAC7Lxc",
                            loggingLevel = LoggingLevel.VERBOSE,
                        ),
                        initialisationStrategy = InitialisationStrategy.FetchAndActivate,
                    )
                // Detect region and update preferences
                val detectedRegion = regionDetector.detectRegion()
                regionPreferences.updateDetectedRegion(detectedRegion)

                // Use current region (which may be overridden)
                val currentRegion = regionPreferences.currentRegion.value.code
                Log.d("OpenFeatureInit", "Using region: $currentRegion (detected: $detectedRegion)")

                // Confidence automatically generates a visitor ID if none is provided so we can just set a region
                val context = ImmutableContext(attributes = mapOf("region" to Value.String(currentRegion)))
                OpenFeatureAPI.setProviderAndWait(provider, context)
                _initializationState.value = InitializationState.SUCCESS
            } catch (e: Exception) {
                Log.e("OpenFeatureInit", "Error initializing OpenFeature", e)
                _initializationState.value = InitializationState.ERROR
            }
        }
    }
