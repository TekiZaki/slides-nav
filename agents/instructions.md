### Instructions (Always edit this file when making changes to make the instructions accurate)

This project is an Android application written in Kotlin designed to run in the background. It allows users to swipe or slide along the left or right edges of the screen to adjust the system volume and perform other customizable actions.

Anti Emoji, no emoji allowed

Anti Generic UI styles, use simple and modern UI, not generic AI/Colorful UI styles

### List of Changes (Always add new changes here, do not delete any previous changes)

- Initial Project Architecture: Created build system files (`build.gradle.kts`, `settings.gradle.kts`).
- Configured GitHub Actions Workflow: Set up a workflow for manual trigger compiles.
- Created background Overlay & Gesture logic using Android's AccessibilityService framework and WindowManager overlays.
- Created Main Settings Activity with modern, clean, dark-themed Jetpack Compose UI without overdesigned, colorful gradients.
- Fixed GitHub Actions path error by removing incorrect ./slides-nav working directory configurations and updating the APK artifact upload path.
- Fixed Gradlew missing error in GitHub Actions by setting up Gradle 8.5 via gradle/actions/setup-gradle and running the build with the system-installed gradle instead of a wrapper script.
- - Resolved Kotlin DSL compilation errors in app/build.gradle.kts by refactoring hyphenated Version Catalog alias names (e.g. replacing '-' with '.' and ensuring correct accessor syntax).
- Created gradle.properties to enable android.useAndroidX=true to resolve the checkDebugAarMetadata task build failure.