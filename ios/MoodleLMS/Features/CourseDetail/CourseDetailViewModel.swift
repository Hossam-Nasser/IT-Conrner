import Foundation
import Observation

@Observable
@MainActor
final class CourseDetailViewModel {
    var state: UiState<[Section]> = .idle
    let course: Course
    private let repository: CourseDetailRepository

    init(course: Course, repository: CourseDetailRepository) {
        self.course = course
        self.repository = repository
    }

    func load() {
        state = .loading
        Task {
            do {
                let sections = try await repository.loadSections(courseId: course.id)
                state = .success(sections)
            } catch {
                state = .error(error.asUserMessage)
            }
        }
    }
}
