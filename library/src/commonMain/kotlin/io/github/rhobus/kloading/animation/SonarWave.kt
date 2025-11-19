package io.github.rhobus.kloading.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * [SonarWave] displays a sonar wave animation with multiple pulsing circles.
 *
 * @param color The base color of the sonar waves. Defaults to `Color.White`.
 * @param waveCount The number of individual waves (circles) to display. Defaults to 3.
 * @param maxRadius The maximum radius a wave will reach. Defaults to 48.dp. The total size of the composable will be `maxRadius * 2`.
 * @param durationMillis The duration in milliseconds for one complete cycle of a single wave, from its appearance to its disappearance. Defaults to 2000 ms.
 * @param modifier The modifier to be applied to the container of the animation.
 */
@Composable
fun SonarWave(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    waveCount: Int = 3,
    maxRadius: Dp = 48.dp,
    durationMillis: Int = 2000
) {
    val infiniteTransition = rememberInfiniteTransition()

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(maxRadius * 2)
    ) {
        repeat(waveCount) { index ->
            val delay = durationMillis / waveCount * index

            val animation = infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = durationMillis,
                        delayMillis = delay,
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Restart
                )
            )

            val radius = maxRadius * animation.value
            val alpha = (1f - animation.value).coerceIn(0f, 1f)

            Box(
                modifier = Modifier
                    .size(radius * 2)
                    .graphicsLayer {
                        this.alpha = alpha
                    }
                    .background(color.copy(alpha = alpha), shape = CircleShape)
            )
        }
    }
}