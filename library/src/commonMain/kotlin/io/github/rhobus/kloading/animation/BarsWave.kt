package io.github.rhobus.kloading.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import io.github.rhobus.kloading.core.extension.toRadians
import kotlin.math.sin

/**
 * [BarsWave] displays a wave animation using a series of vertically oscillating bars.
 *
 * @param modifier The modifier to be applied to the container of the animation.
 * @param barCount The number of bars in the wave animation. Defaults to 5.
 * @param barWidth The width of each individual bar. Defaults to 8.dp.
 * @param barMaxHeight The maximum height a bar can reach during its oscillation. Defaults to 30.dp.
 * @param barMinHeight The minimum height a bar will have. Defaults to 8.dp.
 * @param barSpacing The horizontal spacing between the bars. Defaults to 8.dp.
 * @param durationMillis The duration in milliseconds for one complete wave cycle (for all bars to complete their oscillation pattern). Defaults to 1000 ms.
 * @param color The color of the bars. Defaults to `Color.White`.
 */
@Composable
fun BarsWave(
    modifier: Modifier = Modifier,
    barCount: Int = 5,
    barWidth: Dp = 8.dp,
    barMaxHeight: Dp = 30.dp,
    barMinHeight: Dp = 8.dp,
    barSpacing: Dp = 8.dp,
    durationMillis: Int = 1000,
    color: Color = Color.White,
) {
    Box(
        modifier = modifier
            .height(barMaxHeight)
            .wrapContentWidth()
    ) {
        val infiniteTransition = rememberInfiniteTransition()
        val phaseShift = 360f / barCount

        val animations = (0 until barCount).map { _ ->
            infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            )
        }

        Row(
            modifier = Modifier.fillMaxHeight(),
            horizontalArrangement = Arrangement.spacedBy(barSpacing),
            verticalAlignment = Alignment.Bottom
        ) {
            animations.forEachIndexed { index, anim ->
                val angle = (anim.value + index * phaseShift) % 360f
                val normalized = (sin(angle.toRadians) + 1) / 2
                val height = lerp(barMinHeight, barMaxHeight, normalized.toFloat())

                Box(
                    modifier = Modifier
                        .width(barWidth)
                        .height(height)
                        .background(color, RoundedCornerShape(4.dp))
                )
            }
        }
    }
}