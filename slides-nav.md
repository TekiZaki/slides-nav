# Code Dump: slides-nav

Instruction for AI: When making edits, please provide your changes as:
NOTE: there should not any text/character/symbol/anything between the updated code. The delimiter must have exactly 7 characters
SEARCH/REPLACE block:
## relative/path/to/file.ext
<<<<<<< SEARCH
old code
=======
new code
>>>>>>> REPLACE

To replace all code in a file (CRITICAL: MUST use this for modularization, total rewrites, or when modifying major portions of files like style.css, HTML, or large code files to avoid tedious and error-prone search/replace blocks):
## relative/path/to/file.ext
<<<<<<< REPLACE_ALL
new file content
>>>>>>> REPLACE_ALL

To create a new file, specify the new path under the ## header and leave the SEARCH block empty:
## relative/path/to/new_file.ext
<<<<<<< SEARCH
=======
new file content
>>>>>>> REPLACE

To move or rename a file, use the '->' operator in the ## header:
## relative/path/to/old.ext -> relative/path/to/new.ext
<<<<<<< SEARCH
=======
>>>>>>> REPLACE

To delete a file or folder (Delete workaround):
- To delete a file, move the file that needs to be deleted to the 'recycle-bin' folder:
## relative/path/to/file.ext -> recycle-bin/file.ext
<<<<<<< SEARCH
=======
>>>>>>> REPLACE

- To delete a folder, rename the folder to be deleted with a name like 'recycle-bin-folder-name':
## relative/path/to/folder-name -> relative/path/to/recycle-bin-folder-name
<<<<<<< SEARCH
=======
>>>>>>> REPLACE

## slides-nav/agents/instructions.md

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

## slides-nav/build.gradle.kts (5 lines)

```text
plugins {
    alias(libs.plugins.android.application) version "8.3.1" apply false
    alias(libs.plugins.kotlin.android) version "1.9.22" apply false
}

```

## slides-nav/gradle.properties (2 lines)

```text
# Enable AndroidX support
android.useAndroidX=true
```

## slides-nav/settings.gradle.kts (18 lines)

```text
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SlidesNav"
include(":app")

```

## slides-nav/.github/workflows/build.yml (34 lines)

```yaml
name: Android Build Workflow

on:
  workflow_dispatch: # Allows running this workflow manually from the GitHub Actions tab

jobs:
  build:
    name: Build APK & Bundle
    runs-on: ubuntu-latest

    steps:
      - name: Checkout Repository
        uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          distribution: "temurin"
          java-version: "17"

      - name: Setup Gradle
        uses: gradle/actions/setup-gradle@v4
        with:
          gradle-version: '8.5'

      - name: Run AssembleDebug Build
        run: gradle assembleDebug --no-daemon

      - name: Upload Debug APK Artifact
        uses: actions/upload-artifact@v4
        with:
          name: debug-apk
          path: app/build/outputs/apk/debug/app-debug.apk

```

## slides-nav/.gradle/9.3.0/gc.properties (1 lines)

```text

```

## slides-nav/.gradle/9.3.0/checksums/md5-checksums.bin

*Binary file skipped (contains null bytes)*

## slides-nav/.gradle/9.3.0/checksums/sha1-checksums.bin

*Binary file skipped (contains null bytes)*

## slides-nav/.gradle/9.3.0/fileChanges/last-build.bin

*Binary file skipped (contains null bytes)*

## slides-nav/.gradle/buildOutputCleanup/cache.properties (3 lines)

```text
#Sat Jul 18 19:05:21 WIB 2026
gradle.version=9.3.0

```

## slides-nav/.gradle/vcs-1/gc.properties (1 lines)

```text

```

## slides-nav/app/build.gradle.kts (52 lines)

```text
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.slidesnav"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.slidesnav"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
}

```

## slides-nav/app/src/main/AndroidManifest.xml (39 lines)

```xml
<?xml version="1.0" encoding="utf-8" ?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="Slides Navigation"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@android:style/Theme.DeviceDefault.NoActionBar"
    >

        <activity android:name=".MainActivity" android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <service
            android:name=".GestureAccessibilityService"
            android:permission="android.permission.BIND_ACCESSIBILITY_SERVICE"
            android:exported="true"
        >
            <intent-filter>
                <action
                    android:name="android.accessibilityservice.AccessibilityService"
                />
            </intent-filter>
            <meta-data
                android:name="android.accessibilityservice"
                android:resource="@xml/accessibility_service_config"
            />
        </service>
    </application>
</manifest>

```

## slides-nav/app/src/main/java/com/example/slidesnav/FloatingControl.kt (293 lines)

