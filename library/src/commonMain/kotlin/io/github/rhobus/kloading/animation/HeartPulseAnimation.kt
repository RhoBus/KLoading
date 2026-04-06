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
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private const val heartPulse = "heartPulse"
private const val dashProgress = "dashProgress"

private val defaultDashColor = Color.White
private val defaultPathColor = Color.White.copy(alpha = 0.1f)

/**
 * Displays an animated heart pulse line using a dashed stroke moving across a static path.
 *
 * The animation simulates a heartbeat monitor by combining:
 * - A static background path (low opacity)
 * - A moving dashed stroke (foreground pulse)
 *
 * @param modifier Modifier applied to the canvas container.
 * @param dashColor Color of the animated pulse segment.
 * @param pathColor Color of the static background path.
 * @param duration Duration of one full pulse animation cycle in milliseconds.
 * @param strokeWidth Width of the line used to draw both the static and animated paths.
 */
@Composable
fun HeartPulseAnimation(
    modifier: Modifier = Modifier,
    dashColor: Color = defaultDashColor,
    pathColor: Color = defaultPathColor,
    duration: Int = 2000,
    strokeWidth: Dp = 8.dp,
) {
    val transition = rememberInfiniteTransition(label = heartPulse)

    // handles the horizontal movement of the dashed pulse along the path
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = duration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = dashProgress,
    )

    val strokeWidthPx = with(LocalDensity.current) { strokeWidth.toPx() }

    Canvas(modifier = modifier.size(50.dp)) {
        val scaleX = size.width / 64f
        val scaleY = size.height / 48f

        val rawPoints = listOf(
            Offset(0.157f, 23.954f),
            Offset(14f, 23.954f),
            Offset(21.843f, 48f),
            Offset(43f, 0f),
            Offset(50f, 24f),
            Offset(64f, 24f),
        )

        val points = rawPoints.map { Offset(it.x * scaleX, it.y * scaleY) }

        // draws the full base path as a faint reference line
        drawPolyline(
            points = points,
            color = pathColor,
            strokeWidth = strokeWidthPx,
        )

        val pathScale = minOf(scaleX, scaleY)
        val dashOn = 48f * pathScale
        val dashOff = 144f * pathScale
        val totalOffset = 192f * pathScale

        val dashOffset = totalOffset * (1f - progress)

        // draws the animated dashed segment on top of the base path.
        drawPolyline(
            points = points,
            color = dashColor,
            strokeWidth = strokeWidthPx,
            dashOn = dashOn,
            dashOff = dashOff,
            dashOffset = dashOffset,
        )
    }
}

private fun DrawScope.drawPolyline(
    points: List<Offset>,
    color: Color,
    strokeWidth: Float,
    dashOn: Float = 0f,
    dashOff: Float = 0f,
    dashOffset: Float = 0f,
) {
    if (points.size < 2) return

    val pathEffect: PathEffect? = if (dashOn > 0f && dashOff > 0f) {
        PathEffect.dashPathEffect(
            intervals = floatArrayOf(dashOn, dashOff),
            phase = dashOffset,
        )
    } else null

    val path = Path().apply {
        moveTo(points[0].x, points[0].y)
        for (i in 1 until points.size) {
            lineTo(points[i].x, points[i].y)
        }
    }

    drawPath(
        path = path,
        color = color,
        style = Stroke(
            width = strokeWidth,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round,
            pathEffect = pathEffect,
        ),
    )
}
