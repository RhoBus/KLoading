package io.github.rhobus.kloading.animation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.rhobus.kloading.core.extension.toRadians

/**
 * [PageFold] displays a small card that continuously flips end-over-end around its vertical
 * axis, like a page turning. The front and back faces use different colors, so the color swap
 * at the 90-degree mark (when the card is edge-on and invisible) reinforces the illusion of
 * a physical flip rather than a flat rotation.
 *
 * @param modifier The modifier to be applied to the container of the animation.
 * @param cardSize The size (width and height) of the card. Defaults to 32.dp.
 * @param frontColor The color shown for the first half of the flip. Defaults to `Color.White`.
 * @param backColor The color shown for the second half of the flip. Defaults to `Color.White` at 0.4f alpha.
 * @param durationMillis The duration in milliseconds for one full 360-degree flip. Defaults to 1200 ms.
 * @param cornerRadius The corner radius of the card. Defaults to 6.dp.
 */
@Composable
fun PageFold(
    modifier: Modifier = Modifier,
    cardSize: Dp = 32.dp,
    frontColor: Color = Color.White,
    backColor: Color = Color.White.copy(alpha = 0.4f),
    durationMillis: Int = 1200,
    cornerRadius: Dp = 6.dp,
) {
    val infiniteTransition = rememberInfiniteTransition()

    val rotationY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val normalized = rotationY % 360f
    val showingFront = normalized !in 90f..270f
    // faces narrow to a sliver as they turn edge-on, like a real page
    val squash = kotlin.math.abs(kotlin.math.cos(normalized.toRadians)).toFloat()

    Box(
        modifier = modifier
            .size(cardSize)
            .graphicsLayer {
                scaleX = squash.coerceAtLeast(0.04f)
                cameraDistance = 12f * density
            }
            .background(
                color = if (showingFront) frontColor else backColor,
                shape = RoundedCornerShape(cornerRadius)
            )
    )
}
