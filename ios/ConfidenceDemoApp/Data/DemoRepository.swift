import Foundation
import Combine
import OpenFeature

class DemoRepository: ObservableObject {
    private let openFeatureClient = OpenFeatureAPI.shared.getClient()

    func getWelcomeMessage() -> AnyPublisher<String, Never> {
        return Future { promise in
            Task {
                // This flag doesn't exist so we expect to always fall back to defaults
                let welcomeMessage = self.openFeatureClient.getStringValue(key: "welcome-message", defaultValue: "Welcome to Confidence Demo")
                promise(.success(welcomeMessage))
            }
        }
        .eraseToAnyPublisher()
    }

    func getPlanConfiguration() -> AnyPublisher<PlanConfiguration, Never> {
        return Future { promise in
            Task {
                let showEnterprise = self.openFeatureClient.getBooleanValue(key: "show-enterprise-plan", defaultValue: false)
                let enterprisePrice = self.openFeatureClient.getStringValue(key: "enterprise-price", defaultValue: "Contact Sales")
                let highlightPremium = self.openFeatureClient.getBooleanValue(key: "highlight-premium", defaultValue: false)

                let config = PlanConfiguration(
                    showEnterprisePlan: showEnterprise,
                    enterprisePrice: enterprisePrice,
                    highlightPremium: highlightPremium
                )
                promise(.success(config))
            }
        }
        .eraseToAnyPublisher()
    }

    func getSubscriptionPlans(config: PlanConfiguration) -> AnyPublisher<[SubscriptionPlan], Never> {
        return Future { promise in
            var plans: [SubscriptionPlan] = []

            // Basic Plan
            plans.append(SubscriptionPlan(
                name: "Basic",
                price: "$9.99/month",
                features: [
                    "Access to basic features",
                    "Email support",
                    "1 user account"
                ]
            ))

            // Premium Plan
            plans.append(SubscriptionPlan(
                name: "Premium",
                price: "$19.99/month",
                features: [
                    "All basic features",
                    "Priority support",
                    "5 user accounts",
                    "Advanced analytics"
                ],
                isHighlighted: config.highlightPremium
            ))

            // Enterprise Plan (conditional)
            if config.showEnterprisePlan {
                plans.append(SubscriptionPlan(
                    name: "Enterprise",
                    price: config.enterprisePrice,
                    features: [
                        "All premium features",
                        "24/7 dedicated support",
                        "Unlimited users",
                        "Custom integrations",
                        "SLA guarantee"
                    ]
                ))
            }

            promise(.success(plans))
        }
        .eraseToAnyPublisher()
    }
}
