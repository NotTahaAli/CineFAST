# CineFAST

Android movie ticket booking app. Browse now-showing / coming-soon movies,
pick seats, add snacks, and confirm a booking. Backed by Firebase for auth
and data storage.

Coursework project (package `com.l230954.cinefast`) — no deployment or CI
story, just a buildable Android Studio app.

## Features

- Email/password signup and login (Firebase Auth)
- Home screen with Now Showing / Coming Soon tabs (ViewPager2 + TabLayout)
- Movie list pulled from `app/src/main/assets/movies.json`
- Seat selection, snacks selection, booking confirmation flow
- Booking history ("My Bookings") read/written via Firebase Realtime Database
  (`firebase-ui-database`)

## Tech stack

- Java, Android SDK (minSdk 24, targetSdk/compileSdk 36)
- Gradle Kotlin DSL (AGP 9.0.1)
- Firebase Authentication + Realtime Database
- View/Data Binding, ConstraintLayout, Material Components

## Setup

1. Open the project root in Android Studio.
2. Create a Firebase project, add an Android app with applicationId
   `com.l230954.cinefast`, and download `google-services.json` into `app/`
   (not committed — required for the `google-services` Gradle plugin to
   build). Enable Email/Password auth and Realtime Database in the Firebase
   console.
3. Sync Gradle and run on a device/emulator with API 24+.
