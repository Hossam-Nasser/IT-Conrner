import Foundation

final class AuthRepository {
    private let client: MoodleClient
    private let tokenStore: KeychainStore

    static let devToken = "c269d73b8ec3265227714bf37f4dd2e4"
    static let devUserId = 1003

    init(client: MoodleClient, tokenStore: KeychainStore) {
        self.client = client
        self.tokenStore = tokenStore
    }

    func login(username: String, password: String, userId: Int) async throws {
        let token = try await client.login(username: username, password: password)
        tokenStore.save(token: token, userId: userId)
    }

    func useDevToken() {
        tokenStore.save(token: Self.devToken, userId: Self.devUserId)
    }
}
