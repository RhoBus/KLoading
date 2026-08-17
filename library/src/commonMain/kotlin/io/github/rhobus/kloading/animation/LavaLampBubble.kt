package io.github.rhobus.kloading.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin

/**
 * [LavaLampBubble] displays a handful of soft blobs rising from the bottom to the top of the
 * container at independently varying speeds and sizes, wobbling slightly side to side, then
 * fading back in at the bottom — the slow, uneven float of wax in a lava lamp rather than a
 * uniform group animation.
 *
 * @param modifier The modifier to be applied to the container of the animation.
 * @param containerSize The size (width and height) of the composable. Defaults to 48.dp by 72.dp is typical; pass a Dp for a square container.
 * @param bubbleCount The number of rising bubbles. Defaults to 4.
 * @param color The color of the bubbles. Defaults to `Color.White`.
 * @param minBubbleRadius The smallest bubble radius. Defaults to 3.dp.
 * @param maxBubbleRadius The largest bubble radius. Defaults to 7.dp.
 * @param baseDurationMillis The rise duration in milliseconds for the slowest bubble; others are faster fractions of this. Defaults to 3200 ms.
 */
@Composable
fun LavaLampBubble(
    modifier: Modifier = Modifier,
    containerSize: Dp = 64.dp,
    bubbleCount: Int = 4,
    color: Color = Color.White,
    minBubbleRadius: Dp = 3.dp,
    maxBubbleRadius: Dp = 7.dp,
    baseDurationMillis: Int = 3200,
) {
    val infiniteTransition = rememberInfiniteTransition()

    val risers = (0 until bubbleCount).map { index ->
        val speedFactor = 0.55f + (index.toFloat() / bubbleCount) * 0.6f
        val duration = (baseDurationMillis * speedFactor).toInt()

        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(duration, delayMillis = index * 260, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    Canvas(modifier = modifier.size(containerSize)) {
        val w = this.size.width
        val h = this.size.height

        risers.forEachIndexed { index, riseAnim ->
            val progress = riseAnim.value
            val xBase = w * (0.2f + 0.6f * (index.toFloat() / (bubbleCount - 1).coerceAtLeast(1)))
            val wobble = sin(progress * 2 * PI.toFloat() * 2f + index) * (w * 0.06f)

            val y = h - progress * h
            val radiusPx = (minBubbleRadius.toPx() + (maxBubbleRadius.toPx() - minBubbleRadius.toPx()) * (index % 3) / 2f)

            // fade in near the bottom, fade out near the top
            val alpha = when {
                progress < 0.12f -> progress / 0.12f
                progress > 0.85f -> (1f - progress) / 0.15f
                else -> 1f
            }.coerceIn(0f, 1f)

            drawCircle(
                color = color.copy(alpha = alpha * 0.9f),
                radius = radiusPx,
                center = Offset(xBase + wobble, y)
            )
        }
    }
}
