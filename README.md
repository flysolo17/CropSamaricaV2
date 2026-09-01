# 🌾 Crop Samarica

> **A smart agriculture companion for rice farmers**

Crop Samarica is an Android application designed to help rice farmers manage their crops, access agricultural information, monitor weather conditions, receive reminders, and make better farming decisions through intelligent recommendations.

The application combines modern Android development with cloud services and AI-assisted features to provide farmers with a centralized digital farming companion.

---

## ✨ Features

### 🌱 Crop Management
- Manage rice crop information and growth stages
- Track crop-related activities
- Maintain farming records
- Receive recommendations based on crop conditions

### 🤖 AI-Powered Assistance
Crop Samarica integrates AI-assisted functionality to support agricultural workflows, including:

- Crop recommendation planning
- Automated reminders
- Survey generation for upcoming crop stages
- Agricultural announcements
- AI-assisted farming recommendations

### 🌦️ Weather Information
- View current weather information
- Retrieve weather data through an external weather API
- Use weather conditions as part of farming decisions

### 🐛 Pest Management
- Access pest-related information
- Monitor potential crop threats
- Provide farmers with relevant pest information

### 🧪 Fertilizer Management
- Access fertilizer-related information
- Support fertilizer planning and recommendations

### 📋 Surveys
- Complete crop-stage surveys
- Generate surveys for upcoming crop stages
- Collect information that can be used for farming recommendations

### 🔔 Notifications & Reminders
- Receive farming reminders
- Get announcements
- Receive notifications related to crop activities and recommendations

### 👤 User Management
- Firebase Authentication
- User profiles
- Persistent application preferences

---

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| **Kotlin** | Primary programming language |
| **Jetpack Compose** | Modern Android UI |
| **Material 3** | UI components and design system |
| **Android SDK 36** | Compile and target SDK |
| **Firebase Authentication** | User authentication |
| **Cloud Firestore** | Cloud database |
| **Firebase Storage** | File and media storage |
| **Firebase Cloud Messaging** | Push notifications |
| **Firebase AI** | AI-assisted functionality |
| **Hilt** | Dependency injection |
| **Kotlin Serialization** | JSON serialization |
| **Retrofit** | HTTP networking |
| **OkHttp** | HTTP client |
| **Coil** | Image loading |
| **DataStore** | Local preferences storage |
| **Navigation 3** | Application navigation |
| **KSP** | Kotlin symbol processing |

The current Gradle configuration targets Android SDK 36 with a minimum Android SDK of 24.

---

## 🏗️ Architecture

Crop Samarica follows a modular structure separating UI, business logic, data access, and supporting infrastructure.

```text
app/
└── src/
    └── main/
        ├── java/
        │   └── com/potatodevs/cropsamarica/
        │       ├── ai/
        │       │   ├── converters/
        │       │   ├── CreateAnnouncement.kt
        │       │   ├── CreateRecommendationPlan.kt
        │       │   ├── CreateSurveyForNextCropStage.kt
        │       │   ├── GenerateReminder.kt
        │       │   └── System.kt
        │       │
        │       ├── datastore/
        │       ├── di/
        │       ├── models/
        │       ├── repositories/
        │       ├── service/
        │       ├── test/
        │       │
        │       ├── ui/
        │       │   ├── auth/
        │       │   ├── common/
        │       │   ├── config/
        │       │   ├── developer/
        │       │   ├── errors/
        │       │   ├── guide/
        │       │   ├── index/
        │       │   ├── main/
        │       │   ├── notifications/
        │       │   ├── onboarding/
        │       │   ├── settings/
        │       │   ├── theme/
        │       │   └── utils/
        │       │
        │       ├── utils/
        │       ├── CropSamarica.kt
        │       └── MainActivity.kt
        │
        └── res/
```

The source structure currently separates AI functionality, models, repositories, services, dependency injection, DataStore, and UI modules.

---

## 📱 Requirements

Before running the project, make sure you have:

- Android Studio
- JDK 8 or compatible Android Studio JDK
- Android SDK 36
- Android SDK Platform 24 or higher
- A Firebase project
- A valid weather API secret

The application currently uses:

```text
minSdk    = 24
compileSdk = 36
targetSdk = 36
```



---

