package io.github.rhobus.kloading.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos

/**
 * [DnaHelix] displays a double helix: two columns of dots whose horizontal offset follows a
 * cosine curve in contrary phase (strand B is 180 degrees behind strand A), connected by thin
 * rungs. Advancing the phase over time makes the strands appear to twist continuously along
 * their vertical axis.
 *
 * @param modifier The modifier to be applied to the container of the animation.
 * @param width The width of the composable, defining the maximum horizontal spread of the helix. Defaults to 40.dp.
 * @param height The height of the composable. Defaults to 64.dp.
 * @param rungCount The number of rows (dot pairs) along the helix. Defaults to 8.
 * @param dotRadius The radius of each dot. Defaults to 3.dp.
 * @param strandColorA The color of the first strand's dots. Defaults to `Color.White`.
 * @param strandColorB The color of the second strand's dots. Defaults to `Color.White` at 0.5f alpha.
 * @param rungColor The color of the connecting rungs. Defaults to `Color.White` at 0.25f alpha.
 * @param durationMillis The duration in milliseconds for one full twist cycle. Defaults to 1600 ms.
 */
@Composable
fun DnaHelix(
    modifier: Modifier = Modifier,
    width: Dp = 40.dp,
    height: Dp = 64.dp,
    rungCount: Int = 8,
    dotRadius: Dp = 3.dp,
    strandColorA: Color = Color.White,
    strandColorB: Color = Color.White.copy(alpha = 0.5f),
    rungColor: Color = Color.White.copy(alpha = 0.25f),
    durationMillis: Int = 1600,
) {
    val infiniteTransition = rememberInfiniteTransition()

    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier = modifier.width(width).height(height)) {
        val dotRadiusPx = dotRadius.toPx()
        val amplitude = this.size.width / 2f - dotRadiusPx
        val centerX = this.size.width / 2f
        val rowSpacing = this.size.height / (rungCount - 1).coerceAtLeast(1)

        for (row in 0 until rungCount) {
            val y = row * rowSpacing
            val rowPhase = phase + row * (PI.toFloat() / (rungCount / 2f))

            val xA = centerX + cos(rowPhase) * amplitude
            val xB = centerX + cos(rowPhase + PI.toFloat()) * amplitude

            drawLine(
                color = rungColor,
                start = Offset(xA, y),
                end = Offset(xB, y),
                strokeWidth = 1.dp.toPx()
            )

            drawCircle(color = strandColorA, radius = dotRadiusPx, center = Offset(xA, y))
            drawCircle(color = strandColorB, radius = dotRadiusPx, center = Offset(xB, y))
        }
    }
}