```kotlin
package com.example.slidesnav

import android.view.MotionEvent
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.motionEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class FloatingAction {
    SCREENSHOT,
    FLASHLIGHT,
    SCREEN_OFF
}

@Composable
fun FloatingControl(
    currentX: () -> Int,
    currentY: () -> Int,
    onDrag: (newX: Int, newY: Int) -> Unit,
    onVolumeUp: () -> Unit,
    onVolumeDown: () -> Unit,
    onAction: (FloatingAction) -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ) {
        // Expandable Menu tray (appears on the left side of the controller when expanded)
        AnimatedVisibility(
            visible = isMenuExpanded,
            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
            exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
        ) {
            Row(
                modifier = Modifier
                    .background(Color(0xFF1E1E1E), shape = RoundedCornerShape(12.dp))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Feature 1: Screenshot
                MenuIconButton(
                    label = "Snap",
                    onClick = {
                        isMenuExpanded = false
                        onAction(FloatingAction.SCREENSHOT)
                    }
                ) {
                    ScreenshotIcon(Color.White)
                }

                // Feature 2: Flashlight
                MenuIconButton(
                    label = "Torch",
                    onClick = {
                        isMenuExpanded = false
                        onAction(FloatingAction.FLASHLIGHT)
                    }
                ) {
                    FlashlightIcon(Color.White)
                }

                // Feature 3: Screen Off
                MenuIconButton(
                    label = "Lock",
                    onClick = {
                        isMenuExpanded = false
                        onAction(FloatingAction.SCREEN_OFF)
                    }
                ) {
                    ScreenOffIcon(Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Main Controller Dock matching wireframe layout
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Color(0xBB000000), shape = RoundedCornerShape(16.dp))
                .padding(8.dp)
        ) {
            // Drag Handle - minimalist rectangular vertical bar
            var initialX by remember { mutableStateOf(0) }
            var initialY by remember { mutableStateOf(0) }
            var initialTouchX by remember { mutableStateOf(0f) }
            var initialTouchY by remember { mutableStateOf(0f) }

            Box(
                modifier = Modifier
                    .width(12.dp)
                    .height(60.dp)
                    .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(6.dp))
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                val nativeEvent = event.motionEvent ?: continue
                                when (nativeEvent.action) {
                                    MotionEvent.ACTION_DOWN -> {
                                        initialX = currentX()
                                        initialY = currentY()
                                        initialTouchX = nativeEvent.rawX
                                        initialTouchY = nativeEvent.rawY
                                    }
                                    MotionEvent.ACTION_MOVE -> {
                                        val dx = (nativeEvent.rawX - initialTouchX).toInt()
                                        val dy = (nativeEvent.rawY - initialTouchY).toInt()
                                        onDrag(initialX + dx, initialY + dy)
                                    }
                                }
                            }
                        }
                    }
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Vertically stacked circles (Volume Up, Main Menu Toggle, Volume Down)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Volume Up button (Top small circle)
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color(0xFF333333), shape = CircleShape)
                        .clickable { onVolumeUp() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("+", color = Color.White, fontSize = 16.sp)
                }

                // Main Menu button (Middle larger circle)
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color(0xFFE0E0E0), shape = CircleShape)
                        .clickable { isMenuExpanded = !isMenuExpanded },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .drawBehind {
                                drawCircle(
                                    color = Color.Black,
                                    radius = size.width / 2,
                                    style = Stroke(width = 2.dp.toPx())
                                )
                                drawCircle(
                                    color = Color.Black,
                                    radius = 2.dp.toPx()
                                )
                            }
                    )
                }

                // Volume Down button (Bottom small circle)
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color(0xFF333333), shape = CircleShape)
                        .clickable { onVolumeDown() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("-", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun MenuIconButton(
    label: String,
    onClick: () -> Unit,
    iconContent: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFF2E2E2E), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            iconContent()
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = Color(0xFFCCCCCC),
            fontSize = 11.sp
        )
    }
}

@Composable
fun ScreenshotIcon(color: Color) {
    Box(
        modifier = Modifier
            .size(20.dp)
            .drawBehind {
                val strokeWidth = 2.dp.toPx()
                val sizePx = size.width

                drawLine(color, start = androidx.compose.ui.geometry.Offset(0f, 0f), end = androidx.compose.ui.geometry.Offset(6.dp.toPx(), 0f), strokeWidth = strokeWidth)
                drawLine(color, start = androidx.compose.ui.geometry.Offset(0f, 0f), end = androidx.compose.ui.geometry.Offset(0f, 6.dp.toPx()), strokeWidth = strokeWidth)

                drawLine(color, start = androidx.compose.ui.geometry.Offset(sizePx, sizePx), end = androidx.compose.ui.geometry.Offset(sizePx - 6.dp.toPx(), sizePx), strokeWidth = strokeWidth)
                drawLine(color, start = androidx.compose.ui.geometry.Offset(sizePx, sizePx), end = androidx.compose.ui.geometry.Offset(sizePx, sizePx - 6.dp.toPx()), strokeWidth = strokeWidth)

                drawCircle(color, radius = 3.dp.toPx())
            }
    )
}

@Composable
fun FlashlightIcon(color: Color) {
    Box(
        modifier = Modifier
            .size(20.dp)
            .drawBehind {
                val strokeWidth = 2.dp.toPx()

                drawRect(
                    color = color,
                    topLeft = androidx.compose.ui.geometry.Offset(size.width / 2 - 3.dp.toPx(), size.height / 2),
                    size = androidx.compose.ui.geometry.Size(6.dp.toPx(), 8.dp.toPx())
                )

                drawRect(
                    color = color,
                    topLeft = androidx.compose.ui.geometry.Offset(size.width / 2 - 6.dp.toPx(), size.height / 2 - 6.dp.toPx()),
                    size = androidx.compose.ui.geometry.Size(12.dp.toPx(), 6.dp.toPx())
                )

                drawLine(color, start = androidx.compose.ui.geometry.Offset(size.width / 2 - 8.dp.toPx(), 2.dp.toPx()), end = androidx.compose.ui.geometry.Offset(size.width / 2 - 12.dp.toPx(), 0f), strokeWidth = strokeWidth)
                drawLine(color, start = androidx.compose.ui.geometry.Offset(size.width / 2, 2.dp.toPx()), end = androidx.compose.ui.geometry.Offset(size.width / 2, 0f), strokeWidth = strokeWidth)
                drawLine(color, start = androidx.compose.ui.geometry.Offset(size.width / 2 + 8.dp.toPx(), 2.dp.toPx()), end = androidx.compose.ui.geometry.Offset(size.width / 2 + 12.dp.toPx(), 0f), strokeWidth = strokeWidth)
            }
    )
}

@Composable
fun ScreenOffIcon(color: Color) {
    Box(
        modifier = Modifier
            .size(20.dp)
            .drawBehind {
                val strokeWidth = 2.dp.toPx()

                drawArc(
                    color = color,
                    startAngle = -240f,
                    sweepAngle = 300f,
                    useCenter = false,
                    topLeft = androidx.compose.ui.geometry.Offset(2.dp.toPx(), 2.dp.toPx()),
                    size = androidx.compose.ui.geometry.Size(size.width - 4.dp.toPx(), size.height - 4.dp.toPx()),
                    style = Stroke(width = strokeWidth)
                )
                drawLine(
                    color = color,
                    start = androidx.compose.ui.geometry.Offset(size.width / 2, 0f),
                    end = androidx.compose.ui.geometry.Offset(size.width / 2, size.height / 2),
                    strokeWidth = strokeWidth
                )
            }
    )
}
```

