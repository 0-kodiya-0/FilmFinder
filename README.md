# FilmFinder

FilmFinder is a modern Android application that allows users to search for and discover movies using the OMDb API (Open Movie Database). Built with Jetpack Compose and following MVVM architecture principles, the app offers a clean and intuitive user interface for exploring the world of cinema.

## Features

- **Search for Movies**: Find detailed information about movies by title
- **Actor Search**: Search for movies featuring specific actors
- **Extended Search**: Comprehensive movie search with filtering options and pagination
- **Local Database**: Save your favorite movies for offline viewing
- **Responsive Design**: Optimized for both portrait and landscape orientations
- **Dark Mode Support**: Seamless light and dark theme switching

## Technology Stack

- **UI**: Jetpack Compose with Material 3 design
- **Architecture**: MVVM (Model-View-ViewModel)
- **Local Storage**: Room Database
- **Network**: HttpURLConnection with custom ApiClient

## Setup and Installation

1. Clone this repository
2. Open the project in Android Studio (Android Studio Iguana or newer recommended)
3. Replace the placeholder API key in `ApiClient.kt` with your own OMDb API key
4. Build and run the application on an emulator or physical device

```kotlin
// In app/src/main/java/com/example/filmfinder/data/remote/api/ApiClient.kt
// Replace this API key with your own
const val API_KEY = "YOUR_OMDB_API_KEY_HERE"
```

## Project Structure

- **data**: Contains the data layer including repositories, data models, and API clients
  - **local**: Room database configuration, DAOs, entities, and type converters
  - **remote**: Network API clients, service interfaces, and data models
  - **repository**: Repositories that bridge local and remote data sources
- **ui**: Contains all UI-related code
  - **components**: Reusable UI components
  - **screens**: Screen-specific composables and ViewModels
  - **theme**: Theme definitions, colors, typography, and shapes
- **utils**: Utility classes for image caching, network connectivity, etc.

## Key Components

- **MovieRepository**: Central data access point that handles both local and remote data operations
- **AppDatabase**: Room database configuration that stores movie data
- **ApiClient**: HttpURLConnection-based network client for the OMDb API
- **ImageCacheManager**: Custom LRU cache implementation for efficient image loading

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
- [Android Jetpack](https://developer.android.com/jetpack) for the modern Android development toolkit
- Material Design for the design principles
