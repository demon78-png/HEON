# HEON — Premium Gaming Discovery & Library Hub for Android

HEON is a native Android gaming platform built with Jetpack Compose, Material Design 3, Kotlin Coroutines, and clean MVVM architecture. It delivers a futuristic, cinematic gaming discovery and library experience tailored for mobile performance.

---

## 🎨 Visual Identity & Design System

- **Primary Background**: `#08090D` (Deep Void)
- **Secondary Surface**: `#11131A` (Cyber Dark)
- **Elevated Cards**: `#191C26`
- **Primary Accent**: `#B9FF39` (Electric Neon Lime)
- **Secondary Accent**: `#8B5CF6` (Cyber Violet)
- **Tertiary Accent**: `#38BDF8` (Quantum Cyan)
- **Primary Text**: `#F7F8FC`
- **Secondary Text**: `#A1A7B6`
- **Success / Status**: `#22C55E` / `#F59E0B` / `#EF4444`

---

## 🚀 Key Functional Areas Implemented

### 1. Home & Game Discovery (Parts 01 & 02)
- **Header**: HEON geometric emblem, brand wordmark, notifications with unread badge, player avatar, and search trigger.
- **Hero Featured Carousel**: Cinematic AAA key visuals (`CyberBlade: Resurgence`, `Velocity: Neo-City Drift`, `Galactic Odyssey`), promotional taglines, rating pills, "View Game", and wishlist bookmarks.
- **Browse by Category**: Interactive filter chips (`Action`, `RPG`, `Racing`, `Strategy`, `Sci-Fi`, `Multiplayer`, `Indie`, etc.).
- **Curated Sections**: "Trending Now", "Editor's Picks", "Recently Added", and "Recommended for You".

### 2. Game Details & Launch Panel (Parts 03 & 13)
- Immersive hero visual with status bar insets and BackHandler.
- Rating breakdown, size footprint, version, release date, and hardware metrics (120 FPS, Gamepad ready).
- Interactive download queue integration: Download APK, pause, resume, cancel, verify checksum, and install.
- Installed states: "Launch Game", "Update Available", "Uninstall", and Cloud Streaming alternative.
- Technical system specifications (Minimum Android OS, RAM, Storage, Chipset support).
- Verified player community reviews and similar game recommendations.

### 3. Player Library & Download Manager (Parts 03 & 09)
- **My Games**: Saved and installed titles.
- **Wishlist**: Tracked games with release and sale alerts.
- **Installed**: Local on-device games with version tracking, launch shortcut, and uninstall cleanup.
- **Downloads Queue**: Real-time progress percentage, downloaded/total MB bytes, speed metrics, and pause/resume/install controls.
- **Storage Allocation Analyzer**: Bar showing storage utilization for HEON titles and available device storage.

### 4. Player Profiles & Authentication (Parts 04 & 10)
- Gamer Card: Player avatar, level, XP progress bar, membership tier ("HEON Prime VIP" vs "HEON Explorer").
- Account dialog: Sign In and Register with client validation, password toggle, and guest-to-account library sync path.
- Player statistics: Saved games, total downloads, and logged playtime hours.

### 5. In-App Notifications Center (Part 05)
- Category filters: All, Game Updates, System, and Community.
- Unread counter badges, "Mark all as read" action, swipe/dismiss action, and deep links into game details.

### 6. Premium Pass & Billing (Part 06)
- HEON Prime Monthly ($4.99/mo) and Annual ($49.99/yr, Save 17%) plans.
- Benefits: Priority cloud servers, zero ads, exclusive discounts, 4K artwork & OST packs, and VIP support.
- Google Play Billing flow simulation, active subscription restoration, and transaction history.

### 7. Protected Admin Management Console (Part 07)
- Secure PIN / passkey gate (`7788`).
- Real-time platform telemetry: Total games, published count, registered players, active downloads, and server health.
- Catalog curation: Instant toggle for Featured hero carousel and Editor's Picks.
- Announcement Dispatcher: Draft and broadcast platform notices directly to the player notification center.
- Security audit log history with timestamps and actor tracking.

### 8. Settings & Preferences (Part 11)
- Downloads & Network: Wi-Fi only downloads, auto-update games on Wi-Fi, auto-delete APK after install.
- Accessibility: High contrast text, reduced motion, and haptics.
- Storage Management: One-tap temporary image and catalog cache cleaner.
- About & Legal: Architecture details, version 1.0.4, and licensing.

### 9. Help Center & Support (Part 12)
- Frequently Asked Questions accordion (installation, cloud streaming, guest mode, refunds).
- Support ticket creation with category, subject, description, and status tracking.
- Bug Reporting tool with auto-attached device model and Android OS diagnostics.