## slides-nav/app/src/main/java/com/example/slidesnav/GestureAccessibilityService.kt (156 lines)

```kotlin
package com.example.slidesnav

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.graphics.PixelFormat
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.os.Build
import android.view.Gravity
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner

class GestureAccessibilityService : AccessibilityService(), LifecycleOwner, SavedStateRegistryOwner, ViewModelStoreOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)
    private val savedStateRegistryController = SavedStateRegistryController.create(this)
    private val store = ViewModelStore()

    override val lifecycle: Lifecycle get() = lifecycleRegistry
    override val savedStateRegistry: SavedStateRegistry get() = savedStateRegistryController.savedStateRegistry
    override val viewModelStore: ViewModelStore get() = store

    private lateinit var windowManager: WindowManager
    private var composeView: ComposeView? = null
    private lateinit var audioManager: AudioManager
    private lateinit var cameraManager: CameraManager
    private var isFlashlightOn = false

    override fun onCreate() {
        super.onCreate()
        savedStateRegistryController.performRestore(null)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        setupFloatingController()
    }

    private fun setupFloatingController() {
        val layoutParamsFlags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            layoutParamsFlags,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 100
            y = 500
        }

        composeView = ComposeView(this).apply {
            setViewTreeLifecycleOwner(this@GestureAccessibilityService)
            setViewTreeSavedStateRegistryOwner(this@GestureAccessibilityService)
            setViewTreeViewModelStoreOwner(this@GestureAccessibilityService)
            setContent {
                FloatingControl(
                    currentX = { params.x },
                    currentY = { params.y },
                    onDrag = { newX, newY ->
                        params.x = newX
                        params.y = newY
                        try {
                            windowManager.updateViewLayout(composeView, params)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    },
                    onVolumeUp = { adjustVolume(increase = true) },
                    onVolumeDown = { adjustVolume(increase = false) },
                    onAction = { action ->
                        when (action) {
                            FloatingAction.SCREENSHOT -> takeScreenshotAction()
                            FloatingAction.FLASHLIGHT -> toggleFlashlight()
                            FloatingAction.SCREEN_OFF -> lockScreenAction()
                        }
                    }
                )
            }
        }

        windowManager.addView(composeView, params)
    }

    private fun adjustVolume(increase: Boolean) {
        val streamType = AudioManager.STREAM_MUSIC
        val currentVolume = audioManager.getStreamVolume(streamType)
        val maxVolume = audioManager.getStreamMaxVolume(streamType)
        val step = if (increase) 1 else -1
        val targetVolume = (currentVolume + step).coerceIn(0, maxVolume)
        audioManager.setStreamVolume(streamType, targetVolume, AudioManager.FLAG_SHOW_UI)
    }

    private fun toggleFlashlight() {
        try {
            val cameraId = cameraManager.cameraIdList.getOrNull(0) ?: return
            isFlashlightOn = !isFlashlightOn
            cameraManager.setTorchMode(cameraId, isFlashlightOn)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun takeScreenshotAction() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            performGlobalAction(GLOBAL_ACTION_TAKE_SCREENSHOT)
        }
    }

    private fun lockScreenAction() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            performGlobalAction(GLOBAL_ACTION_LOCK_SCREEN)
        }
    }

    override fun onDestroy() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        composeView?.let {
            try {
                windowManager.removeView(it)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        super.onDestroy()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() {}
}
```

