package com.example.demo.data

data class SubscriptionPlan(
    val name: String,
    val price: String,
    val features: List<String>,
    val isHighlighted: Boolean = false,
    val isEnabled: Boolean = true,
)

data class PlanConfiguration(
    val showEnterprisePlan: Boolean = false,
    val enterprisePrice: String = "-",
    val highlightPremium: Boolean = false,
)
