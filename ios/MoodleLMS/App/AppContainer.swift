import Foundation
import Observation

@Observable
final class AppContainer {
    let tokenStore: KeychainStore
    let client: MoodleClient
    let auth: AuthRepository
    let coursesRepo: CoursesRepository
    let courseDetailRepo: CourseDetailRepository
    let gradesRepo: GradesRepository

    private(set) var isLoggedIn: Bool

    init(tokenStore: KeychainStore, client: MoodleClient) {
        self.tokenStore = tokenStore
        self.client = client
        self.auth = AuthRepository(client: client, tokenStore: tokenStore)
        self.coursesRepo = CoursesRepository(client: client, tokenStore: tokenStore)
        self.courseDetailRepo = CourseDetailRepository(client: client, tokenStore: tokenStore)
        self.gradesRepo = GradesRepository(client: client, tokenStore: tokenStore)
        self.isLoggedIn = tokenStore.token() != nil
    }

    static func live() -> AppContainer {
        let store = KeychainStore()
        let client = MoodleClient(baseURL: URL(string: "https://moodle.itcorner.qzz.io")!, tokenStore: store)
        return AppContainer(tokenStore: store, client: client)
    }

    func didLogIn() {
        isLoggedIn = true
    }

    func logOut() {
        tokenStore.clear()
        isLoggedIn = false
    }
}
