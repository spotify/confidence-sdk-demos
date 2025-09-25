import SwiftUI

struct RegionOverflowMenu: View {
    let currentRegion: RegionInfo?
    let showRegionMenu: Bool
    let onToggleMenu: () -> Void
    let onSelectRegion: (String) -> Void
    let onClearOverride: () -> Void

    @Environment(\.materialTheme) var theme

    var body: some View {
        VStack {
            Button(action: onToggleMenu) {
                Image(systemName: "ellipsis")
                    .foregroundColor(theme.onSurface)
                    .font(.title2)
                    .rotationEffect(.degrees(90))
            }

            if showRegionMenu {
                VStack(alignment: .leading, spacing: 0) {
                    // Region header
                    VStack(alignment: .leading, spacing: 4) {
                        Text("Region: \(currentRegion?.name ?? "Unknown")")
                            .font(.caption)
                            .foregroundColor(theme.onSurfaceVariant)

                        if currentRegion?.isOverride == true {
                            Text("(Override)")
                                .font(.caption2)
                                .foregroundColor(theme.onSurfaceVariant)
                        }
                    }
                    .padding(.horizontal, 16)
                    .padding(.vertical, 8)

                    Divider()

                    // Region options
                    ForEach(RegionPreferences.availableRegions) { region in
                        Button(action: {
                            onSelectRegion(region.code)
                        }) {
                            HStack {
                                Text(region.name)
                                    .foregroundColor(theme.onSurface)

                                Spacer()

                                if currentRegion?.code == region.code {
                                    Image(systemName: "checkmark")
                                        .foregroundColor(theme.primary)
                                        .font(.caption)
                                }
                            }
                            .padding(.horizontal, 16)
                            .padding(.vertical, 12)
                        }
                    }

                    if currentRegion?.isOverride == true {
                        Divider()

                        Button(action: onClearOverride) {
                            Text("Use Auto-detected")
                                .foregroundColor(theme.onSurface)
                                .padding(.horizontal, 16)
                                .padding(.vertical, 12)
                        }
                    }
                }
                .background(theme.surface)
                .clipShape(RoundedRectangle(cornerRadius: 8))
                .shadow(color: Color.black.opacity(0.1), radius: 4, x: 0, y: 2)
                .overlay(
                    RoundedRectangle(cornerRadius: 8)
                        .stroke(Color.gray.opacity(0.2), lineWidth: 1)
                )
                .zIndex(1)
            }
        }
    }
}