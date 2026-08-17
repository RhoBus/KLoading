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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * [GearMesh] displays two interlocking gears rotating in opposite directions at a speed ratio
 * inverse to their tooth counts, the way real meshed gears must move to stay engaged: the
 * smaller gear always spins faster.
 *
 * @param modifier The modifier to be applied to the container of the animation.
 * @param color The color of both gears. Defaults to `Color.White`.
 * @param largeTeeth The number of teeth on the larger, slower gear. Defaults to 10.
 * @param smallTeeth The number of teeth on the smaller, faster gear. Defaults to 6.
 * @param largeRadius The outer radius of the larger gear. Defaults to 20.dp.
 * @param smallRadius The outer radius of the smaller gear. Defaults to 13.dp.
 * @param durationMillis The duration in milliseconds for one full rotation of the larger gear. Defaults to 3000 ms.
 */
@Composable
fun GearMesh(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    largeTeeth: Int = 10,
    smallTeeth: Int = 6,
    largeRadius: Dp = 20.dp,
    smallRadius: Dp = 13.dp,
    durationMillis: Int = 3000,
) {
    val infiniteTransition = rememberInfiniteTransition()

    val largeRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier = modifier.size(largeRadius * 2 + smallRadius)) {
        val largeCenter = Offset(largeRadius.toPx(), this.size.height - largeRadius.toPx())
        val smallCenter = Offset(
            this.size.width - smallRadius.toPx(),
            largeCenter.y - largeRadius.toPx() - smallRadius.toPx() * 0.15f
        )

        // smaller gear spins faster and opposite direction, proportional to tooth ratio
        val smallRotation = -largeRotation * (largeTeeth.toFloat() / smallTeeth)

        rotate(largeRotation, pivot = largeCenter) {
            drawGear(largeCenter, largeRadius.toPx(), largeTeeth, color)
        }
        rotate(smallRotation, pivot = smallCenter) {
            drawGear(smallCenter, smallRadius.toPx(), smallTeeth, color)
        }
    }
}

private fun DrawScope.drawGear(center: Offset, radius: Float, teeth: Int, color: Color) {
    val innerRadius = radius * 0.68f
    val toothDepth = radius * 0.22f
    val hubRadius = radius * 0.28f
    val path = Path()
    val pointsPerTooth = 4
    val totalPoints = teeth * pointsPerTooth

    for (i in 0..totalPoints) {
        val angle = (2 * PI * i / totalPoints).toFloat()
        val toothPhase = (i % pointsPerTooth)
        val r = if (toothPhase < pointsPerTooth / 2) radius else radius - toothDepth
        val point = Offset(center.x + cos(angle) * r, center.y + sin(angle) * r)
        if (i == 0) path.moveTo(point.x, point.y) else path.lineTo(point.x, point.y)
    }
    path.close()

    drawPath(path, color = color.copy(alpha = 0.85f))
    drawCircle(color = color.copy(alpha = 0.85f), radius = innerRadius * 0.05f + hubRadius, center = center)
    drawCircle(color = Color.Transparent, radius = hubRadius * 0.4f, center = center)
}
