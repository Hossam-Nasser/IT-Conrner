import SwiftUI

struct CourseImageView: View {
    let url: URL?
    let fallbackText: String
    let seed: Int
    var cornerRadius: CGFloat = 12

    private static let palette: [(Color, Color)] = [
        (Color(red: 0.97, green: 0.50, blue: 0.07), Color(red: 0.72, green: 0.37, blue: 0.03)),
        (Color(red: 0.10, green: 0.46, blue: 0.82), Color(red: 0.05, green: 0.28, blue: 0.63)),
        (Color(red: 0.22, green: 0.56, blue: 0.24), Color(red: 0.10, green: 0.37, blue: 0.13)),
        (Color(red: 0.56, green: 0.14, blue: 0.67), Color(red: 0.29, green: 0.08, blue: 0.55)),
        (Color(red: 0.83, green: 0.18, blue: 0.18), Color(red: 0.56, green: 0.00, blue: 0.00)),
        (Color(red: 0.00, green: 0.54, blue: 0.48), Color(red: 0.00, green: 0.30, blue: 0.25)),
    ]

    var body: some View {
        ZStack {
            if let url = url, !isSVG(url) {
                AsyncImage(url: url) { phase in
                    switch phase {
                    case .success(let image):
                        image.resizable().aspectRatio(contentMode: .fill)
                    default:
                        placeholder
                    }
                }
            } else {
                placeholder
            }
        }
        .aspectRatio(16/9, contentMode: .fill)
        .clipShape(RoundedRectangle(cornerRadius: cornerRadius))
    }

    private var placeholder: some View {
        let pair = Self.palette[abs(seed) % Self.palette.count]
        return LinearGradient(colors: [pair.0, pair.1], startPoint: .topLeading, endPoint: .bottomTrailing)
            .overlay(
                Text(String(fallbackText.prefix(2)).uppercased())
                    .font(.system(size: 28, weight: .semibold))
                    .foregroundStyle(.white)
            )
    }

    private func isSVG(_ url: URL) -> Bool {
        url.pathExtension.lowercased() == "svg" || url.absoluteString.contains(".svg")
    }
}
