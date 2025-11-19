package io.github.rhobus.kloading.animation

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.rhobus.kloading.core.shape.Square

/**
 * [RotatingSquare] displays a continuously rotating square.
 *
 * @param squareSize The size of the square (both width and height). Defaults to 48.dp.
 * @param color The color of the square. Defaults to `Color.White`.
 * @param duration The duration in milliseconds for one full 360-degree rotation of the square. Defaults to 1250 ms.
 * @param modifier The modifier to be applied to the container of the animation.
 */
@Composable
fun RotatingSquare(
    squareSize: Dp = 48.dp,
    color: Color = Color.White,
    duration: Int = 1250,
    modifier: Modifier = Modifier,
) {
    val rotation by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = duration, easing = EaseInOut),
            repeatMode = RepeatMode.Restart
        )
    )
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Square(modifier = Modifier.rotate(rotation), color = color, size = squareSize)
    }
}
