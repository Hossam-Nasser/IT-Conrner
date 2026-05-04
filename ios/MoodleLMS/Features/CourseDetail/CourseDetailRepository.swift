import Foundation

struct Section: Identifiable, Hashable {
    let id: Int
    let name: String
    let modules: [Module]
}

struct Module: Identifiable, Hashable {
    let id: Int
    let name: String
    let url: URL?
    let iconURL: URL?
    let modName: String?
}

final class CourseDetailRepository {
    private let client: MoodleClient
    private let tokenStore: KeychainStore

    init(client: MoodleClient, tokenStore: KeychainStore) {
        self.client = client
        self.tokenStore = tokenStore
    }

    func loadSections(courseId: Int) async throws -> [Section] {
        let dtos: [SectionDTO] = try await client.callFunction(
            "core_course_get_contents",
            params: ["courseid": String(courseId)]
        )
        let token = tokenStore.token()
        return dtos.map { dto in
            let cleaned = dto.name.htmlUnescaped
            return Section(
                id: dto.id,
                name: cleaned.isEmpty ? "Untitled section" : cleaned,
                modules: (dto.modules ?? []).map { m in
                    Module(
                        id: m.id,
                        name: m.name.htmlUnescaped,
                        url: m.url.flatMap(URL.init(string:)),
                        iconURL: m.modicon.flatMap { Self.attach(token: token, raw: $0) },
                        modName: m.modname
                    )
                }
            )
        }
    }

    private static func attach(token: String?, raw: String) -> URL? {
        guard let token, !token.isEmpty else { return URL(string: raw) }
        let separator = raw.contains("?") ? "&" : "?"
        return URL(string: "\(raw)\(separator)token=\(token)")
    }
}
