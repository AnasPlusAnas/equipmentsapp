# HKBUEquipments App (Anas 24216437)

## Overview

The HKBUEquipments App is an Android application built using Kotlin, designed to manage and interact with an equipment inventory system. It utilizes the Ktor HTTP client for making network requests to a backend API.

## Table of Contents

- [Features](#features)
- [Setup](#setup)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Running the Application](#running-the-application)
- [Assumptions](#assumptions)
- [Limitations](#limitations)
- [Dependencies](#dependencies)
- [Configuration](#configuration)

## Features

- User authentication (login/register)
- Browse equipment listings
- View equipment details
- Reserve and unreserve equipment
- User-specific equipment list

## Setup

### Prerequisites

Before you begin, ensure you have met the following requirements:

-   Android Studio installed.
-   A compatible Android device or emulator.
-   Android SDK (API level 24 or higher).
-   Gradle version 8.5
-   JDK version 17
-   An active internet connection for API communication.
-   **Configure the SDK Path:**

    *   The `sdk.dir` property in the `local.properties` file should point to your Android SDK installation directory.  Ensure the path is correct.  Example:

        ```ini
        sdk.dir=C\:\\android-studio-sdk
        ```

### Installation

1.  **Clone the repository:**

    ```bash
    git clone https://github.com/UG-CS-HKBU/android-equipments-spring-2025-AnasPlusAnas.git
    ```

2.  **Open the project in Android Studio:**

    -   Launch Android Studio.
    -   Select `Open an Existing Project`.
    -   Navigate to the cloned repository and select the root `build.gradle` file.

3.  **Gradle Sync:**

    -   Once the project is open, Android Studio will prompt you to sync the Gradle files. Click `Sync Now`.
    -   Alternatively, you can manually trigger a sync by going to `File` \> `Sync Project with Gradle Files`.

## Running the Application

1.  **Connect an Android Device or Start an Emulator:**

    -   Ensure your Android device is connected to your computer via USB and USB debugging is enabled.
    -   Alternatively, you can use the Android Emulator provided by Android Studio. Create and start an emulator from the AVD Manager (`Tools` \> `AVD Manager`).

2.  **Build and Run the Application:**

    -   Click the `Run` button (green play icon) in the Android Studio toolbar.
    -   Select your connected device or emulator from the deployment target options.
    -   Android Studio will build the project and install the app on the selected device or emulator.

3.  **Using the Application:**

    -   Once installed, the app will launch automatically.
    -   Follow the on-screen instructions to use the app's features.

## Assumptions

-   The backend API (`https://equipments-api.azurewebsites.net/api`) is running and accessible.
-   User accounts can be created through the registration process.
-   The API endpoints for login, registration, equipment listing, and reservations are functioning as expected.
-   The application assumes a stable internet connection for all network operations.

## Limitations

-   The application currently lacks offline capabilities.
-   Error handling and UI feedback could be improved for certain edge cases.
-   The UI is not fully optimized for all screen sizes and resolutions.
-   The application does not include comprehensive unit tests.
-   The current implementation does not support advanced search or filtering options.

## Dependencies

-   Kotlin
-   Android SDK
-   Ktor HTTP Client
-   Kotlinx Serialization
-   AndroidX libraries (AppCompat, Material, etc.)

## Configuration
Test Login:
`bringsell1@ow.ly`
`123456`

The base URL for the API is defined in `KtorClient.kt`:

```kotlin
private const val BASE_URL = "https://equipments-api.azurewebsites.net/api"
