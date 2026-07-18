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