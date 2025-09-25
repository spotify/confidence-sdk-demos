# Confidence Demo iOS App

This is the iOS SwiftUI version of the Confidence feature flag demo app that matches the Android version's look and functionality. It includes both an iOS Simulator app and a Swift Package for development.

## 🚀 Running the App

### Option 1: iOS Simulator (Recommended)
1. **Open the iOS project:** `open ConfidenceDemoiOS.xcodeproj`
2. **Select iOS Simulator:** Choose iPhone 15, iPhone 15 Pro, or any iOS 15+ device
3. **Run:** Press `Cmd+R` or click the "Run" button
4. The app will build and launch in the iOS Simulator with proper bundle ID

### Option 2: Swift Package (Development)
1. **Open Package:** `open Package.swift`
2. **Select target:** Choose "ConfidenceDemoApp" scheme
3. **Run on Mac:** Select "My Mac (designed for iOS)" to test SwiftUI components

### Option 3: Command Line
```bash
# Build the iOS project
xcodebuild -project ConfidenceDemoiOS.xcodeproj -scheme ConfidenceDemoiOS -destination 'platform=iOS Simulator,name=iPhone 15'

# Or build the Swift Package
swift build
```

## 📱 App Features

- **Material Design UI**: SwiftUI components styled to match the Android version
- **Feature Flag Integration**: Uses Confidence SDK with OpenFeature API
- **Subscription Plans**: Dynamic subscription plan display based on feature flags
- **Region Selection**: Test different regions to see how flags behave
- **Loading States**: Proper initialization and error handling

## 🏗️ Project Structure

```
# Unified Source Code (No Duplicates!)
ConfidenceDemoApp/              # Single source of truth
├── main.swift                  # Smart entry point (works with both SPM & Xcode)
├── ContentView.swift
├── UI/
│   ├── DemoScreen.swift        # Main demo screen
│   ├── Theme.swift             # Material Design theme
│   └── Components/
│       ├── SubscriptionCard.swift
│       └── RegionOverflowMenu.swift
├── ViewModels/
│   └── DemoViewModel.swift     # Main view model
├── Data/
│   ├── DemoRepository.swift    # Feature flag data layer
│   └── OpenFeatureInitializer.swift
└── Models/
    ├── SubscriptionPlan.swift
    ├── RegionInfo.swift
    └── InitializationState.swift

# Project Files
Package.swift                   # Swift Package Manager configuration
ConfidenceDemoiOS.xcodeproj/   # Xcode project (references ConfidenceDemoApp/ sources)
```

**Key Benefits:**
- ✅ **Single source of truth** - no duplicate files to maintain
- ✅ **Works with both SPM and Xcode** - same files, different build systems
- ✅ **Smart main.swift** - detects iOS vs macOS and behaves accordingly
- ✅ **Easy maintenance** - edit once, works everywhere

## 🔧 Dependencies

Both projects use Swift Package Manager dependencies:

- **Confidence**: Spotify's Confidence SDK (v1.4.4)
- **ConfidenceOpenFeature**: OpenFeature provider for Confidence

Dependencies are automatically resolved when you build in Xcode or run `swift package resolve`.

## 🎯 Usage

1. **Launch the app** in iOS Simulator (recommended) or Mac
2. **Watch loading state** as Confidence feature flags initialize
3. **View subscription plans** (Basic, Premium, and optionally Enterprise)
4. **Test regions** using the overflow menu (⋮) to see how flags change content
5. **See real-time updates** as feature flag configurations affect the UI

The app demonstrates real feature flag evaluation using the **Confidence backend with OpenFeature API integration**.

## 📋 Requirements

- **Xcode 16.0+** (or 15.0+)
- **iOS 15.0+** (for simulator/device)
- **macOS 12.0+** (for Swift Package development)
- **Swift 5.9+**

## 🔄 Development

### For iOS Simulator Development:
1. Open `ConfidenceDemoiOS.xcodeproj` in Xcode
2. Edit Swift files in the `ConfidenceDemoiOS/` directory
3. Build and run to test changes
4. Use Xcode's SwiftUI previews for rapid development

### For Swift Package Development:
1. Open `Package.swift` in Xcode
2. Edit Swift files in the `ConfidenceDemoApp/` directory
3. Use `swift build` for command line builds
4. Test on macOS with iOS-style UI

## 🔀 Choosing Between Projects

- **Use `ConfidenceDemoiOS.xcodeproj`** when:
  - Testing in iOS Simulator
  - Building for iOS devices
  - Need proper iOS app bundle and Info.plist
  - Want true iOS app behavior

- **Use `Package.swift`** when:
  - Rapid development and testing
  - Command line builds
  - Cross-platform development
  - Library/framework development