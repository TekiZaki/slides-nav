### Instructions (Always edit this file when making changes to make the instructions accurate)

This project is an Android application written in Kotlin designed to run in the background. It allows users to swipe or slide along the left or right edges of the screen to adjust the system volume and perform other customizable actions.

Anti Emoji, no emoji allowed

Anti Generic UI styles, use simple and modern UI, not generic AI/Colorful UI styles

### List of Changes (Always add new changes here, do not delete any previous changes)

- Initial Project Architecture: Created build system files (`build.gradle.kts`, `settings.gradle.kts`).
- Configured GitHub Actions Workflow: Set up a workflow for manual trigger compiles.
- Created background Overlay & Gesture logic using Android's AccessibilityService framework and WindowManager overlays.
- Created Main Settings Activity with modern, clean, dark-themed Jetpack Compose UI without overdesigned, colorful gradients.