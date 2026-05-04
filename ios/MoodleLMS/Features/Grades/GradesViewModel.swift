import Foundation
import Observation

@Observable
@MainActor
final class GradesViewModel {
    var courses: UiState<[Course]> = .idle
    var items: UiState<[GradeItem]> = .idle
    var selectedCourseId: Int?

    private let coursesRepo: CoursesRepository
    private let gradesRepo: GradesRepository

    init(coursesRepo: CoursesRepository, gradesRepo: GradesRepository) {
        self.coursesRepo = coursesRepo
        self.gradesRepo = gradesRepo
    }

    func loadCourses() {
        courses = .loading
        Task {
            do {
                let result = try await coursesRepo.loadCourses()
                courses = .success(result)
                if let first = result.first {
                    selectedCourseId = first.id
                    loadItems(for: first.id)
                }
            } catch {
                courses = .error(error.asUserMessage)
            }
        }
    }

    func select(courseId: Int) {
        guard selectedCourseId != courseId else { return }
        selectedCourseId = courseId
        loadItems(for: courseId)
    }

    private func loadItems(for courseId: Int) {
        items = .loading
        Task {
            do {
                items = .success(try await gradesRepo.loadGrades(courseId: courseId))
            } catch {
                items = .error(error.asUserMessage)
            }
        }
    }
}
