import Foundation
import Observation

@Observable
@MainActor
final class LoginViewModel {
    var username: String = "student1"
    var password: String = "Demo@12345"
    var userId: String = String(AuthRepository.devUserId)
    var isLoading: Bool = false
    var errorMessage: String?

    private let auth: AuthRepository
    private let onSuccess: () -> Void

    init(auth: AuthRepository, onSuccess: @escaping () -> Void) {
        self.auth = auth
        self.onSuccess = onSuccess
    }

    func submit() {
        guard !username.isEmpty, !password.isEmpty, let id = Int(userId) else {
            errorMessage = "Please fill in all fields (numeric user id)."
            return
        }
        errorMessage = nil
        isLoading = true
        Task {
            defer { isLoading = false }
            do {
                try await auth.login(username: username, password: password, userId: id)
                onSuccess()
            } catch {
                errorMessage = error.asUserMessage
            }
        }
    }

    func useDevToken() {
        auth.useDevToken()
        onSuccess()
    }
}
