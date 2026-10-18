# KastaPil 🎯

An Android app that keeps score while you play darts. Throw your darts in real life, tap where they landed on the on-screen dartboard, and KastaPil handles the math.

> **Status:** University project (TDP028, Linköping University, 2023). It's functional but unfinished. Some screens (for example *About*) and features are only partially implemented.

## Features

- **Game setup:** 301 or 501, best of 1/3/5, one or two players
- **Touch-based scoring:** tap the dartboard where the dart hit. Custom hit detection maps the touch position to the right segment and multiplier
- **Statistics:** where on the board you hit most, darts thrown, most-used checkout doubles, and more (signed-in users only)
- **Google sign-in** or play as a guest
- **Cloud storage:** match data is saved to Firebase Firestore per user
- **Localization:** English and Swedish
- **Theming:** light and dark mode

## Tech stack

- Kotlin + Jetpack Compose (Material 3)
- MVVM architecture with ViewModels
- Navigation Compose
- Firebase Authentication (Google) and Cloud Firestore
- Coil for image loading

## Project structure

```
app/src/main/java/com/example/kastapil/
├── models/       # Data classes, Firestore access, auth client, app state
├── navigation/   # NavGraph and screen routes
├── ui/
│   ├── components/  # Reusable composables (app bar, dialogs, dropdowns)
│   ├── screens/     # Sign-in, home, create game, in-game, statistics, settings
│   └── theme/       # Colors, typography, theme
└── viewModels/   # One ViewModel per screen
```
