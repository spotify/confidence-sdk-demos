import Foundation

struct SubscriptionPlan: Identifiable, Equatable {
    let id = UUID()
    let name: String
    let price: String
    let features: [String]
    let isHighlighted: Bool
    let isEnabled: Bool

    init(name: String, price: String, features: [String], isHighlighted: Bool = false, isEnabled: Bool = true) {
        self.name = name
        self.price = price
        self.features = features
        self.isHighlighted = isHighlighted
        self.isEnabled = isEnabled
    }
}

struct PlanConfiguration {
    let showEnterprisePlan: Bool
    let enterprisePrice: String
    let highlightPremium: Bool

    init(showEnterprisePlan: Bool = false, enterprisePrice: String = "-", highlightPremium: Bool = false) {
        self.showEnterprisePlan = showEnterprisePlan
        self.enterprisePrice = enterprisePrice
        self.highlightPremium = highlightPremium
    }
}