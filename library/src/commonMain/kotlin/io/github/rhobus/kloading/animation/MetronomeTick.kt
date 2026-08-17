package io.github.rhobus.kloading.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.rhobus.kloading.core.extension.toRadians
import kotlin.math.cos
import kotlin.math.sin

/**
 * [MetronomeTick] displays a metronome arm swinging between two extremes at constant angular
 * velocity, reversing direction abruptly at each end rather than easing into it. This mechanical,
 * linear back-and-forth is what distinguishes it from [PendulumSwing] (sine-based, smoothly
 * decelerating) and [WatchTickingAnimation] (elastic snap between fixed positions).
 *
 * @param modifier The modifier to be applied to the container of the animation.
 * @param size The size (width and height) of the composable. Defaults to 48.dp.
 * @param armColor The color of the swinging arm. Defaults to `Color.White`.
 * @param bodyColor The color of the static triangular body outline. Defaults to `Color.White` at 0.2f alpha.
 * @param maxAngleDegrees The maximum swing angle from vertical, on either side. Defaults to 28f.
 * @param tickMillis The duration in milliseconds for a single swing from one side to the other. Defaults to 500 ms.
 */
@Composable
fun MetronomeTick(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    armColor: Color = Color.White,
    bodyColor: Color = Color.White.copy(alpha = 0.2f),
    maxAngleDegrees: Float = 28f,
    tickMillis: Int = 500,
) {
    val angle = remember { Animatable(-maxAngleDegrees) }

    LaunchedEffect(maxAngleDegrees, tickMillis) {
        var target = maxAngleDegrees
        while (true) {
            angle.animateTo(target, tween(tickMillis, easing = LinearEasing))
            target = -target
        }
    }

    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val pivot = Offset(w / 2f, h * 0.92f)

        // static wedge body
        val body = Path().apply {
            moveTo(w * 0.28f, h)
            lineTo(w * 0.72f, h)
            lineTo(w * 0.58f, h * 0.1f)
            lineTo(w * 0.42f, h * 0.1f)
            close()
        }
        drawPath(body, color = bodyColor, style = Stroke(width = 1.5.dp.toPx()))

        // swinging arm
        val armLength = h * 0.82f
        val angleRad = (angle.value - 90f).toRadians
        val tip = Offset(
            x = pivot.x + (cos(angleRad) * armLength).toFloat(),
            y = pivot.y + (sin(angleRad) * armLength).toFloat(),
        )

        drawLine(
            color = armColor,
            start = pivot,
            end = tip,
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawCircle(color = armColor, radius = 2.5.dp.toPx(), center = pivot)
    }
}
