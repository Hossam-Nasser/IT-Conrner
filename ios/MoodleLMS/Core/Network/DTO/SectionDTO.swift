import Foundation

struct SectionDTO: Decodable {
    let id: Int
    let name: String
    let summary: String?
    let visible: Int?
    let modules: [ModuleDTO]?
}

struct ModuleDTO: Decodable {
    let id: Int
    let name: String
    let url: String?
    let modname: String?
    let modplural: String?
    let modicon: String?
    let visible: Int?
}
