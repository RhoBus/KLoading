package io.github.rhobus.kloading.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

/**
 * [CompassNeedle] displays a compass needle that periodically "hunts" toward a new heading with
 * an elastic, slightly overshooting motion — the way a real magnetic needle wobbles past north
 * before settling — rather than easing smoothly to a stop.
 *
 * @param modifier The modifier to be applied to the container of the animation.
 * @param diskSize The size (width and height) of the compass face. Defaults to 44.dp.
 * @param needleColorNorth The color of the north-pointing half of the needle. Defaults to `Color.White`.
 * @param needleColorSouth The color of the south-pointing half of the needle. Defaults to `Color.White` at 0.3f alpha.
 * @param ringColor The color of the compass face ring. Defaults to `Color.White` at 0.2f alpha.
 * @param headingHoldMillis The time in milliseconds the needle rests at a heading before hunting to a new one. Defaults to 1200 ms.
 * @param maxDriftDegrees The maximum random drift from true north for each new heading. Defaults to 25f.
 */
@Composable
fun CompassNeedle(
    modifier: Modifier = Modifier,
    diskSize: Dp = 44.dp,
    needleColorNorth: Color = Color.White,
    needleColorSouth: Color = Color.White.copy(alpha = 0.3f),
    ringColor: Color = Color.White.copy(alpha = 0.2f),
    headingHoldMillis: Int = 600,
    maxDriftDegrees: Float = 45f,
) {
    val rotation = remember { Animatable(0f) }
    val random = remember { Random(11) }

    LaunchedEffect(Unit) {
        while (true) {
            val target = (random.nextFloat() * 2f - 1f) * maxDriftDegrees
            rotation.animateTo(
                targetValue = target,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            delay(headingHoldMillis.toLong().milliseconds)
        }
    }

    Canvas(modifier = modifier.size(diskSize)) {
        val radius = this.size.minDimension / 2f
        val center = this.center

        drawCircle(color = ringColor, radius = radius, style = Stroke(width = 1.5.dp.toPx()))

        rotate(rotation.value, pivot = center) {
            val needleLength = radius * 0.75f

            val north = Path().apply {
                moveTo(center.x, center.y - needleLength)
                lineTo(center.x - radius * 0.12f, center.y)
                lineTo(center.x + radius * 0.12f, center.y)
                close()
            }
            val south = Path().apply {
                moveTo(center.x, center.y + needleLength)
                lineTo(center.x - radius * 0.12f, center.y)
                lineTo(center.x + radius * 0.12f, center.y)
                close()
            }

            drawPath(north, color = needleColorNorth)
            drawPath(south, color = needleColorSouth)
            drawCircle(color = needleColorNorth, radius = radius * 0.06f, center = center)
        }
    }
}
