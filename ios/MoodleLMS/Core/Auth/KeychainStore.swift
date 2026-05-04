import Foundation
import Security

final class KeychainStore {
    private let service = "io.itcorner.moodle"
    private let tokenKey = "wstoken"
    private let userIdKey = "userid"

    func token() -> String? { read(tokenKey) }
    func userId() -> Int? { read(userIdKey).flatMap(Int.init) }

    func save(token: String, userId: Int) {
        write(tokenKey, value: token)
        write(userIdKey, value: String(userId))
    }

    func clear() {
        delete(tokenKey); delete(userIdKey)
    }

    // MARK: - Keychain primitives

    private func write(_ key: String, value: String) {
        let data = Data(value.utf8)
        let query: [CFString: Any] = [
            kSecClass: kSecClassGenericPassword,
            kSecAttrService: service,
            kSecAttrAccount: key,
        ]
        SecItemDelete(query as CFDictionary)
        var add = query
        add[kSecValueData] = data
        SecItemAdd(add as CFDictionary, nil)
    }

    private func read(_ key: String) -> String? {
        let query: [CFString: Any] = [
            kSecClass: kSecClassGenericPassword,
            kSecAttrService: service,
            kSecAttrAccount: key,
            kSecReturnData: true,
            kSecMatchLimit: kSecMatchLimitOne,
        ]
        var result: AnyObject?
        let status = SecItemCopyMatching(query as CFDictionary, &result)
        guard status == errSecSuccess, let data = result as? Data else { return nil }
        return String(data: data, encoding: .utf8)
    }

    private func delete(_ key: String) {
        let query: [CFString: Any] = [
            kSecClass: kSecClassGenericPassword,
            kSecAttrService: service,
            kSecAttrAccount: key,
        ]
        SecItemDelete(query as CFDictionary)
    }
}
