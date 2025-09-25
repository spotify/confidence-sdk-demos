import SwiftUI

struct SubscriptionCard: View {
    let plan: SubscriptionPlan
    let onSelectPlan: () -> Void

    @Environment(\.materialTheme) var theme

    var body: some View {
        ZStack {
            RoundedRectangle(cornerRadius: 16)
                .fill(theme.surface)
                .overlay(
                    RoundedRectangle(cornerRadius: 16)
                        .stroke(
                            plan.isHighlighted ? theme.primary : Color.gray.opacity(0.3),
                            lineWidth: plan.isHighlighted ? 2 : 1
                        )
                )
                .shadow(
                    color: Color.black.opacity(0.1),
                    radius: plan.isHighlighted ? 8 : 4,
                    x: 0,
                    y: 2
                )

            VStack(spacing: 0) {
                // Most Popular Badge
                if plan.isHighlighted {
                    HStack {
                        Spacer()
                        Text("Most Popular")
                            .font(.caption)
                            .fontWeight(.medium)
                            .foregroundColor(.white)
                            .padding(.horizontal, 12)
                            .padding(.vertical, 4)
                            .background(theme.primary)
                            .clipShape(RoundedRectangle(cornerRadius: 12))
                            .offset(y: -12)
                        Spacer()
                    }
                    .zIndex(1)
                }

                VStack(spacing: 16) {
                    VStack(spacing: 8) {
                        // Plan Name
                        Text(plan.name)
                            .font(.title2)
                            .fontWeight(.bold)
                            .foregroundColor(theme.onSurfaceVariant)
                            .padding(.top, plan.isHighlighted ? 0 : 16)

                        // Price
                        Text(plan.price)
                            .font(.title)
                            .fontWeight(.bold)
                            .foregroundColor(theme.onSurface)
                    }

                    // Features List
                    VStack(alignment: .leading, spacing: 8) {
                        ForEach(plan.features, id: \.self) { feature in
                            HStack(alignment: .top, spacing: 8) {
                                Image(systemName: "checkmark")
                                    .foregroundColor(theme.primary)
                                    .font(.caption)
                                    .frame(width: 16, height: 16)

                                Text(feature)
                                    .font(.body)
                                    .foregroundColor(theme.onSurfaceVariant)
                                    .multilineTextAlignment(.leading)

                                Spacer(minLength: 0)
                            }
                        }
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)

                    Spacer()

                    // Action Button
                    Button(action: onSelectPlan) {
                        Text(plan.isHighlighted ? "Get Started" : "Select Plan")
                            .fontWeight(.medium)
                            .foregroundColor(plan.isHighlighted ? .white : theme.primary)
                            .frame(maxWidth: .infinity)
                            .padding(.vertical, 12)
                            .background(
                                plan.isHighlighted ? theme.primary : Color.clear
                            )
                            .overlay(
                                RoundedRectangle(cornerRadius: 8)
                                    .stroke(theme.primary, lineWidth: plan.isHighlighted ? 0 : 1)
                            )
                            .clipShape(RoundedRectangle(cornerRadius: 8))
                    }
                }
                .padding(16)
            }
        }
        .frame(height: 350)
    }
}