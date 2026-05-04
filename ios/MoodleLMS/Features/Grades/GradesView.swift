import SwiftUI

struct GradesView: View {
    @Environment(AppContainer.self) private var container
    @State private var viewModel: GradesViewModel?

    var body: some View {
        Group {
            switch viewModel?.courses ?? .idle {
            case .idle, .loading:
                LoadingView()
            case .error(let message):
                ErrorStateView(message: message) { viewModel?.loadCourses() }
            case .success(let courses):
                if courses.isEmpty {
                    EmptyStateView(message: "No courses to show grades for.")
                } else {
                    content(courses: courses)
                }
            }
        }
        .navigationTitle("Grades")
        .task {
            if viewModel == nil {
                viewModel = GradesViewModel(
                    coursesRepo: container.coursesRepo,
                    gradesRepo: container.gradesRepo
                )
                viewModel?.loadCourses()
            }
        }
    }

    @ViewBuilder
    private func content(courses: [Course]) -> some View {
        let selection = Binding<Int>(
            get: { viewModel?.selectedCourseId ?? courses.first?.id ?? 0 },
            set: { viewModel?.select(courseId: $0) }
        )
        VStack(spacing: 0) {
            Picker("Course", selection: selection) {
                ForEach(courses) { course in
                    Text(course.name).tag(course.id)
                }
            }
            .pickerStyle(.menu)
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(.horizontal, 16)
            .padding(.vertical, 12)
            .background(Color(.secondarySystemBackground))
            .clipShape(RoundedRectangle(cornerRadius: 12))
            .padding(16)

            switch viewModel?.items ?? .idle {
            case .idle, .loading:
                LoadingView()
            case .error(let message):
                ErrorStateView(message: message) {
                    if let id = viewModel?.selectedCourseId { viewModel?.select(courseId: id) }
                }
            case .success(let items):
                if items.isEmpty {
                    EmptyStateView(message: "No grades have been recorded for this course.")
                } else {
                    List(items) { item in
                        GradeRow(item: item)
                            .listRowInsets(EdgeInsets(top: 12, leading: 16, bottom: 12, trailing: 16))
                    }
                    .listStyle(.plain)
                }
            }
        }
    }
}

private struct GradeRow: View {
    let item: GradeItem

    var body: some View {
        HStack {
            Text(item.name)
                .font(.body)
            Spacer()
            if let display = item.percentage ?? item.grade {
                Text(display)
                    .font(.footnote.weight(.medium))
                    .padding(.horizontal, 12)
                    .padding(.vertical, 4)
                    .background(Color.accentColor.opacity(0.15))
                    .foregroundStyle(Color.accentColor)
                    .clipShape(Capsule())
            } else {
                Text("Not graded yet")
                    .font(.footnote)
                    .foregroundStyle(.secondary)
            }
        }
    }
}
