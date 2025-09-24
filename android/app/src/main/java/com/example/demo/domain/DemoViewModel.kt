package com.example.demo.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demo.data.DemoRepository
import com.example.demo.data.InitializationState
import com.example.demo.data.OpenFeatureInitializer
import com.example.demo.data.PlanConfiguration
import com.example.demo.data.RegionInfo
import com.example.demo.data.RegionPreferences
import com.example.demo.data.SubscriptionPlan
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.openfeature.kotlin.sdk.ImmutableContext
import dev.openfeature.kotlin.sdk.OpenFeatureAPI
import dev.openfeature.kotlin.sdk.Value
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DemoUiState(
    val title: String = "",
    val subscriptionPlans: List<SubscriptionPlan> = emptyList(),
    val isLoading: Boolean = true,
    val initializationError: Boolean = false,
    val currentRegion: RegionInfo? = null,
    val showRegionMenu: Boolean = false,
)

@HiltViewModel
class DemoViewModel
    @Inject
    constructor(
        private val repository: DemoRepository,
        private val openFeatureInitializer: OpenFeatureInitializer,
        private val regionPreferences: RegionPreferences,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(DemoUiState())
        val uiState: StateFlow<DemoUiState> = _uiState.asStateFlow()

        init {
            loadData()
            observeRegionChanges()
        }

        private fun loadData() {
            viewModelScope.launch {
                // Wait for OpenFeature initialization before loading data
                openFeatureInitializer.initializationState.collect { initState ->
                    when (initState) {
                        InitializationState.LOADING -> {
                            _uiState.value = _uiState.value.copy(isLoading = true, initializationError = false)
                        }
                        InitializationState.SUCCESS -> {
                            loadPlansData()
                        }
                        InitializationState.ERROR -> {
                            _uiState.value =
                                _uiState.value.copy(
                                    isLoading = false,
                                    initializationError = true,
                                )
                        }
                    }
                }
            }
        }

        private fun loadPlansData() {
            viewModelScope.launch {
                combine(
                    repository.getWelcomeMessage(),
                    repository.getPlanConfiguration(),
                ) { title, config ->
                    _uiState.value = _uiState.value.copy(title = title)
                    loadPlans(config)
                }.collect { }
            }
        }

        private fun loadPlans(config: PlanConfiguration) {
            viewModelScope.launch {
                repository.getSubscriptionPlans(config).collect { plans ->
                    _uiState.value =
                        _uiState.value.copy(
                            subscriptionPlans = plans,
                            isLoading = false,
                        )
                }
            }
        }

        private fun observeRegionChanges() {
            viewModelScope.launch {
                regionPreferences.currentRegion.collect { region ->
                    _uiState.value = _uiState.value.copy(isLoading = true)
                    OpenFeatureAPI.setEvaluationContextAndWait(ImmutableContext(attributes = mapOf("region" to Value.String(region.code))))
                    loadPlansData()
                    _uiState.value = _uiState.value.copy(currentRegion = region, isLoading = false)
                }
            }
        }

        fun toggleRegionMenu() {
            _uiState.value = _uiState.value.copy(showRegionMenu = !_uiState.value.showRegionMenu)
        }

        fun selectRegion(regionCode: String?) {
            regionPreferences.setRegionOverride(regionCode)
            _uiState.value = _uiState.value.copy(showRegionMenu = false)
            // Reload data with new region
            if (!_uiState.value.isLoading) {
                _uiState.value = _uiState.value.copy(isLoading = true)
                loadPlansData()
            }
        }

        fun clearRegionOverride() {
            selectRegion(null)
        }
    }
