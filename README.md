# Best Photo for Android

This project will build an Android app that shows editor's pick best photos fom Unsplash.
There are two tabs, one for browsing best photos and the other for searching photo library with keywords.

## Requirements

- Android minimum API 26+
- Android Studio ArcticFox 3.1+
- Kotlin 1.4+
- Use AndroidX artifacts when creating your project

## Installation

1. Open the project in Android Studio.
2. run with an emulator or connected physical device.

## Notes

- Unsplash API Access Key is embedded in the application class and rated to 50 calls / hour
- like API requires write_like scope which the account doesn't have,
  so the function doesn't work on the detail view when tapping on the thumbs up icon.