### 10. Community & Social (Part 10)
- Global player leaderboard with rankings and division badges.
- Friends list with live Online / In-Game / Offline presence.
- Game achievements tracker with unlock status and XP rewards.

### 11. Cloud Gaming & Remote Sessions (Part 14)
- **Live Stream Viewport**: Real-time WebRTC stream simulation with telemetry overlay (latency in ms, FPS, bitrate, and packet loss).
- **Edge Server Regions**: Multi-region selection (EU Central Frankfurt, US East Virginia, AP East Tokyo).
- **Stream Presets**: Balanced (1080p @ 60fps), Pro Performance (1080p @ 120fps), and 4K Ultra Cinematic (HEON Prime VIP only).
- **Virtual Gamepad**: High-precision on-screen touch overlay featuring D-Pad, dynamic thumbstick, and tactile action buttons (A, B, X, Y).

### 12. Gamification, Season Horizon & Bounties (Part 16)
- **Season Horizon Pass**: Tier-based progression with seasonal XP bar, free vs Prime rewards.
- **Bounties & Challenges**: Daily quests and weekly missions with non-duplicative, anti-abuse claim validation.
- **Cosmetics Locker**: Equip and manage earned avatar frames, neon profile glows, and gamer titles.

### 13. Offline Mode, Sync & Platform Resilience (Part 17 & 18)
- Automatic detection and graceful fallback to local cached catalog when disconnected.
- Queued sync operations preserving user actions for automatic synchronization when connectivity is restored.
- Role-based staff access restrictions with strict passkey gating.

### 14. QA Verification & Android Packaging (Part 19 & 20)
- Full local JVM Robolectric test suite passing (`:app:testDebugUnitTest`).
- Verified Android APK build (`:app:assembleDebug`) generating signed package ready for emulator testing and distribution.

---

## 🛠️ Architecture & Package Structure

```
app/src/main/java/com/example/
├── MainActivity.kt                  # App root, navigation routing, toast alerts, auth dialog
├── data/
│   └── GameRepository.kt            # Clean repository interfaces and local catalog data source
├── model/
│   ├── Game.kt                      # Game, SystemRequirements, PlayerReview, DownloadOption
│   ├── Category.kt                  # GameCategory model & default taxonomy
│   ├── DownloadModels.kt            # DownloadState, DownloadItem, InstalledGameInfo
│   ├── TransactionModels.kt         # PremiumPlan, TransactionRecord
│   ├── SocialModels.kt              # Achievement, LeaderboardEntry, FriendPlayer
│   ├── SupportModels.kt             # SupportTicket, FaqItem, BugReport
│   ├── AdminModels.kt               # AuditLog, PlatformMetrics
│   └── UserProfile.kt               # UserProfile, NotificationItem
└── ui/
    ├── HeonViewModel.kt             # Central reactive state management
    ├── components/
    │   ├── HeonBrand.kt             # Vector emblem & brand wordmark
    │   ├── HeonButtons.kt           # Buttons, chips, rating pills, status badges, headers
    │   ├── HeonBottomNav.kt         # M3 bottom bar respecting navigationBars insets
    │   ├── GameCards.kt             # Hero, Landscape, Portrait, and Compact game cards
    │   ├── StateViews.kt            # Skeleton shimmer, empty states, error states
    │   └── AuthDialog.kt            # Sign-in & sign-up modal dialog
    ├── screens/
    │   ├── HomeScreen.kt            # Hero carousel, trending, categories, recommendations
    │   ├── DiscoverScreen.kt        # Search, filters, sorting, 2-column game grid
    │   ├── GameDetailScreen.kt      # Download/Install/Launch panel, specs, reviews, related
    │   ├── LibraryScreen.kt         # My Games, Wishlist, Installed, Downloads queue, Storage
    │   ├── NotificationsScreen.kt   # Alert center, categories, mark all read
    │   ├── ProfileScreen.kt         # Player card, XP, stats, navigation to secondary flows
    │   ├── PremiumScreen.kt         # Plans, perks, Google Play Billing, transactions
    │   ├── AdminScreen.kt           # Passkey gate, telemetry, catalog curation, announcements
    │   ├── SettingsScreen.kt        # Network, Accessibility, Cache cleaner, About
    │   ├── SupportScreen.kt         # FAQs, Ticket submission, Bug reporter
    │   └── SocialScreen.kt          # Leaderboards, Friends presence, Achievements
    └── theme/
        ├── Color.kt                 # Official HEON color tokens
        ├── Type.kt                  # M3 typography hierarchy
        └── Theme.kt                 # HeonTheme with dark gaming colorScheme
```
