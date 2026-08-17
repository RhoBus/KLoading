package io.github.rhobus.kloading.animation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.rhobus.kloading.core.extension.toRadians
import kotlin.math.cos
import kotlin.math.sin

/**
 * [FlowerBloom] displays petals arranged around a center that open outward with a staggered
 * delay per petal, then all close back together — like a time-lapse of a flower blooming and
 * folding shut, rather than a symmetric group animation where every petal moves in lockstep.
 *
 * @param modifier The modifier to be applied to the container of the animation.
 * @param petalCount The number of petals. Defaults to 6.
 * @param petalSize The size (width and height) of each petal at full bloom. Defaults to 10.dp.
 * @param bloomRadius The distance petals travel from the center when fully bloomed. Defaults to 16.dp.
 * @param color The color of the petals. Defaults to `Color.White`.
 * @param centerColor The color of the flower's center dot. Defaults to `Color.White`.
 * @param cycleMillis The duration in milliseconds for one full open+close cycle. Defaults to 1800 ms.
 * @param staggerMillis The delay in milliseconds between each successive petal's animation start. Defaults to 60 ms.
 */
@Composable
fun FlowerBloom(
    modifier: Modifier = Modifier,
    petalCount: Int = 6,
    petalSize: Dp = 10.dp,
    bloomRadius: Dp = 16.dp,
    color: Color = Color.White,
    centerColor: Color = Color.White,
    cycleMillis: Int = 1800,
    staggerMillis: Int = 60,
) {
    val infiniteTransition = rememberInfiniteTransition()

    Box(
        modifier = modifier.size(bloomRadius * 2 + petalSize),
        contentAlignment = Alignment.Center
    ) {
        repeat(petalCount) { i ->
            val angleDeg = 360f / petalCount * i
            val angleRad = angleDeg.toRadians

            val bloom by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = cycleMillis - petalCount * staggerMillis,
                        delayMillis = i * staggerMillis,
                        easing = FastOutSlowInEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                )
            )

            val offsetX = (cos(angleRad) * bloomRadius.value * bloom).dp
            val offsetY = (sin(angleRad) * bloomRadius.value * bloom).dp

            Box(
                modifier = Modifier
                    .offset(x = offsetX, y = offsetY)
                    .size(petalSize)
                    .graphicsLayer {
                        scaleX = 0.4f + 0.6f * bloom
                        scaleY = 0.4f + 0.6f * bloom
                        alpha = 0.3f + 0.7f * bloom
                    }
                    .background(color, RoundedCornerShape(50))
            )
        }

        Box(
            modifier = Modifier
                .size(petalSize * 0.7f)
                .background(centerColor, RoundedCornerShape(50))
        )
    }
}
