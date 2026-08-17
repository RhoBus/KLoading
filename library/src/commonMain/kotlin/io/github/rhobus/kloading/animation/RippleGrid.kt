package io.github.rhobus.kloading.animation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.hypot
import kotlin.math.max

/**
 * [RippleGrid] displays a square grid of dots that pulse in scale and opacity, with a start
 * delay proportional to each dot's distance from the grid's center. The result reads as a
 * ripple expanding outward across the grid, similar to a drop landing in a pool.
 *
 * @param modifier The modifier to be applied to the container of the animation.
 * @param gridSize The number of dots per row/column. Defaults to 5 (5x5 grid).
 * @param dotSize The size (diameter) of each dot at rest. Defaults to 6.dp.
 * @param spacing The spacing between dots. Defaults to 6.dp.
 * @param color The color of the dots. Defaults to `Color.White`.
 * @param pulseDurationMillis The duration in milliseconds of a single dot's pulse. Defaults to 700 ms.
 * @param rippleSpeedMillis The delay in milliseconds added per unit of distance from the center, controlling how fast the ripple travels outward. Defaults to 120 ms.
 */
@Composable
fun RippleGrid(
    modifier: Modifier = Modifier,
    gridSize: Int = 5,
    dotSize: Dp = 6.dp,
    spacing: Dp = 6.dp,
    color: Color = Color.White,
    pulseDurationMillis: Int = 700,
    rippleSpeedMillis: Int = 120,
) {
    val infiniteTransition = rememberInfiniteTransition()
    val center = (gridSize - 1) / 2f
    val maxDistance = hypot(center, center)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        for (row in 0 until gridSize) {
            Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                for (col in 0 until gridSize) {
                    val distance = hypot(row - center, col - center)
                    val delayMillis = (distance * rippleSpeedMillis).toInt()
                    val cycleMillis = (maxDistance * rippleSpeedMillis).toInt() + pulseDurationMillis * 2

                    val pulse by infiniteTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(
                                durationMillis = max(cycleMillis, pulseDurationMillis),
                                delayMillis = delayMillis,
                                easing = FastOutSlowInEasing
                            ),
                            repeatMode = RepeatMode.Reverse
                        )
                    )

                    Box(
                        modifier = Modifier
                            .size(dotSize)
                            .graphicsLayer {
                                val scale = 0.4f + 0.6f * pulse
                                scaleX = scale
                                scaleY = scale
                                alpha = 0.3f + 0.7f * pulse
                            }
                            .background(color, CircleShape)
                    )
                }
            }
        }
    }
}
