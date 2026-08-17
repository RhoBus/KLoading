package io.github.rhobus.kloading.animation

import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * [PulsingRing] displays a single ring that breathes in radius, stroke width, and opacity all
 * at once. It is deliberately the simplest animation in this set — one element carrying the
 * entire signal — for contexts where a busier composition would be too loud.
 *
 * @param modifier The modifier to be applied to the container of the animation.
 * @param maxSize The diameter of the ring at its largest. Defaults to 40.dp.
 * @param color The color of the ring. Defaults to `Color.White`.
 * @param minScale The scale factor of the ring at its smallest, relative to [maxSize]. Defaults to 0.65f.
 * @param strokeWidth The stroke width of the ring at its largest (it thins slightly as it grows). Defaults to 3.dp.
 * @param durationMillis The duration in milliseconds for one grow-or-shrink half cycle. Defaults to 900 ms.
 */
@Composable
fun PulsingRing(
    modifier: Modifier = Modifier,
    maxSize: Dp = 40.dp,
    color: Color = Color.White,
    minScale: Float = 0.65f,
    strokeWidth: Dp = 3.dp,
    durationMillis: Int = 900,
) {
    val infiniteTransition = rememberInfiniteTransition()

    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(modifier = modifier.size(maxSize)) {
        val radius = (this.size.minDimension / 2f) * (minScale + (1f - minScale) * progress)
        val alpha = 0.35f + 0.65f * progress
        val width = strokeWidth.toPx() * (1.4f - 0.4f * progress)

        drawCircle(
            color = color.copy(alpha = alpha),
            radius = radius,
            center = this.center,
            style = Stroke(width = width)
        )
    }
}