## slides-nav/app/src/main/java/com/example/slidesnav/MainActivity.kt (104 lines)

```kotlin
package com.example.slidesnav

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                MainScreen(
                    onOpenSettings = { openAccessibilitySettings() }
                )
            }
        }
    }

    private fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
    }
}

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFFE0E0E0),
            background = Color(0xFF121212),
            surface = Color(0xFF1E1E1E),
            onBackground = Color(0xFFFFFFFF),
            onSurface = Color(0xFFCCCCCC)
        ),
        content = content
    )
}

@Composable
fun MainScreen(onOpenSettings: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Slides Nav",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Control system actions globally using a minimalist floating control dock with quick volume adjustments and a utility features menu.",
                fontSize = 15.sp,
                fontFamily = FontFamily.SansSerif,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onOpenSettings,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(4.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Enable in Accessibility Settings",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

```

## slides-nav/app/src/main/res/drawable/ic_launcher_foreground.xml (15 lines)

```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="108dp"
    android:height="108dp"
    android:viewportWidth="108"
    android:viewportHeight="108">
    <path
        android:fillColor="#E0E0E0"
        android:pathData="M34,24 L44,24 L44,84 L34,84 Z" />
    <path
        android:fillColor="#E0E0E0"
        android:pathData="M64,24 L74,24 L74,84 L64,84 Z" />
    <path
        android:fillColor="#9E9E9E"
        android:pathData="M48,54 L60,54 L60,56 L48,56 Z" />
</vector>
```

## slides-nav/app/src/main/res/mipmap/ic_launcher.xml (5 lines)

```xml
<?xml version="1.0" encoding="utf-8"?>
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@android:color/black" />
    <foreground android:drawable="@drawable/ic_launcher_foreground" />
</adaptive-icon>
```

## slides-nav/app/src/main/res/mipmap/ic_launcher_round.xml (5 lines)

```xml
<?xml version="1.0" encoding="utf-8"?>
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@android:color/black" />
    <foreground android:drawable="@drawable/ic_launcher_foreground" />
</adaptive-icon>
```

## slides-nav/app/src/main/res/values/strings.xml (7 lines)

```xml
<resources>
    <string name="app_name">Slides Navigation</string>
    <string
        name="accessibility_service_description"
    >This service provides a minimalist floating control dock with quick volume buttons and an expandable utility features menu.</string>
</resources>

```

## slides-nav/app/src/main/res/xml/accessibility_service_config.xml (10 lines)

```xml
<?xml version="1.0" encoding="utf-8" ?>
<accessibility-service
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:description="@string/accessibility_service_description"
    android:accessibilityEventTypes="typeAllMask"
    android:accessibilityFeedbackType="feedbackGeneric"
    android:accessibilityFlags="flagDefault"
    android:canRetrieveWindowContent="false"
/>

```

## slides-nav/gradle/libs.versions.toml (23 lines)

```text
[versions]
agp = "8.3.1"
kotlin = "1.9.22"
coreKtx = "1.12.0"
lifecycleRuntimeKtx = "2.7.0"
activityCompose = "1.8.2"
composeBom = "2024.02.01"

[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
androidx-lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycleRuntimeKtx" }
androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activityCompose" }
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
androidx-compose-ui = { group = "androidx.compose.ui", name = "ui" }
androidx-compose-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
androidx-compose-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
androidx-compose-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
androidx-compose-material3 = { group = "androidx.compose.material3", name = "material3" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }

```

