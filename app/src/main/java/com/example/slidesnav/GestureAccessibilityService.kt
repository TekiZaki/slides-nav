package com.example.slidesnav

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.media.AudioManager
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent

class GestureAccessibilityService : AccessibilityService() {

    private lateinit var windowManager: WindowManager
    private var leftOverlay: View? = null
    private var rightOverlay: View? = null
    private lateinit var audioManager: AudioManager

    override fun onServiceConnected() {
        super.onServiceConnected()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        
        setupEdgeOverlays()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupEdgeOverlays() {
        val overlayWidth = 40 // Touch boundary width in pixels

        // Base layout flags to allow passing touches to underlying elements while capturing slides
        val layoutParamsFlags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL

        // Left Side Overlay configuration
        val leftParams = WindowManager.LayoutParams(
            overlayWidth,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            layoutParamsFlags,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.START or Gravity.TOP
        }

        leftOverlay = View(this).apply {
            setOnTouchListener(GestureTouchListener(true))
        }
        windowManager.addView(leftOverlay, leftParams)

        // Right Side Overlay configuration
        val rightParams = WindowManager.LayoutParams(
            overlayWidth,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            layoutParamsFlags,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.END or Gravity.TOP
        }

        rightOverlay = View(this).apply {
            setOnTouchListener(GestureTouchListener(false))
        }
        windowManager.addView(rightOverlay, rightParams)
    }

    private inner class GestureTouchListener(private val isLeft: Boolean) : View.OnTouchListener {
        private var startY = 0f
        private val gestureThreshold = 50f // Minimum movement to count as slide

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startY = event.rawY
                    return true
                }
                MotionEvent.ACTION_MOVE -> {
                    val currentY = event.rawY
                    val diffY = startY - currentY
                    if (kotlin.math.abs(diffY) > gestureThreshold) {
                        if (diffY > 0) {
                            adjustVolume(increase = true)
                        } else {
                            adjustVolume(increase = false)
                        }
                        // Reset starting frame to allow gradual dragging response
                        startY = currentY
                    }
                    return true
                }
            }
            return false
        }
    }

    private fun adjustVolume(increase: Boolean) {
        val streamType = AudioManager.STREAM_MUSIC
        val currentVolume = audioManager.getStreamVolume(streamType)
        val maxVolume = audioManager.getStreamMaxVolume(streamType)
        val step = if (increase) 1 else -1
        val targetVolume = (currentVolume + step).coerceIn(0, maxVolume)
        
        audioManager.setStreamVolume(streamType, targetVolume, AudioManager.FLAG_SHOW_UI)
    }

    override fun onDestroy() {
        super.onDestroy()
        leftOverlay?.let { windowManager.removeView(it) }
        rightOverlay?.let { windowManager.removeView(it) }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() {}
}
=======