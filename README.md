# IT-Corner Moodle LMS — Native Mobile Apps

Two native mobile apps (iOS / SwiftUI and Android / Jetpack Compose) that integrate with the IT-Corner Moodle LMS via its REST web service.

Both apps implement three screens — **Courses**, **Course Details**, **Grades** — on top of the same MVVM shape.

## Demo

A short screen recording of the apps running end-to-end (login → courses → course detail with modules → grades).

https://drive.google.com/file/d/1a2v3chpp9odnxkLH1PYmPaGiPGEvcWOL/view?usp=sharing

## Repo layout

```
IT-Corner/
├── android/    Android Studio project (Kotlin + Jetpack Compose)
├── ios/        Xcode project (Swift + SwiftUI) — see ios/SETUP.md
├── video.mp4   Demo recording
└── README.md
```

## Setup

### Android
1. Open `android/` in Android Studio (Hedgehog or newer).
2. Let Gradle sync. Min SDK 24, target/compile SDK 35.
3. Run on an API 34 emulator or a physical device.
4. On the Login screen, sign in with `student1` / `Demo@12345`, **or** tap **"Use dev token"** to skip with the pre-generated token.

### iOS
The Swift sources are ready; the `.xcodeproj` is generated on first open in Xcode (see [`ios/SETUP.md`](ios/SETUP.md) for the one-time steps).

1. On a Mac, open Xcode 15+ and create a new SwiftUI App named `MoodleLMS` inside `ios/`, then drop in the existing `MoodleLMS/` source folders (App, Core, Features, Resources).
2. Set the deployment target to iOS 17.
3. Run on an iPhone 15 simulator. Same login options as Android.

### API credentials (from the brief)

| Field | Value |
|---|---|
| Base URL | `https://moodle.itcorner.qzz.io` |
| Endpoint | `/webservice/rest/server.php` |
| Dev `wstoken` | `c269d73b8ec3265227714bf37f4dd2e4` |
| Dev `userid` | `1003` |

## Architecture overview

```
View  ──intent──▶  ViewModel  ──▶  Repository  ──▶  RemoteDataSource (HTTP)
  ▲                    │                                  │
  └──UiState<T>────────┘                                  ▼
                                                      Moodle API
```

- **MVVM**, mirrored on both platforms. The brief said "MVVM or equivalent" and we took it literally — no UseCase layer for what is a small, read-only app.
- **`UiState<T>` = Idle | Loading | Success(T) | Error(message)** — one type, identical shape on both platforms, drives every screen.
- **DTO / Domain split.** Network DTOs (`*Dto` / `*DTO`) live in the data layer and are mapped to plain domain models (`Course`, `Section`, `Module`, `GradeItem`) before crossing into ViewModels. UI never imports DTOs.
- **Token storage:** Keychain on iOS, `EncryptedSharedPreferences` on Android. The token is injected into the network layer, never hard-coded in repositories.
- **Image loading:** Moodle file URLs are token-protected; both platforms append `?token=<wstoken>` before handing the URL to the image loader (Coil / `AsyncImage`).

### Stacks

| | Android | iOS |
|---|---|---|
| UI | Jetpack Compose + Material 3 | SwiftUI |
| Async | Coroutines + `StateFlow` | `async/await` + `@MainActor` + `@Observable` |
| Networking | Retrofit + OkHttp + kotlinx.serialization | `URLSession` + `JSONDecoder` |
| DI | Hilt | Lightweight `AppContainer` via `@Environment` |
| Images | Coil (+ Coil-SVG decoder) | `AsyncImage` (+ gradient fallback for SVG) |
| Secure storage | EncryptedSharedPreferences | Keychain |
| Navigation | Navigation-Compose | `NavigationStack` + `TabView` |

## Screens

1. **Courses** — search/filter field at the top, scrollable cards with image (or generated colored placeholder when Moodle returns an SVG), category chip from the course shortname, progress bar + percentage when present.
2. **Course Detail** — pushed from a course card. Sections rendered as headers with an "n items" pill, modules listed underneath with their Moodle icon and module type. Tapping a module opens its URL in the system browser.
3. **Grades** — course picker at the top (the API is course-scoped); list of grade items with the percentage / grade rendered as a pill on the right, "Not graded yet" when Moodle returns dash placeholders.

All three screens share the same loading / error / empty states and a Retry action driven by the ViewModel's `load()`.

## Implementation notes (Moodle quirks worth knowing)

- **HTTP 200 errors.** Moodle returns success codes for application errors and signals failure via a JSON envelope `{ "exception", "errorcode", "message" }`. Both clients first attempt to decode this envelope; if it matches, they throw a typed `MoodleApiException` / `MoodleError.api`. This is the single most common foot-gun for Moodle integrations.
- **Course-scoped grades.** `gradereport_user_get_grade_items` requires a `courseid`, so the Grades screen exposes a course picker rather than a flat list.
- **Token-protected images.** `core_enrol_get_users_courses` and module icons return URLs that 401 without a token — both apps append `?token=…` before loading.
- **SVG course images.** Moodle auto-generates `course.svg` images for many courses. Android handles SVG via Coil's `SvgDecoder`; iOS's `AsyncImage` doesn't decode SVG, so we detect the extension and fall back to a brand-coloured gradient with the course initials.
- **HTML entities.** Course and section names sometimes come back encoded (`UX &amp; Interface Design`). We unescape on the way in (`HtmlCompat.fromHtml` / a small `String.htmlUnescaped` extension) so the UI never shows raw entities.
- **Dash grade placeholders.** Moodle uses `-` / `- %` for ungraded items; we strip those to nil and render "Not graded yet" instead of garbage.

## Trade-offs (deliberate, given the 24-hour window)

Cut from scope:
- Offline cache / local DB (Room / SwiftData).
- Pagination — the demo user has a small course list.
- Push notifications.
- Automated test coverage. The architecture is test-friendly (repositories are interfaces of one method, ViewModels expose a single `StateFlow` / `@Observable` state) but I did not ship a test suite.
- Localization beyond English.
- Module-level rendering inside the app (forum threads, page contents) — tapping a module opens its URL in the system browser instead.

What I'd add next:
1. Offline cache so course lists survive cold launches.
2. Unit tests for repositories (decoder round-trip + error envelope path) and ViewModel state machines.
3. Snapshot / UI tests for the three core screens.
4. Dark-mode visual pass (system colors give us 80% for free; the rest is brand polish).
5. Pull-to-refresh on Courses + Grades (the ViewModel `load()` plumbing is already in place).
