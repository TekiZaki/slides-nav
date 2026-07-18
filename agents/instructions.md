### Instructions (Always edit this file when making changes to make the instructions accurate)

This project is an Android application written in Kotlin designed to run in the background. It displays a minimalist, dark-themed floating controller dock matching a wireframe layout (a vertical handle alongside three vertically stacked circles: Volume Up, Menu Toggle, and Volume Down) that allows users to adjust device volume and open an expandable utility menu to perform system actions like taking screenshots, toggling the flashlight, or locking the screen.

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
- Created default application launcher icons (ic_launcher and ic_launcher_round) along with a minimalist vector foreground drawable to resolve the AAPT missing resource build failure.
- Migrated gesture overlays to a modern floating control dock with quick volume circles and an expandable utility menu containing clean, hand-crafted vector drawings for screenshot, flashlight, and lock screen actions.
- Implemented smooth, jitter-free dragging for the floating control dock using raw absolute screen coordinates from the underlying MotionEvents to prevent visual stuttering.
- Fixed a Kotlin compilation error by importing the public extension property motionEvent on PointerEvent in FloatingControl.kt.