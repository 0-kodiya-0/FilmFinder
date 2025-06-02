# FilmFinder

FilmFinder is a cross-platform movie discovery application that allows users to search for and explore movies using the OMDb API (Open Movie Database). The project showcases modern mobile development practices with implementations in both native Android (Kotlin) and React Native (TypeScript).

## Features

- **Search for Movies**: Find detailed information about movies by title
- **Actor Search**: Search for movies featuring specific actors in local database
- **Extended Search**: Comprehensive movie search with filtering options and pagination
- **Local Database**: Save your favorite movies for offline viewing
- **Responsive Design**: Optimized for both portrait and landscape orientations
- **Dark Mode Support**: Seamless light and dark theme switching (Android)
- **Cross-Platform**: Available in both native Android and React Native implementations

## Platform Implementations

### Android (Native - Kotlin)
- **Location**: `apps/android/`
- **UI**: Jetpack Compose with Material 3 design
- **Architecture**: MVVM (Model-View-ViewModel)
- **Local Storage**: Room Database
- **Network**: HttpURLConnection with custom ApiClient
- **Image Caching**: Custom LRU cache implementation
- **Dependency Injection**: Manual dependency injection
- **Navigation**: Jetpack Navigation Compose

### React Native (TypeScript)
- **Location**: `apps/reactNative/`
- **UI**: React Native with native components
- **Architecture**: Custom hooks with state management
- **Local Storage**: Expo SQLite
- **Network**: Fetch API with custom service layer
- **Image Handling**: React Native Image with caching
- **Navigation**: React Navigation 6
- **Platform**: Expo managed workflow

## Technology Stack

### Shared Technologies
- **API**: OMDb API for movie data
- **Database**: SQLite for local storage
- **Network**: RESTful API communication
- **Image Loading**: Efficient caching mechanisms

### Android-Specific
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture Components**: ViewModel, LiveData, Room
- **Build System**: Gradle with Kotlin DSL
- **Minimum SDK**: API 24 (Android 7.0)

### React Native-Specific
- **Language**: TypeScript
- **Framework**: React Native 0.79+
- **State Management**: React Hooks (useState, useEffect, custom hooks)
- **Database**: Expo SQLite
- **Navigation**: React Navigation 7
- **Development**: Expo SDK 53

## Project Structure

```
FilmFinder/
├── apps/
│   ├── android/                 # Native Android application
│   │   ├── app/
│   │   │   ├── src/main/java/com/example/filmfinder/
│   │   │   │   ├── data/        # Data layer (repositories, API, database)
│   │   │   │   ├── ui/          # UI layer (screens, components, themes)
│   │   │   │   └── utils/       # Utility classes
│   │   │   └── build.gradle.kts
│   │   ├── gradle/
│   │   └── settings.gradle.kts
│   └── reactNative/             # React Native application
│       ├── src/
│       │   ├── components/      # Reusable UI components
│       │   ├── screens/         # Screen components
│       │   ├── data/           # Data models, API, database
│       │   ├── hooks/          # Custom React hooks
│       │   ├── navigation/     # Navigation configuration
│       │   └── utils/          # Utility functions
│       ├── App.tsx
│       ├── package.json
│       └── app.json
├── README.md
├── ROADMAP.md
└── LICENSE
```

## Setup and Installation

### Prerequisites
- **Android Studio** (for Android development)
- **Node.js** (v16 or higher for React Native)
- **Expo CLI** (for React Native development)
- **OMDb API Key** (free at https://www.omdbapi.com/)

### Android Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd FilmFinder
   ```

2. **Open Android project**
   ```bash
   cd apps/android
   # Open in Android Studio or build with Gradle
   ```

3. **Configure API key**
   - Create `local.properties` in the android app directory
   ```properties
   OMDB_API_KEY="your_api_key_here"
   OMDB_BASE_URL="https://www.omdbapi.com/"
   ```

4. **Build and run**
   ```bash
   ./gradlew assembleDebug
   # Or run directly from Android Studio
   ```

### React Native Setup

1. **Navigate to React Native directory**
   ```bash
   cd apps/reactNative
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Configure API key**
   - Create `.env` file in the root of the reactNative directory
   ```env
   # OMDb API Configuration
   OMDB_API_KEY=your_api_key_here
   OMDB_BASE_URL=https://www.omdbapi.com/
   ```

4. **Start the development server**
   ```bash
   npx expo start
   ```

5. **Run on device/simulator**
   ```bash
   # For Android
   npx expo run:android
   
   # For iOS (macOS only)
   npx expo run:ios
   ```

## Key Components

### Android
- **MovieRepository**: Central data access point for both local and remote data
- **AppDatabase**: Room database configuration for movie storage
- **ApiClient**: HttpURLConnection-based network client
- **ImageCacheManager**: Custom LRU cache for efficient image loading
- **ViewModels**: State management for each screen

### React Native
- **MovieRepository**: Unified data access layer
- **DatabaseService**: Expo SQLite service for local storage
- **ApiService**: Fetch-based API client
- **Custom Hooks**: State management (useMovieSearch, useActorSearch, etc.)
- **Navigation**: React Navigation setup

## Features Comparison

| Feature | Android (Kotlin) | React Native (TypeScript) |
|---------|------------------|---------------------------|
| Movie Search | ✅ | ✅ |
| Actor Search | ✅ | ✅ |
| Extended Search | ✅ | ✅ |
| Local Database | ✅ (Room) | ✅ (Expo SQLite) |
| Image Caching | ✅ (Custom LRU) | ✅ (Built-in) |
| Offline Support | ✅ | ✅ |
| Dark Mode | ✅ | 🚧 (Planned) |
| Landscape/Portrait | ✅ | ✅ |
| Network Detection | ✅ | ✅ |
| Error Handling | ✅ | ✅ |

## Development Features

- **Hot Reload**: Both platforms support hot reload for faster development
- **Type Safety**: Kotlin and TypeScript provide compile-time type checking
- **Modern UI**: Material 3 (Android) and native components (React Native)
- **State Management**: Reactive state management on both platforms
- **Navigation**: Type-safe navigation systems
- **Testing**: Unit test foundations included

## License

```
MIT License

Copyright (c) 2025 Sanithu Jayakody

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## Acknowledgments

- [OMDb API](https://www.omdbapi.com/) for providing the movie data
- [Android Jetpack](https://developer.android.com/jetpack) for modern Android development toolkit
- [React Native](https://reactnative.dev/) for cross-platform mobile development
- [Expo](https://expo.dev/) for streamlined React Native development
- Material Design for design principles

## Roadmap

See [ROADMAP.md](ROADMAP.md) for planned features and development timeline.

---

**Note**: This project demonstrates modern mobile app development practices across two different platforms, showcasing how the same core functionality can be implemented using platform-specific approaches while maintaining consistent user experience.