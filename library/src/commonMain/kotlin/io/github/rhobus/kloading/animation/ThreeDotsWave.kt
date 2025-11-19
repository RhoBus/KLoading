package io.github.rhobus.kloading.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.rhobus.kloading.core.shape.Circle
import kotlin.math.PI
import kotlin.math.sin

/**
 * [ThreeDotsWave] displays an animated sequence of three dots waving in a sine wave pattern.
 *
 * @param color The color of each dot. Default is `Color.White`.
 * @param size The size (diameter) of each dot. Default is 8.dp.
 * @param spaceBetween The horizontal space between the dots. Default is 8.dp.
 * @param modifier The modifier to be applied to the container of the animation.
 *
 */
@Composable
fun ThreeDotsWave(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    size: Dp = 8.dp,
    spaceBetween: Dp = 8.dp,
) {
    val infiniteTransition = rememberInfiniteTransition()

    val waveAnim = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val dotCount = 3
    val phaseShift = (2 * PI / dotCount).toFloat()

    Row(
        modifier = modifier.height(size * 3f),
        horizontalArrangement = Arrangement.spacedBy(spaceBetween),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(dotCount) { i ->
            val offsetY = sin(waveAnim.value + i * phaseShift) * size.value
            Box(
                modifier = Modifier
                    .offset(y = (-offsetY).dp)
            ) {
                Circle(
                    color = color,
                    size = size,
                )
            }
        }
    }
}