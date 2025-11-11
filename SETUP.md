# GRead Android Setup

## Prerequisites
- Android Studio 2022.1+
- Android SDK 26+
- Kotlin 1.9+

## Quick Start
1. Open in Android Studio
2. Sync Gradle
3. Run on emulator/device

## Key Features
- Login/Register (JWT auth)
- Book library with progress tracking
- Activity feed
- Groups browsing
- User profile & stats
- Theme switching

## Architecture
- **UI**: Jetpack Compose
- **State**: MVVM with StateFlow
- **API**: Retrofit + Gson
- **Storage**: DataStore (preferences)

## API Endpoints
Base: `https://gread.fun/wp-json/`

### Auth
- `POST jwt-auth/v1/token` - Login
- `POST wp/v2/users/register` - Register

### Library
- `GET gread/v1/library` - Get books
- `POST gread/v1/library/add` - Add book
- `POST gread/v1/library/progress` - Update progress

### Activity
- `GET gread/v1/activity` - Feed
- `GET buddypress/v1/members/{id}` - User info
- `GET buddypress/v1/groups` - Groups

## File Structure
```
app/src/main/
├── java/com/gread/
│   ├── data/
│   │   ├── api/        (Retrofit service)
│   │   ├── managers/   (AuthManager)
│   │   └── models/     (Data classes)
│   ├── presentation/
│   │   └── viewmodels/ (MVVM viewmodels)
│   └── ui/
│       ├── screens/    (Compose screens)
│       └── theme/      (Material3 theming)
└── res/
    └── values/
```

## Next Steps
- Add more screens (Messages, Notifications, Settings)
- Add image caching (Coil)
- Add offline support (Room DB)
- Theme customization
