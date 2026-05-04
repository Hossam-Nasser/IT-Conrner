import SwiftUI

struct CoursesView: View {
    @Environment(AppContainer.self) private var container
    @State private var viewModel: CoursesViewModel?
    @State private var query: String = ""

    var body: some View {
        Group {
            switch viewModel?.state ?? .idle {
            case .idle, .loading:
                LoadingView()
            case .error(let message):
                ErrorStateView(message: message) { viewModel?.load() }
            case .success(let courses):
                if courses.isEmpty {
                    EmptyStateView(message: "You're not enrolled in any courses yet.")
                } else {
                    list(courses: filtered(courses))
                }
            }
        }
        .navigationTitle("My Courses")
        .navigationDestination(for: Course.self) { course in
            CourseDetailView(course: course)
        }
        .task {
            if viewModel == nil {
                viewModel = CoursesViewModel(repository: container.coursesRepo)
                viewModel?.load()
            }
        }
    }

    private func filtered(_ courses: [Course]) -> [Course] {
        guard !query.isEmpty else { return courses }
        return courses.filter {
            $0.name.localizedCaseInsensitiveContains(query)
                || ($0.shortName ?? "").localizedCaseInsensitiveContains(query)
        }
    }

    @ViewBuilder
    private func list(courses: [Course]) -> some View {
        ScrollView {
            VStack(spacing: 14) {
                HStack {
                    Image(systemName: "magnifyingglass").foregroundStyle(.secondary)
                    TextField("Filter my courses", text: $query)
                        .textInputAutocapitalization(.never)
                        .autocorrectionDisabled()
                }
                .padding(.horizontal, 14)
                .padding(.vertical, 10)
                .background(Color(.secondarySystemBackground))
                .clipShape(Capsule())

                if courses.isEmpty {
                    EmptyStateView(message: "No courses match \"\(query)\".")
                        .frame(minHeight: 200)
                } else {
                    ForEach(courses) { course in
                        NavigationLink(value: course) {
                            CourseCard(course: course)
                        }
                        .buttonStyle(.plain)
                    }
                }
            }
            .padding(16)
        }
        .refreshable { viewModel?.load() }
    }
}

private struct CourseCard: View {
    let course: Course

    var body: some View {
        VStack(alignment: .leading, spacing: 10) {
            CourseImageView(
                url: course.imageURL,
                fallbackText: course.shortName ?? course.name,
                seed: course.id,
                cornerRadius: 14
            )
            VStack(alignment: .leading, spacing: 8) {
                Text(course.name)
                    .font(.headline)
                    .multilineTextAlignment(.leading)
                if let short = course.shortName {
                    CategoryChip(text: short)
                }
                if let pct = course.progressPercent {
                    ProgressView(value: Double(min(max(pct, 0), 100)) / 100)
                    Text("\(pct)% complete")
                        .font(.caption)
                        .foregroundStyle(.secondary)
                }
            }
            .padding(.horizontal, 4)
            .padding(.bottom, 4)
        }
        .padding(8)
        .background(Color(.secondarySystemBackground))
        .clipShape(RoundedRectangle(cornerRadius: 16))
    }
}

private struct CategoryChip: View {
    let text: String
    var body: some View {
        Text(text)
            .font(.caption.weight(.medium))
            .padding(.horizontal, 10)
            .padding(.vertical, 4)
            .background(Color.accentColor.opacity(0.15))
            .foregroundStyle(Color.accentColor)
            .clipShape(Capsule())
    }
}
