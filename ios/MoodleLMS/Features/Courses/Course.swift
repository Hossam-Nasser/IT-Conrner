import Foundation

struct Course: Identifiable, Hashable {
    let id: Int
    let name: String
    let shortName: String?
    let progressPercent: Int?
    let imageURL: URL?

    init(id: Int, name: String, shortName: String?, progressPercent: Int?, imageURL: URL?) {
        self.id = id
        self.name = name
        self.shortName = shortName
        self.progressPercent = progressPercent
        self.imageURL = imageURL
    }

    static func attach(token: String?, to raw: String) -> URL? {
        guard let token, !token.isEmpty else { return URL(string: raw) }
        let separator = raw.contains("?") ? "&" : "?"
        return URL(string: "\(raw)\(separator)token=\(token)")
    }
}
