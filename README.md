# WebSiteNavigator Compose

This project is an Android application built using Jetpack Compose. The app displays different web pages and allows users to swipe back and forth between pages to display the next/previous page. It also shows a progress bar before each page is loaded and hides it once the page is loaded. If a page has already been visited, it shows a screenshot instead of the progress bar.

## Features

1. Display different web pages.
2. Swipe back and forth between pages.
3. Show a progress bar before each page is loaded.
4. Hide the progress bar once the page is loaded.
5. Show a screenshot if the page has already been visited.

## Setup

### Prerequisites

- Android Studio
- Kotlin 1.5+
- Android SDK 24+

### Dependencies

The project uses the following dependencies:

- Jetpack Compose
- Retrofit
- Gson
- Kotlinx Serialization

### Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/yourusername/WebSiteNavigator_Compose.git
    ```
2. Open the project in Android Studio.
3. Sync the project with Gradle files.
4. Build and run the project on an Android device or emulator.

## Usage

1. Launch the app.
2. Swipe left or right to navigate between different web pages.
3. The URL address bar at the top displays the currently loading URL.
4. If a page has already been visited, a screenshot of the page is displayed instead of the progress bar.

## API

The app fetches the list of web pages from the following API endpoint:
```
GET https://private-58ab56-mocks3.apiary-mock.com/pages
```
