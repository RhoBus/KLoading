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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.rhobus.kloading.core.extension.toRadians
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * [PendulumSwing] displays a swinging pendulum. Unlike an eased tween, the angle is driven
 * directly by a sine function of a linearly advancing phase, which reproduces the real
 * kinematics of a pendulum: fastest at the bottom of the arc, slowest (and momentarily still)
 * at each extreme.
 *
 * @param modifier The modifier to be applied to the container of the animation.
 * @param size The size (width and height) of the composable. Defaults to 56.dp.
 * @param armLength The length of the pendulum arm, as a fraction of [size]. Defaults to 0.75f.
 * @param maxAngleDegrees The maximum swing angle from vertical, in degrees, on either side. Defaults to 35f.
 * @param bobRadius The radius of the bob at the end of the arm. Defaults to 6.dp.
 * @param color The color of the arm and bob. Defaults to `Color.White`.
 * @param durationMillis The duration in milliseconds for one full swing cycle (left-right-left). Defaults to 1400 ms.
 * @param strokeWidth The width of the pendulum arm and pivot stroke. Defaults to 2.dp.
 */
@Composable
fun PendulumSwing(
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
    armLength: Float = 0.75f,
    maxAngleDegrees: Float = 35f,
    bobRadius: Dp = 6.dp,
    color: Color = Color.White,
    durationMillis: Int = 1400,
    strokeWidth: Dp = 2.dp,
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

    Canvas(modifier = modifier.size(size)) {
        val strokeWidthPx = strokeWidth.toPx()
        val bobRadiusPx = bobRadius.toPx()
        val pivot = Offset(this.size.width / 2f, bobRadiusPx)
        val length = this.size.height * armLength

        val angleDeg = maxAngleDegrees * sin(phase)
        val angleRad = (angleDeg + 90f).toRadians

        val bobCenter = Offset(
            x = pivot.x + (cos(angleRad) * length).toFloat(),
            y = pivot.y + (sin(angleRad) * length).toFloat(),
        )

        // pivot anchor
        drawCircle(color = color.copy(alpha = 0.6f), radius = strokeWidthPx, center = pivot)

        // arm
        drawLine(
            color = color,
            start = pivot,
            end = bobCenter,
            strokeWidth = strokeWidthPx,
            cap = StrokeCap.Round
        )

        // bob
        drawCircle(color = color, radius = bobRadiusPx, center = bobCenter)
    }
}
