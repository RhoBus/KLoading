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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.rhobus.kloading.core.extension.toRadians
import kotlin.math.cos
import kotlin.math.sin

/**
 * [CometOrbit] displays a single bright dot orbiting a circular track with a trailing chain of
 * echo dots that shrink and fade behind it, like a comet's tail. This distinguishes it from
 * [DotSpinner] (several independently pulsing dots) and [SonarWave] (expanding rings) — here
 * there is exactly one "real" object and the rest is memory of where it has been.
 *
 * @param modifier The modifier to be applied to the container of the animation.
 * @param trackRadius The radius of the orbit track. Defaults to 20.dp.
 * @param cometRadius The radius of the leading comet dot. Defaults to 5.dp.
 * @param color The color of the comet and its trail. Defaults to `Color.White`.
 * @param trailLength The number of echo dots trailing the comet. Defaults to 6.
 * @param trailSpreadDegrees The angular spacing, in degrees, between successive echo dots. Defaults to 18f.
 * @param durationMillis The duration in milliseconds for one full orbit. Defaults to 1500 ms.
 */
@Composable
fun CometOrbit(
    modifier: Modifier = Modifier,
    trackRadius: Dp = 20.dp,
    cometRadius: Dp = 5.dp,
    color: Color = Color.White,
    trailLength: Int = 6,
    trailSpreadDegrees: Float = 18f,
    durationMillis: Int = 1500,
) {
    val infiniteTransition = rememberInfiniteTransition()

    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier = modifier.size(trackRadius * 2 + cometRadius)) {
        val center = this.center
        val trackRadiusPx = trackRadius.toPx()
        val cometRadiusPx = cometRadius.toPx()

        for (i in trailLength downTo 0) {
            val trailAngleDeg = angle - i * trailSpreadDegrees
            val angleRad = trailAngleDeg.toRadians

            val point = Offset(
                x = center.x + (cos(angleRad) * trackRadiusPx).toFloat(),
                y = center.y + (sin(angleRad) * trackRadiusPx).toFloat(),
            )

            val fraction = 1f - (i.toFloat() / trailLength)
            drawCircle(
                color = color.copy(alpha = fraction),
                radius = cometRadiusPx * (0.25f + 0.75f * fraction),
                center = point
            )
        }
    }
}
