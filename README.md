# GRead Android Port

Android port of the GRead iOS app. Mirrors iOS functionality using Jetpack Compose and MVVM architecture.

## Build
```bash
./gradlew assembleDebug
```

## Key Components
- **UI**: Jetpack Compose (matches SwiftUI)
- **Architecture**: MVVM with LiveData/StateFlow
- **API**: Retrofit with Gson
- **Auth**: JWT token management
- **Data**: DataStore for preferences

## Structure
- `ui/screens/`: UI screens (LoginScreen, LibraryScreen, etc.)
- `ui/theme/`: Theme configuration
- `data/models/`: Data classes (User, Book, Activity, etc.)
- `data/api/`: Retrofit API service
- `data/managers/`: Business logic (AuthManager, etc.)
- `presentation/viewmodels/`: MVVM viewmodels

## API Base
`https://gread.fun/wp-json/`
