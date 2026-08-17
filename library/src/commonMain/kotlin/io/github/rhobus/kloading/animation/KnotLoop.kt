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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * [KnotLoop] displays a dot continuously tracing a figure-eight (lemniscate) path, with the
 * full path drawn faintly underneath as a guide. The closed, self-crossing curve reads as an
 * "infinite" loading motif — there is no start or end to the motion, only a single point
 * moving endlessly along it.
 *
 * @param modifier The modifier to be applied to the container of the animation.
 * @param size The size (width and height) of the composable. Defaults to 56.dp.
 * @param dotRadius The radius of the tracing dot. Defaults to 5.dp.
 * @param dotColor The color of the tracing dot. Defaults to `Color.White`.
 * @param pathColor The color of the faint guide path. Defaults to `Color.White` at 0.15f alpha.
 * @param durationMillis The duration in milliseconds for one full trace of the loop. Defaults to 2200 ms.
 */
@Composable
fun KnotLoop(
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
    dotRadius: Dp = 5.dp,
    dotColor: Color = Color.White,
    pathColor: Color = Color.White.copy(alpha = 0.15f),
    durationMillis: Int = 2200,
) {
    val infiniteTransition = rememberInfiniteTransition()

    val t by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier = modifier.size(size)) {
        val center = this.center
        val a = this.size.minDimension / 2f * 0.85f

        fun lemniscatePoint(theta: Float): Offset {
            val denom = 1f + sin(theta) * sin(theta)
            val x = (a * cos(theta)) / denom
            val y = (a * sin(theta) * cos(theta)) / denom
            return Offset(center.x + x, center.y + y)
        }

        // faint guide path
        val guide = Path()
        val steps = 120
        for (i in 0..steps) {
            val theta = (i.toFloat() / steps) * 2 * PI.toFloat()
            val p = lemniscatePoint(theta)
            if (i == 0) guide.moveTo(p.x, p.y) else guide.lineTo(p.x, p.y)
        }
        guide.close()
        drawPath(guide, color = pathColor, style = Stroke(width = 1.5.dp.toPx()))

        // moving dot
        val dotPoint = lemniscatePoint(t)
        drawCircle(color = dotColor, radius = dotRadius.toPx(), center = dotPoint)
    }
}
