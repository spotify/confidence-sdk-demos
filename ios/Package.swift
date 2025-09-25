// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "ConfidenceDemo",
    platforms: [.iOS(.v15), .macOS(.v12)],
    products: [
        .executable(name: "ConfidenceDemoApp", targets: ["ConfidenceDemoApp"])
    ],
    dependencies: [
        .package(url: "https://github.com/spotify/confidence-sdk-swift.git", from: "1.4.4")
    ],
    targets: [
        .executableTarget(
            name: "ConfidenceDemoApp",
            dependencies: [
                .product(name: "Confidence", package: "confidence-sdk-swift"),
                .product(name: "ConfidenceOpenFeature", package: "confidence-sdk-swift")
            ],
            path: "ConfidenceDemoApp"
        )
    ]
)
