import SwiftUI

struct DemoScreen: View {
    @EnvironmentObject var viewModel: DemoViewModel
    @Environment(\.materialTheme) var theme

    private let gridColumns = [
        GridItem(.adaptive(minimum: 280), spacing: 16)
    ]

    var body: some View {
        Group {
            if viewModel.uiState.isLoading {
                LoadingView()
            } else if viewModel.uiState.initializationError {
                ErrorView()
            } else {
                MainContentView()
            }
        }
        .materialDesignTheme()
    }

    private func LoadingView() -> some View {
        VStack(spacing: 16) {
            ProgressView()
                .scaleEffect(1.2)

            Text("Initializing feature flags...")
                .font(.body)
                .foregroundColor(theme.onSurfaceVariant)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(theme.surface)
    }

    private func ErrorView() -> some View {
        VStack(spacing: 16) {
            Text("Failed to initialize feature flags")
                .font(.title2)
                .fontWeight(.bold)
                .foregroundColor(theme.error)

            Text("Please check your configuration and try again")
                .font(.body)
                .foregroundColor(theme.onSurfaceVariant)
                .multilineTextAlignment(.center)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(theme.surface)
    }

    private func MainContentView() -> some View {
        ScrollView {
            LazyVGrid(columns: gridColumns, spacing: 16) {
                // Header with title and region menu
                HeaderView()
                    .gridCellColumns(2)

                // Subscription cards
                ForEach(viewModel.uiState.subscriptionPlans) { plan in
                    SubscriptionCard(plan: plan) {
                        // Handle plan selection
                        print("Selected plan: \(plan.name)")
                    }
                }
            }
            .padding(16)
        }
        .background(theme.surface)
        .onTapGesture {
            // Close region menu when tapping outside
            if viewModel.uiState.showRegionMenu {
                viewModel.toggleRegionMenu()
            }
        }
    }

    private func HeaderView() -> some View {
        VStack(spacing: 24) {
            HStack {
                Spacer()

                Text(viewModel.uiState.title)
                    .font(.largeTitle)
                    .fontWeight(.bold)
                    .foregroundColor(theme.onSurface)
                    .multilineTextAlignment(.center)

                Spacer()

                // Region overflow menu
                RegionOverflowMenu(
                    currentRegion: viewModel.uiState.currentRegion,
                    showRegionMenu: viewModel.uiState.showRegionMenu,
                    onToggleMenu: {
                        viewModel.toggleRegionMenu()
                    },
                    onSelectRegion: { regionCode in
                        viewModel.selectRegion(regionCode)
                    },
                    onClearOverride: {
                        viewModel.clearRegionOverride()
                    }
                )
            }
            .frame(maxWidth: .infinity)
        }
    }
}

extension GridItem {
    static func adaptive(minimum: CGFloat, spacing: CGFloat = 0) -> GridItem {
        GridItem(.adaptive(minimum: minimum), spacing: spacing)
    }
}

extension View {
    func gridCellColumns(_ count: Int) -> some View {
        // SwiftUI doesn't have direct equivalent to Android's GridItemSpan
        // This is a placeholder for grid spanning functionality
        self
    }
}