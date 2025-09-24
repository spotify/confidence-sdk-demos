package com.example.demo.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegionDetector
    @Inject
    constructor() {
        // In a real implementation, you would use the context to get device settings
        fun detectRegion(): String = getRandomRegion()

        private fun getRandomRegion(): String {
            return REGIONS.random()
        }
    }

val REGIONS = listOf("na", "eu", "ap", "sa", "af")
