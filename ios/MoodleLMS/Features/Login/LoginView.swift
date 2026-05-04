import SwiftUI

struct LoginView: View {
    @Environment(AppContainer.self) private var container
    @State private var viewModel: LoginViewModel?

    var body: some View {
        VStack(spacing: 16) {
            Spacer()
            Text("Sign in to Moodle")
                .font(.title2).bold()

            if let vm = viewModel {
                TextField("Username", text: Binding(get: { vm.username }, set: { vm.username = $0 }))
                    .textFieldStyle(.roundedBorder)
                    .textInputAutocapitalization(.never)
                    .autocorrectionDisabled()

                SecureField("Password", text: Binding(get: { vm.password }, set: { vm.password = $0 }))
                    .textFieldStyle(.roundedBorder)

                TextField("User ID", text: Binding(get: { vm.userId }, set: { vm.userId = $0.filter(\.isNumber) }))
                    .textFieldStyle(.roundedBorder)
                    .keyboardType(.numberPad)

                if let message = vm.errorMessage {
                    Text(message)
                        .foregroundStyle(.red)
                        .font(.footnote)
                }

                Button(action: vm.submit) {
                    if vm.isLoading {
                        ProgressView()
                    } else {
                        Text("Sign in").frame(maxWidth: .infinity)
                    }
                }
                .buttonStyle(.borderedProminent)
                .disabled(vm.isLoading)

                Button("Use dev token instead", action: vm.useDevToken)
                    .buttonStyle(.bordered)
            }
            Spacer()
        }
        .padding(24)
        .task {
            if viewModel == nil {
                viewModel = LoginViewModel(auth: container.auth) {
                    container.didLogIn()
                }
            }
        }
    }
}
