import SwiftUI

struct RootView: View {
    @Environment(AppContainer.self) private var container

    var body: some View {
        if container.isLoggedIn {
            MainTabs()
        } else {
            LoginView()
        }
    }
}

private struct MainTabs: View {
    var body: some View {
        TabView {
            NavigationStack {
                CoursesView()
            }
            .tabItem { Label("Courses", systemImage: "book") }

            NavigationStack {
                GradesView()
            }
            .tabItem { Label("Grades", systemImage: "star") }
        }
    }
}
