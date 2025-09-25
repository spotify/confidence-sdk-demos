import SwiftUI

struct ContentView: View {
    @EnvironmentObject var viewModel: DemoViewModel

    var body: some View {
        DemoScreen()
    }
}