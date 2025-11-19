package io.github.rhobus.kloading.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.rhobus.kloading.core.extension.toRadians
import kotlin.math.cos
import kotlin.math.sin

/**
 * [WatchRunningAnimation] displays a continuously animating watch, simulating a running clock hand.
 *
 * @param clockSize The size of the entire clock composable. Defaults to 40.dp.
 * @param handColor The color of the rotating clock hand. Defaults to `Color.White`.
 * @param clockColor The color of the static clock circle. Defaults to `Color.White` with 0.2f alpha.
 * @param handWidth The stroke width for both the clock circle and the clock hand. Defaults to 3.dp.
 * @param duration The duration of one full rotation of the clock hand in milliseconds. Defaults to 1000 ms.
 * @param modifier The modifier to be applied to the container of the animation.
 */
@Composable
fun WatchRunningAnimation(
    modifier: Modifier = Modifier,
    clockSize: Dp = 40.dp,
    handColor: Color = Color.White,
    clockColor: Color = Color.White.copy(alpha = 0.2f),
    handWidth: Dp = 3.dp,
    duration: Int = 1000,
) {
    val infiniteTransition = rememberInfiniteTransition()

    val rotationDegrees by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = duration,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = modifier
            .size(clockSize)
            .wrapContentSize(Alignment.Center)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = center
            val radius = size.minDimension / 2

            drawCircle(
                color = clockColor,
                radius = radius,
                center = center,
                style = Stroke(width = handWidth.toPx())
            )

            val handLength = radius * 0.7f
            val angleRad = (rotationDegrees - 90).toRadians
            val endX = center.x + cos(angleRad) * handLength
            val endY = center.y + sin(angleRad) * handLength

            drawLine(
                color = handColor,
                start = center,
                end = Offset(endX.toFloat(), endY.toFloat()),
                strokeWidth = handWidth.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}
