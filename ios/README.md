# Confidence Demo iOS App

This is an iOS SwiftUI implementation of the Confidence feature flag demo app, designed to match the look and functionality of the Android version.

## Features

- **Subscription Plans Display**: Shows different subscription plans in a grid layout
- **Feature Flag Integration**: Uses Confidence OpenFeature provider for dynamic configuration
- **Region Selection**: Allows users to override their region for testing different configurations
- **Material Design Inspired UI**: Matches the Android app's visual design using SwiftUI
- **Loading & Error States**: Proper handling of initialization states

## Architecture

The app follows MVVM architecture with:
- **Models**: `SubscriptionPlan`, `RegionInfo`, `InitializationState`
- **ViewModels**: `DemoViewModel` for managing app state
- **Views**: SwiftUI components matching the Android Compose layout
- **Data Layer**: `DemoRepository` and `OpenFeatureInitializer`

## UI Components

- `DemoScreen`: Main screen with grid layout
- `SubscriptionCard`: Individual plan cards with Material Design styling
- `RegionOverflowMenu`: Dropdown menu for region selection
- `Theme`: Material Design color scheme and styling

## Running the App

1. Make sure you have Xcode 15+ installed
2. Navigate to the ios directory
3. Build and run using Swift Package Manager:

```bash
swift build
swift run
```

Or open in Xcode and run from there.

## Dependencies

- **Confidence**: Spotify's Confidence OpenFeature provider for Swift
- **OpenFeature**: Feature flag evaluation client

## Configuration

The app connects to Confidence using the OpenFeature provider. Make sure to configure your Confidence credentials in the appropriate environment variables or configuration files.