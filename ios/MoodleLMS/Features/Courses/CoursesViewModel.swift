import Foundation
import Observation

@Observable
@MainActor
final class CoursesViewModel {
    var state: UiState<[Course]> = .idle
    private let repository: CoursesRepository

    init(repository: CoursesRepository) {
        self.repository = repository
    }

    func load() {
        state = .loading
        Task {
            do {
                let courses = try await repository.loadCourses()
                state = .success(courses)
            } catch {
                state = .error(error.asUserMessage)
            }
        }
    }
}
