import Foundation

final class MoodleClient {
    private let baseURL: URL
    private let tokenStore: KeychainStore
    private let session: URLSession
    private let decoder: JSONDecoder

    init(baseURL: URL, tokenStore: KeychainStore, session: URLSession = .shared) {
        self.baseURL = baseURL
        self.tokenStore = tokenStore
        self.session = session
        self.decoder = JSONDecoder()
    }

    func callFunction<T: Decodable>(
        _ function: String,
        params: [String: String] = [:]
    ) async throws -> T {
        var components = URLComponents(url: baseURL.appendingPathComponent("webservice/rest/server.php"),
                                       resolvingAgainstBaseURL: false)!
        var query: [URLQueryItem] = [
            URLQueryItem(name: "wsfunction", value: function),
            URLQueryItem(name: "moodlewsrestformat", value: "json"),
        ]
        if let token = tokenStore.token() {
            query.append(URLQueryItem(name: "wstoken", value: token))
        } else {
            throw MoodleError.unauthenticated
        }
        for (k, v) in params { query.append(URLQueryItem(name: k, value: v)) }
        components.queryItems = query

        return try await get(components.url!)
    }

    func login(username: String, password: String) async throws -> String {
        var components = URLComponents(url: baseURL.appendingPathComponent("login/token.php"),
                                       resolvingAgainstBaseURL: false)!
        components.queryItems = [
            URLQueryItem(name: "username", value: username),
            URLQueryItem(name: "password", value: password),
            URLQueryItem(name: "service", value: "moodle_mobile_app"),
        ]

        struct LoginResponse: Decodable {
            let token: String?
            let error: String?
            let errorcode: String?
        }
        let resp: LoginResponse = try await get(components.url!)
        if let token = resp.token, !token.isEmpty { return token }
        throw MoodleError.api(code: resp.errorcode, message: resp.error ?? "Invalid credentials")
    }

    private func get<T: Decodable>(_ url: URL) async throws -> T {
        let data: Data
        do {
            (data, _) = try await session.data(from: url)
        } catch let urlError as URLError {
            throw MoodleError.transport(urlError)
        }
        if let envelope = try? decoder.decode(MoodleErrorEnvelope.self, from: data),
           envelope.exception != nil || envelope.errorcode != nil {
            throw MoodleError.api(code: envelope.errorcode,
                                  message: envelope.message ?? envelope.exception ?? "Moodle error")
        }
        do {
            return try decoder.decode(T.self, from: data)
        } catch let dec as DecodingError {
            throw MoodleError.decoding(dec)
        }
    }
}