## 🚀 Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/flysolo17/CropSamaricaV2.git
cd CropSamaricaV2
```

### 2. Open the project

Open the project using **Android Studio**.

Allow Gradle to synchronize and download the required dependencies.

### 3. Configure Firebase

The application uses Firebase services including:

- Firebase Authentication
- Cloud Firestore
- Firebase Storage
- Firebase Cloud Messaging
- Firebase AI

Create/configure a Firebase project and connect the Android application using your own Firebase configuration.

> **Important:** Do not commit private Firebase credentials, API keys, or other secrets to source control.

### 4. Configure the Weather API

The application reads the weather API secret from `local.properties`.

Create or update:

```text
local.properties
```

with:

```properties
WEATHER_SECRET=your_weather_api_key
```

The Gradle configuration exposes this value to the application through:

```kotlin
buildConfigField(
    "String",
    "WEATHER_SECRET",
    "\"${properties.getProperty("WEATHER_SECRET")}\""
)
```



### 5. Build the application

Using the Gradle wrapper:

#### Windows

```bash
gradlew.bat assembleDebug
```

#### macOS / Linux

```bash
./gradlew assembleDebug
```

### 6. Run the application

Connect an Android device or start an Android Emulator, then run the application from Android Studio.

---

## 🔐 Configuration

Crop Samarica depends on external services. Before running the application, configure:

```text
Firebase
├── Authentication
├── Firestore
├── Storage
├── Cloud Messaging
└── Firebase AI

Weather API
└── WEATHER_SECRET
```

Keep environment-specific secrets outside version control.

---

## 🧩 Project Dependencies

The project uses a number of Android and Google libraries, including:

- AndroidX Core KTX
- AndroidX Lifecycle
- Activity Compose
- Jetpack Compose
- Compose Material 3
- Firebase Authentication
- Firebase Firestore
- Firebase Storage
- Firebase AI
- Firebase Messaging
- Hilt
- KSP
- DataStore Preferences
- Coil
- Retrofit
- Gson Converter
- OkHttp
- Kotlin Serialization
- Android Navigation 3

These dependencies are defined in the application's Gradle configuration.

---

## 🧪 Testing

The project contains both unit-test and Android instrumentation-test source sets:

```text
app/src/
├── androidTest/
└── test/
```

Run unit tests with:

```bash
./gradlew test
```

Run Android instrumentation tests with:

```bash
./gradlew connectedAndroidTest
```

On Windows:

```bash
gradlew.bat test
gradlew.bat connectedAndroidTest
```

---

## 🔒 Security

When contributing or deploying Crop Samarica:

- Never commit API keys.
- Never commit production secrets.
- Keep `local.properties` private.
- Use Firebase Security Rules to protect user data.
- Restrict Firebase Authentication providers to those required by the application.
- Apply least-privilege access to Firestore and Storage.
- Review API keys and Firebase configuration before publishing a production build.

---

## 📂 Main Modules

### `ai`

Contains AI-related functionality used by the application.

```text
ai/
├── converters/
├── CreateAnnouncement.kt
├── CreateRecommendationPlan.kt
├── CreateSurveyForNextCropStage.kt
├── GenerateReminder.kt
└── System.kt
```

### `models`

Contains application data models such as:

```text
announcement
fertilizer
pests
reminder
rice
survey
tasks
weather
Notification
User
```



### `repositories`

Responsible for application data access and repository abstractions.

### `service`

Contains application and external-service related functionality.

### `datastore`

Handles local persistent preferences.

### `ui`

Contains the Jetpack Compose presentation layer:

```text
ui/
├── auth
├── common
├── config
├── developer
├── errors
├── guide
├── index
├── main
├── notifications
├── onboarding
├── settings
├── theme
└── utils
```



---

## 🎯 Project Goals

Crop Samarica aims to make agricultural information and digital farming tools more accessible to rice farmers.

The project focuses on:

- Improving access to agricultural information
- Helping farmers monitor crop activities
- Providing timely reminders
- Supporting data-driven farming decisions
- Using AI to assist agricultural workflows
- Improving communication between agricultural stakeholders and farmers

---

## 🗺️ Roadmap

Potential future improvements include:

- [ ] Offline-first functionality
- [ ] Expanded crop support
- [ ] Improved pest detection
- [ ] More detailed crop analytics
- [ ] Historical weather analysis
- [ ] Improved AI recommendations
- [ ] Farm location and mapping
- [ ] Agricultural market information
- [ ] Farmer-to-expert communication
- [ ] More comprehensive notifications
- [ ] Production analytics and monitoring

---

## 🤝 Contributing

Contributions, suggestions, and bug reports are welcome.

### Development workflow

1. Fork the repository.
2. Create a feature branch.

```bash
git checkout -b feature/your-feature
```

3. Make your changes.
4. Run tests.

```bash
./gradlew test
```

5. Commit your changes.

```bash
git commit -m "feat: add your feature"
```

6. Push the branch.

```bash
git push origin feature/your-feature
```

7. Open a Pull Request.

---

## 📄 License

No license is currently specified in the repository.

If this project is intended to be open source, add a `LICENSE` file and select an appropriate license before publishing or accepting external contributions.

---

## 👨‍💻 Development

**Crop Samarica**  
Package: `com.potatodevs.cropsamarica`

Repository:  
https://github.com/flysolo17/CropSamaricaV2

---

<p align="center">
  Built with ❤️ using Kotlin and Jetpack Compose
</p>
