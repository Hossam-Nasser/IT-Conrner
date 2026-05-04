# iOS — Xcode setup (one-time)

The Swift sources are ready. Xcode generates `.xcodeproj` files on first open, so on a Mac:

1. Open Xcode → **File → New → Project → App**
   - Product name: `MoodleLMS`
   - Interface: **SwiftUI**
   - Language: **Swift**
   - Save into the `ios/` folder (overwrite the placeholder folder it creates).
2. In Finder, replace Xcode's generated `MoodleLMS/` folder contents with the `MoodleLMS/` folder shipped in this repo (or drag in the folders `App/`, `Core/`, `Features/`, `Resources/` and the `MoodleLMSApp.swift` file, choosing **"Create folder references"**).
3. Set the deployment target to **iOS 17** (Project → MoodleLMS target → Minimum Deployments).
4. Build & run on an iPhone 15 simulator.

No third-party packages are required — networking, image loading, and persistence are all done with stdlib (`URLSession`, `AsyncImage`, Keychain).

## Auth

On the Login screen you can:
- Sign in with `student1` / `Demo@12345` (the brief's username/password), or
- Tap **"Use dev token"** to skip login with the pre-generated `wstoken`.
