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
                    onDrag = { dx, dy ->
                        params.x = params.x + dx
                        params.y = params.y + dy
                        try {
                            windowManager.updateViewLayout(this, params)
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