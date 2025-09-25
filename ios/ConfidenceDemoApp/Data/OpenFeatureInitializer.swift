import Foundation
import Combine
import OpenFeature
import Confidence
import ConfidenceProvider

class OpenFeatureInitializer: ObservableObject {
    @Published var initializationState: InitializationState = .loading

    private var cancellables = Set<AnyCancellable>()

    init() {
        initialize()
    }

    private func initialize() {
        Task {
            // Configure Confidence provider with OpenFeature
            let confidence = Confidence.Builder(clientSecret: "BEFwpwAWFupTtxEyt7ukdIc5hwAC7Lxc", loggerLevel: .NONE).build()
            let provider = ConfidenceFeatureProvider(confidence: confidence)

            let initialContext = ImmutableContext(
                structure: ImmutableStructure(attributes: ["region" : Value.string("eu")])
              )

            await OpenFeatureAPI.shared.setProviderAndWait(provider: provider, initialContext: initialContext)

            DispatchQueue.main.async {
                self.initializationState = .success
            }
        }
    }
}
