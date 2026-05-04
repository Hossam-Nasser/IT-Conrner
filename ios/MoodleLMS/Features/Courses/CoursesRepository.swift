import Foundation

final class CoursesRepository {
    private let client: MoodleClient
    private let tokenStore: KeychainStore

    init(client: MoodleClient, tokenStore: KeychainStore) {
        self.client = client
        self.tokenStore = tokenStore
    }

    func loadCourses() async throws -> [Course] {
        guard let userId = tokenStore.userId() else { throw MoodleError.unauthenticated }
        let dtos: [CourseDTO] = try await client.callFunction(
            "core_enrol_get_users_courses",
            params: ["userid": String(userId)]
        )
        let token = tokenStore.token()
        return dtos.map { $0.asDomain(token: token) }
    }
}
