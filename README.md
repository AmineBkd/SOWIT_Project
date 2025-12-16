## 🔑 Setup Instructions

### **IMPORTANT: Configure Google Maps API Key**

⚠️ **This step is mandatory for the application to work properly.**

#### Using `local.properties` (Recommended)

1. Obtain a Google Maps API key:
   - Go to [Google Cloud Console](https://console.cloud.google.com/)
   - Create a new project or select an existing one
   - Enable **"Maps SDK for Android"** API
   - Create credentials (API Key)

2. Create or edit the `local.properties` file in the project root:

```properties
# local.properties
sdk.dir=/path/to/your/android/sdk

# Add your Google Maps API Key here
MAPS_API_KEY=YOUR_GOOGLE_MAPS_API_KEY_HERE
```

3. The API key will be automatically injected into the app during build

## 🛠️ Tech Stack

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Architecture:** MVVM + Clean Architecture
- **Dependency Injection:** Hilt/Dagger
- **Database:** Room
- **Maps:** Google Maps SDK for Android
- **Async:** Kotlin Coroutines
