package io.github.rhobus.kloading.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutElastic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.rhobus.kloading.core.extension.toRadians
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

/**
 * [WatchTickingAnimation] displays a watch ticking animation.
 *
 * @param clockSize The diameter of the clock.
 * @param handColor The color of the clock hand. Defaults to `Color.White`.
 * @param clockColor The color of the clock's circular outline. Defaults to `Color.White` with 0.2f alpha.
 * @param handWidth The stroke width of the clock hand.
 * @param animationDurationMs The duration of one 90-degree turn of the hand in milliseconds.
 * @param pauseDurationMs The duration of the pause after each 90-degree turn in milliseconds.
 * @param modifier The modifier to be applied to the container of the animation.
 */
@Composable
fun WatchTickingAnimation(
    modifier: Modifier = Modifier,
    clockSize: Dp = 40.dp,
    handColor: Color = Color.White,
    clockColor: Color = Color.White.copy(alpha = 0.2f),
    handWidth: Dp = 3.dp,
    animationDurationMs: Int = 700,
    pauseDurationMs: Int = 50,
) {
    val rotation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        var currentAngle = 0f
        while (true) {
            currentAngle += 90f
            rotation.animateTo(
                targetValue = currentAngle,
                animationSpec = tween(durationMillis = animationDurationMs, easing = EaseOutElastic)
            )
            delay(pauseDurationMs.toLong())
        }
    }

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
            val visualAngle = rotation.value % 360f
            val angleRad = (visualAngle - 90).toRadians
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