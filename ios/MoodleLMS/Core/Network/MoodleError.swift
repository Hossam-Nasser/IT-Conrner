import Foundation

struct MoodleErrorEnvelope: Decodable {
    let exception: String?
    let errorcode: String?
    let message: String?
}

enum MoodleError: LocalizedError {
    case transport(URLError)
    case decoding(DecodingError)
    case api(code: String?, message: String)
    case unauthenticated
    case unknown

    var errorDescription: String? {
        switch self {
        case .transport: return "No internet connection. Check your network and try again."
        case .decoding: return "We couldn't read the response from Moodle."
        case .api(_, let message): return message
        case .unauthenticated: return "Please sign in again."
        case .unknown: return "Something went wrong. Please try again."
        }
    }
}

extension Error {
    var asUserMessage: String {
        if let m = self as? MoodleError { return m.errorDescription ?? "Something went wrong." }
        if let url = self as? URLError { return MoodleError.transport(url).errorDescription ?? "Network error." }
        return MoodleError.unknown.errorDescription ?? "Something went wrong."
    }
}
