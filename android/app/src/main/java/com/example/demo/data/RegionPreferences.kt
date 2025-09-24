package com.example.demo.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

data class RegionInfo(
    val code: String,
    val name: String,
    val isOverride: Boolean = false,
)

@Singleton
class RegionPreferences
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) {
        private val prefs: SharedPreferences = context.getSharedPreferences("region_prefs", Context.MODE_PRIVATE)

        private val _currentRegion = MutableStateFlow(getStoredRegion())
        val currentRegion: StateFlow<RegionInfo> = _currentRegion.asStateFlow()

        companion object {
            private const val KEY_OVERRIDE_REGION = "override_region"

            val AVAILABLE_REGIONS =
                listOf(
                    RegionInfo("na", "North America"),
                    RegionInfo("eu", "Europe"),
                    RegionInfo("ap", "Asia Pacific"),
                    RegionInfo("sa", "South America"),
                    RegionInfo("af", "Africa"),
                )
        }

        private fun getStoredRegion(): RegionInfo {
            val overrideRegion = prefs.getString(KEY_OVERRIDE_REGION, null)
            return if (overrideRegion != null) {
                AVAILABLE_REGIONS.find { it.code == overrideRegion }?.copy(isOverride = true)
                    ?: AVAILABLE_REGIONS.first()
            } else {
                AVAILABLE_REGIONS.first() // Will be replaced with detected region
            }
        }

        fun setRegionOverride(regionCode: String?) {
            if (regionCode != null) {
                prefs.edit { putString(KEY_OVERRIDE_REGION, regionCode) }
                val region = AVAILABLE_REGIONS.find { it.code == regionCode }
                if (region != null) {
                    _currentRegion.value = region.copy(isOverride = true)
                }
            } else {
                prefs.edit { remove(KEY_OVERRIDE_REGION) }
                // Will be updated with detected region
            }
        }

        fun updateDetectedRegion(detectedRegionCode: String) {
            // Only update if there's no override
            if (prefs.getString(KEY_OVERRIDE_REGION, null) == null) {
                val region =
                    AVAILABLE_REGIONS.find { it.code == detectedRegionCode }
                        ?: AVAILABLE_REGIONS.first()
                _currentRegion.value = region
            }
        }

        fun hasOverride(): Boolean {
            return prefs.getString(KEY_OVERRIDE_REGION, null) != null
        }
    }
