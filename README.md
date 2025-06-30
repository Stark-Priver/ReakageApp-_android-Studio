# Water Watch - Water Issues Reporting App

## Description

Water Watch is an Android mobile application designed to help users report water-related problems such as leaks, contamination, or quality issues. Users can create an account, submit reports with descriptions, locations, and optional photos, and view the status of their submitted reports. The application utilizes Firebase for backend services.

## Features

*   **User Authentication:**
    *   Email and Password based Sign Up and Login.
    *   Persistent user sessions.
    *   Secure Logout functionality.
*   **Report Submission:**
    *   Users can submit new reports detailing water-related issues.
    *   Fields for description and location.
    *   Option to upload a photo of the issue (with image picker and permission handling).
*   **View Submitted Reports:**
    *   Users can view a list of all reports they have personally submitted.
    *   Displays report details including description, location, status, submission date, and attached photo.
    *   Real-time updates for the report list.
*   **User Profile:**
    *   Displays the logged-in user's email.
    *   (Foundation for future profile updates).
*   **Splash Screen:**
    *   App loading screen that directs users to Login or Home based on authentication state.

## Tech Stack & Setup

*   **Frontend:**
    *   Kotlin
    *   Jetpack Compose for UI development.
    *   Accompanist Permissions for runtime permission handling.
    *   Coil for image loading.
*   **Backend (Firebase):**
    *   **Firebase Authentication:** For user management.
    *   **Firebase Realtime Database:** For storing report data.
    *   **Firebase Storage:** For storing user-uploaded report images.
*   **Architecture:**
    *   Follows a basic MVVM-like pattern for UI logic separation (ViewModels, Composables).
    *   Gradle with Kotlin DSL and Version Catalogs for dependency management.

### Firebase Setup Instructions

1.  **Create a Firebase Project:** Go to the [Firebase Console](https://console.firebase.google.com/) and create a new project (or use an existing one).
2.  **Register your Android App:**
    *   Add an Android app to your Firebase project.
    *   Use `com.example.reakageapp` as the package name.
    *   Download the `google-services.json` file.
3.  **Add `google-services.json`:** Place the downloaded `google-services.json` file into the `app/` directory of this project.
4.  **Enable Firebase Services:**
    *   **Authentication:** Enable "Email/Password" sign-in method in the Firebase Authentication section.
    *   **Realtime Database:** Create a Realtime Database instance. For initial setup, you can use test mode rules, but ensure you update them with the secure rules provided below for production.
    *   **Storage:** Enable Firebase Storage. Update storage rules as provided below.

### Recommended Security Rules:

**Realtime Database Rules (`project-root/database.rules.json` or set in console):**
```json
{
  "rules": {
    "reports": {
      ".indexOn": ["userId"],
      "$reportId": {
        ".write": "auth != null && newData.child('userId').val() === auth.uid",
        ".read": "auth != null && data.child('userId').val() === auth.uid",
        ".validate": "newData.hasChildren(['userId', 'reporterEmail', 'timestamp', 'location', 'description', 'status']) && newData.child('userId').isString() && newData.child('reporterEmail').isString() && newData.child('location').isString() && newData.child('description').isString() && newData.child('status').isString()"
      }
    }
  }
}
```

**Firebase Storage Rules (`project-root/storage.rules` or set in console):**
```
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /report_images/{userId}/{imageId} {
      allow read: if request.auth != null;
      allow write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
```

## How to Build and Run

1.  **Clone the repository:**
    ```bash
    git clone <repository-url>
    cd <repository-directory>
    ```
2.  **Firebase Setup:** Ensure you have completed the Firebase Setup steps above, especially adding the `google-services.json` file to the `app/` directory.
3.  **Open in Android Studio:** Open the project in the latest stable version of Android Studio.
4.  **Sync Gradle:** Allow Android Studio to sync the project and download dependencies.
5.  **Build and Run:** Select an emulator or connect a physical device and run the app from Android Studio.

## Project Structure

*   `app/src/main/java/com/example/reakageapp`: Root package.
    *   `MainActivity.kt`: Main entry point for the app.
    *   `NavigationGraph.kt`: Handles navigation logic using Jetpack Compose Navigation.
    *   `components/`: Shared UI components (e.g., BottomNavigationBar, GlassCard).
    *   `data/model/`: Data classes (e.g., `Report.kt`).
    *   `presentation/`: ViewModels for different screens/features (e.g., `AuthViewModel.kt`, `ReportViewModel.kt`).
    *   `screens/`: Composable functions representing different app screens (e.g., `LoginScreen.kt`, `HomeScreen.kt`, `SubmitReportScreen.kt`).
    *   `theme/`: UI theme, colors, and custom modifiers like `glassmorphism`.
*   `app/src/main/res/`: Android resources (drawables, mipmap for launcher icons).
*   `gradle/`: Gradle configuration, including `libs.versions.toml` for version catalog.

---

This README provides a comprehensive overview of the Water Watch application.
