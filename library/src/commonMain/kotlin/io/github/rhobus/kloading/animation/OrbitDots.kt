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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.rhobus.kloading.core.extension.toRadians
import kotlin.math.cos
import kotlin.math.sin

/**
 * [OrbitDots] displays several dots orbiting a common center, each at its own radius and its
 * own period — inner dots complete a revolution faster than outer dots, echoing Kepler's
 * observation that closer orbits move faster. This differentiates it from [DotSpinner], where
 * all dots share a single rotation.
 *
 * @param modifier The modifier to be applied to the container of the animation.
 * @param orbitCount The number of orbiting dots (and orbits). Defaults to 3.
 * @param maxRadius The radius of the outermost orbit. Inner orbits are spaced evenly inside it. Defaults to 22.dp.
 * @param dotSize The size (diameter) of each orbiting dot. Defaults to 6.dp.
 * @param color The color of the dots. Defaults to `Color.White`.
 * @param baseDurationMillis The rotation duration of the outermost, slowest orbit in milliseconds. Inner orbits are proportionally faster. Defaults to 2400 ms.
 */
@Composable
fun OrbitDots(
    modifier: Modifier = Modifier,
    orbitCount: Int = 3,
    maxRadius: Dp = 22.dp,
    dotSize: Dp = 6.dp,
    color: Color = Color.White,
    baseDurationMillis: Int = 2400,
) {
    val infiniteTransition = rememberInfiniteTransition()

    val angles = (0 until orbitCount).map { index ->
        // inner orbits (smaller index+1 fraction) spin faster, like closer planets
        val orbitFraction = (index + 1f) / orbitCount
        val duration = (baseDurationMillis * orbitFraction).toInt().coerceAtLeast(200)

        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(duration, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    Canvas(modifier = modifier.size(maxRadius * 2 + dotSize)) {
        val center = this.center
        val dotRadiusPx = dotSize.toPx() / 2f

        angles.forEachIndexed { index, angle ->
            val orbitRadius = maxRadius.toPx() * (index + 1f) / orbitCount
            val angleRad = angle.value.toRadians

            val dotCenter = androidx.compose.ui.geometry.Offset(
                x = center.x + (cos(angleRad) * orbitRadius).toFloat(),
                y = center.y + (sin(angleRad) * orbitRadius).toFloat(),
            )

            drawCircle(color = color, radius = dotRadiusPx, center = dotCenter)
        }
    }
}
