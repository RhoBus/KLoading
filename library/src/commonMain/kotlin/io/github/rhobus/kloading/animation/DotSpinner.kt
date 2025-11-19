package io.github.rhobus.kloading.animation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import io.github.rhobus.kloading.core.extension.toRadians
import io.github.rhobus.kloading.core.shape.Circle
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * [DotSpinner] displays a spinning animation with multiple dots revolving around a central point. The dots also
 * move outwards and inwards from the center, creating a pulsing effect.
 *
 * @param color The color of the dots. Defaults to `Color.White`.
 * @param dotSize The size (diameter) of each individual dot. Defaults to 8.dp.
 * @param maxRadius The maximum radius the dots will reach from the center during their animation.
 * The total size of the composable will be `maxRadius * 2 + dotSize`. Defaults to 20.dp.
 * @param durationMillis The duration in milliseconds for one full rotation cycle of the dots.
 * The inward/outward pulsing animation will complete in half this duration. Defaults to 1200ms.
 * @param modifier The modifier to be applied to the container of the animation.
 */
@Composable
fun DotSpinner(
    color: Color = Color.White,
    dotSize: Dp = 8.dp,
    maxRadius: Dp = 20.dp,
    durationMillis: Int = 1200,
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition()
    val density = LocalDensity.current

    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val radiusFraction by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis / 2, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = modifier.size(maxRadius * 2 + dotSize),
        contentAlignment = Alignment.Center
    ) {
        val dotCount = 3
        val angleBetweenDots = 360f / dotCount

        repeat(dotCount) { i ->
            val angleDeg = rotationAngle + i * angleBetweenDots
            val angleRad = angleDeg.toRadians

            val offset = with(density) {
                val dynamicRadius = maxRadius.toPx() * radiusFraction
                val x = (dynamicRadius * cos(angleRad)).toFloat()
                val y = (dynamicRadius * sin(angleRad)).toFloat()
                IntOffset(x.roundToInt(), y.roundToInt())
            }

            Box(modifier = Modifier.offset { offset }) {
                Circle(color = color, size = dotSize)
            }
        }
    }
}