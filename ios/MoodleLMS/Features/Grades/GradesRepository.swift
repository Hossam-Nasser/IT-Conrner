import Foundation

struct GradeItem: Identifiable, Hashable {
    let id: Int
    let name: String
    let grade: String?
    let percentage: String?
}

final class GradesRepository {
    private let client: MoodleClient
    private let tokenStore: KeychainStore

    init(client: MoodleClient, tokenStore: KeychainStore) {
        self.client = client
        self.tokenStore = tokenStore
    }

    func loadGrades(courseId: Int) async throws -> [GradeItem] {
        guard let userId = tokenStore.userId() else { throw MoodleError.unauthenticated }
        let response: GradeReportResponse = try await client.callFunction(
            "gradereport_user_get_grade_items",
            params: ["userid": String(userId), "courseid": String(courseId)]
        )
        return (response.usergrades.first?.gradeitems ?? []).map { dto -> GradeItem in
            let rawName = (dto.itemname?.isEmpty == false ? dto.itemname! :
                            (dto.itemtype?.lowercased() == "course" ? "Course total" : "Grade item"))
            return GradeItem(
                id: dto.id,
                name: rawName.htmlUnescaped,
                grade: clean(dto.gradeformatted),
                percentage: clean(dto.percentageformatted)
            )
        }
    }

    /// Moodle returns "-" or "- %" for ungraded items. Display nothing instead.
    private func clean(_ value: String?) -> String? {
        guard let trimmed = value?.trimmingCharacters(in: .whitespaces), !trimmed.isEmpty else { return nil }
        if trimmed == "-" { return nil }
        let stripped = trimmed.replacingOccurrences(of: "[\\s%-]", with: "", options: .regularExpression)
        return stripped.isEmpty ? nil : trimmed
    }
}
