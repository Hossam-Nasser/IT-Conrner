import SwiftUI

struct CourseDetailView: View {
    @Environment(AppContainer.self) private var container
    @Environment(\.openURL) private var openURL
    let course: Course
    @State private var viewModel: CourseDetailViewModel?

    var body: some View {
        Group {
            switch viewModel?.state ?? .idle {
            case .idle, .loading:
                LoadingView()
            case .error(let message):
                ErrorStateView(message: message) { viewModel?.load() }
            case .success(let sections):
                if sections.isEmpty {
                    EmptyStateView(message: "This course has no sections yet.")
                } else {
                    sectionsList(sections)
                }
            }
        }
        .navigationTitle(course.name)
        .navigationBarTitleDisplayMode(.inline)
        .task {
            if viewModel == nil {
                viewModel = CourseDetailViewModel(course: course, repository: container.courseDetailRepo)
                viewModel?.load()
            }
        }
    }

    @ViewBuilder
    private func sectionsList(_ sections: [Section]) -> some View {
        List {
            ForEach(sections) { section in
                SwiftUI.Section {
                    if section.modules.isEmpty {
                        Text("No items in this section")
                            .font(.footnote)
                            .foregroundStyle(.secondary)
                    } else {
                        ForEach(section.modules) { module in
                            ModuleRow(module: module) {
                                if let url = module.url { openURL(url) }
                            }
                        }
                    }
                } header: {
                    HStack {
                        Text(section.name)
                            .font(.headline)
                        Spacer()
                        Text("\(section.modules.count) \(section.modules.count == 1 ? "item" : "items")")
                            .font(.caption.weight(.medium))
                            .padding(.horizontal, 8)
                            .padding(.vertical, 2)
                            .background(Color.accentColor.opacity(0.15))
                            .foregroundStyle(Color.accentColor)
                            .clipShape(Capsule())
                    }
                }
            }
        }
        .listStyle(.insetGrouped)
    }
}

private struct ModuleRow: View {
    let module: Module
    let onTap: () -> Void

    var body: some View {
        Button(action: onTap) {
            HStack(spacing: 12) {
                if let iconURL = module.iconURL {
                    AsyncImage(url: iconURL) { phase in
                        switch phase {
                        case .success(let image): image.resizable().scaledToFit()
                        default: Image(systemName: "doc.text").foregroundStyle(.secondary)
                        }
                    }
                    .frame(width: 24, height: 24)
                }
                Text(module.name)
                    .foregroundStyle(.primary)
                Spacer()
                if let mod = module.modName {
                    Text(mod.capitalized)
                        .font(.caption)
                        .foregroundStyle(.secondary)
                }
            }
        }
        .disabled(module.url == nil)
    }
}
