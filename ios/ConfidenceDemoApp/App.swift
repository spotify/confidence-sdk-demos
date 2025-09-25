import SwiftUI

@main
struct ConfidenceDemoApp: App {
    @StateObject private var demoViewModel = DemoViewModel()

    var body: some Scene {
        WindowGroup {
            ContentView()
                .environmentObject(demoViewModel)
        }
    }
}