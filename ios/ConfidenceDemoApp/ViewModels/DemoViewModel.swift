import Foundation
import Combine
import OpenFeature

struct DemoUiState {
    var title: String = ""
    var subscriptionPlans: [SubscriptionPlan] = []
    var isLoading: Bool = true
    var initializationError: Bool = false
    var currentRegion: RegionInfo?
    var showRegionMenu: Bool = false
}

class DemoViewModel: ObservableObject {
    @Published var uiState = DemoUiState()

    private let repository = DemoRepository()
    private let openFeatureInitializer = OpenFeatureInitializer()
    private let regionPreferences = RegionPreferences()
    private var cancellables = Set<AnyCancellable>()

    init() {
        setupObservers()
        loadData()
    }

    private func setupObservers() {
        // Observe initialization state
        openFeatureInitializer.$initializationState
            .sink { [weak self] initState in
                switch initState {
                case .loading:
                    self?.uiState.isLoading = true
                    self?.uiState.initializationError = false
                case .success:
                    self?.loadPlansData()
                case .error:
                    self?.uiState.isLoading = false
                    self?.uiState.initializationError = true
                }
            }
            .store(in: &cancellables)

        // Observe region changes
        regionPreferences.$currentRegion
            .sink { [weak self] region in
                self?.uiState.currentRegion = region
                if let region = region {
                    self?.setEvaluationContext(region: region.code)
                }
            }
            .store(in: &cancellables)
    }

    private func loadData() {
        // Initial load will be triggered by initialization state observer
    }

    private func loadPlansData() {
        Publishers.CombineLatest(
            repository.getWelcomeMessage(),
            repository.getPlanConfiguration()
        )
        .flatMap { [weak self] title, config -> AnyPublisher<[SubscriptionPlan], Never> in
            self?.uiState.title = title
            return self?.repository.getSubscriptionPlans(config: config) ?? Just([]).eraseToAnyPublisher()
        }
        .receive(on: DispatchQueue.main)
        .sink { [weak self] plans in
            self?.uiState.subscriptionPlans = plans
            self?.uiState.isLoading = false
        }
        .store(in: &cancellables)
    }

    private func setEvaluationContext(region: String) {
        Task {
            let context = ImmutableContext(targetingKey: "user", structure: ImmutableStructure(attributes: ["region": Value.string(region)]))
            await OpenFeatureAPI.shared.setEvaluationContextAndWait(evaluationContext: context)

            DispatchQueue.main.async {
                self.loadPlansData()
            }
        }
    }

    func toggleRegionMenu() {
        uiState.showRegionMenu.toggle()
    }

    func selectRegion(_ regionCode: String?) {
        regionPreferences.setRegionOverride(regionCode)
        uiState.showRegionMenu = false

        // Reload data with new region
        if !uiState.isLoading {
            uiState.isLoading = true
            loadPlansData()
        }
    }

    func clearRegionOverride() {
        selectRegion(nil)
    }
}
