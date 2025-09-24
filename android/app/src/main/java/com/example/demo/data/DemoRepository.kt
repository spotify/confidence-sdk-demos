package com.example.demo.data

import dev.openfeature.kotlin.sdk.Client
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoRepository
    @Inject
    constructor(
        private val client: Client,
    ) {
        fun getWelcomeMessage(): Flow<String> =
            flow {
                emit("Choose a plan")
            }

        fun getPlanConfiguration(): Flow<PlanConfiguration> =
            flow {
                val enterpriseConfigEnabled = client.getBooleanValue("show-enterprise-plan.enabled", false)
                val enterpriseConfigPrice = client.getStringValue("show-enterprise-plan.price", "Contact Us")
                val highlightPremium =
                    client.getBooleanValue(
                        "subscription-highlight.enabled",
                        false,
                    )

                emit(
                    PlanConfiguration(
                        showEnterprisePlan = enterpriseConfigEnabled,
                        enterprisePrice = enterpriseConfigPrice,
                        highlightPremium = highlightPremium,
                    ),
                )
            }

        fun getSubscriptionPlans(config: PlanConfiguration): Flow<List<SubscriptionPlan>> =
            flow {
                val basePlans =
                    listOf(
                        SubscriptionPlan(
                            name = "Basic",
                            price = "$9",
                            features =
                                listOf(
                                    "Access to Feature A",
                                    "Standard support",
                                    "Basic usage limits",
                                    "Email notifications",
                                ),
                        ),
                        SubscriptionPlan(
                            name = "Premium",
                            price = "$19",
                            features =
                                listOf(
                                    "All Basic features",
                                    "Access to Feature B",
                                    "Priority support",
                                    "Advanced usage limits",
                                    "API access",
                                ),
                            isHighlighted = config.highlightPremium,
                        ),
                        SubscriptionPlan(
                            name = "Pro",
                            price = "$39",
                            features =
                                listOf(
                                    "All Premium features",
                                    "Access to Feature C",
                                    "Premium support",
                                    "Highest usage limits",
                                    "Custom integrations",
                                    "Advanced analytics",
                                ),
                        ),
                    )

                val plans =
                    if (config.showEnterprisePlan) {
                        basePlans +
                            SubscriptionPlan(
                                name = "Enterprise",
                                price = config.enterprisePrice,
                                features =
                                    listOf(
                                        "All Premium features",
                                        "Unlimited usage",
                                        "Dedicated account manager",
                                        "Custom solutions",
                                        "SLA guarantee",
                                    ),
                            )
                    } else {
                        basePlans
                    }

                emit(plans)
            }
    }
