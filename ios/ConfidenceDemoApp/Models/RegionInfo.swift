import Foundation

struct RegionInfo: Identifiable, Equatable {
    let id = UUID()
    let code: String
    let name: String
    let isOverride: Bool

    init(code: String, name: String, isOverride: Bool = false) {
        self.code = code
        self.name = name
        self.isOverride = isOverride
    }
}

class RegionPreferences: ObservableObject {
    @Published var currentRegion: RegionInfo?
    @Published var hasOverride: Bool = false

    static let availableRegions = [
        RegionInfo(code: "US", name: "United States"),
        RegionInfo(code: "EU", name: "Europe"),
        RegionInfo(code: "ASIA", name: "Asia Pacific")
    ]

    init() {
        // Start with US as default
        self.currentRegion = RegionInfo(code: "US", name: "United States", isOverride: false)
    }

    func setRegionOverride(_ regionCode: String?) {
        if let regionCode = regionCode,
           let region = Self.availableRegions.first(where: { $0.code == regionCode }) {
            self.currentRegion = RegionInfo(code: region.code, name: region.name, isOverride: true)
            self.hasOverride = true
        } else {
            // Clear override - use auto-detected
            self.currentRegion = RegionInfo(code: "US", name: "United States", isOverride: false)
            self.hasOverride = false
        }
    }

    func updateDetectedRegion(_ regionCode: String) {
        if !hasOverride,
           let region = Self.availableRegions.first(where: { $0.code == regionCode }) {
            self.currentRegion = RegionInfo(code: region.code, name: region.name, isOverride: false)
        }
    }
}