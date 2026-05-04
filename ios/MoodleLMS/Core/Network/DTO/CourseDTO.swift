import Foundation

struct CourseDTO: Decodable {
    let id: Int
    let fullname: String
    let shortname: String?
    let progress: Double?
    let courseimage: String?
    let overviewfiles: [OverviewFileDTO]?
}

struct OverviewFileDTO: Decodable {
    let fileurl: String?
    let mimetype: String?
}

extension CourseDTO {
    func asDomain(token: String?) -> Course {
        let raw = (courseimage?.isEmpty == false ? courseimage : nil)
            ?? overviewfiles?.first(where: { ($0.mimetype ?? "").hasPrefix("image/") })?.fileurl
            ?? overviewfiles?.first?.fileurl
        let url = raw.flatMap { Course.attach(token: token, to: $0) }
        return Course(
            id: id,
            name: fullname.htmlUnescaped,
            shortName: shortname?.isEmpty == false ? shortname : nil,
            progressPercent: progress.map { Int($0) },
            imageURL: url
        )
    }
}
