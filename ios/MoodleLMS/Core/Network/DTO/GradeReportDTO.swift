import Foundation

struct GradeReportResponse: Decodable {
    let usergrades: [UserGradeDTO]
}

struct UserGradeDTO: Decodable {
    let courseid: Int
    let userid: Int
    let gradeitems: [GradeItemDTO]
}

struct GradeItemDTO: Decodable {
    let id: Int
    let itemname: String?
    let itemtype: String?
    let gradeformatted: String?
    let percentageformatted: String?
    let feedback: String?
}
