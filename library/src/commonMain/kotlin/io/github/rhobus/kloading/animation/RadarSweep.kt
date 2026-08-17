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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * [RadarSweep] displays a rotating radar sweep: a bright leading edge followed by a wedge of
 * color that fades to transparent across roughly a quarter turn, simulating the persistence
 * trail of a radar display's phosphor sweep.
 *
 * @param modifier The modifier to be applied to the container of the animation.
 * @param diskSize The diameter of the radar disc. Defaults to 56.dp.
 * @param sweepColor The color of the sweep, brightest at the leading edge. Defaults to `Color.White`.
 * @param ringColor The color of the static concentric range rings. Defaults to `Color.White` at 0.15f alpha.
 * @param ringCount The number of concentric range rings drawn. Defaults to 3.
 * @param durationMillis The duration in milliseconds for one full sweep rotation. Defaults to 2500 ms.
 */
@Composable
fun RadarSweep(
    modifier: Modifier = Modifier,
    diskSize: Dp = 56.dp,
    sweepColor: Color = Color.White,
    ringColor: Color = Color.White.copy(alpha = 0.15f),
    ringCount: Int = 3,
    durationMillis: Int = 2500,
) {
    val infiniteTransition = rememberInfiniteTransition()

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier = modifier.size(diskSize)) {
        val radius = this.size.minDimension / 2f
        val center = this.center

        drawCircle(color = ringColor, radius = radius, style = Stroke(width = 1.dp.toPx()))
        for (i in 1..ringCount) {
            drawCircle(
                color = ringColor,
                radius = radius * i / (ringCount + 1),
                style = Stroke(width = 1.dp.toPx())
            )
        }

        rotate(rotation, pivot = center) {
            val sweepBrush = Brush.sweepGradient(
                colorStops = arrayOf(
                    0f to Color.Transparent,
                    0.75f to Color.Transparent,
                    0.97f to sweepColor.copy(alpha = 0.35f),
                    1f to sweepColor,
                ),
                center = center
            )
            drawCircle(brush = sweepBrush, radius = radius, center = center)
        }
    }
}
