package io.github.rhobus.kloading.animation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

/**
 * [RotatingBricks] displays an animation where four bricks rotate their positions in a square.
 *
 * @param modifier The modifier to be applied to the container of the animation.
 * @param brickSize The size of each individual brick (width and height). Defaults to 20.dp.
 * @param spacing The spacing between the bricks. Defaults to 4.dp.
 * @param color The color of the bricks. Defaults to `Color.White`.
 * @param animationDuration The duration in milliseconds for a single brick to animate to its new position. Defaults to 400 ms.
 * @param delayBetween The delay in milliseconds between each rotation cycle of the bricks. Defaults to 1200 ms.
 */
@Composable
fun RotatingBricks(
    modifier: Modifier = Modifier,
    brickSize: Dp = 20.dp,
    spacing: Dp = 4.dp,
    color: Color = Color.White,
    animationDuration: Int = 400,
    delayBetween: Int = 1200
) {
    val positions = listOf(
        Offset(0f, 0f),
        Offset(1f, 0f),
        Offset(1f, 1f),
        Offset(0f, 1f),
    )

    val brickOrder = remember { mutableStateListOf(0, 1, 2, 3) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(delayBetween.toLong())
            val first = brickOrder.removeAt(0)
            brickOrder.add(first)
        }
    }

    Box(
        modifier = modifier.size((brickSize * 2) + spacing),
        contentAlignment = Alignment.TopStart
    ) {
        val brickPx = with(LocalDensity.current) { brickSize.toPx() + spacing.toPx() }

        brickOrder.forEach { posIndex ->
            val targetOffset = positions[posIndex] * brickPx
            val animatedOffsetX by animateFloatAsState(
                targetValue = targetOffset.x,
                animationSpec = tween(durationMillis = animationDuration, easing = FastOutSlowInEasing),
            )
            val animatedOffsetY by animateFloatAsState(
                targetValue = targetOffset.y,
                animationSpec = tween(durationMillis = animationDuration, easing = FastOutSlowInEasing),
            )

            Box(
                modifier = Modifier
                    .offset { IntOffset(animatedOffsetX.roundToInt(), animatedOffsetY.roundToInt()) }
                    .size(brickSize)
                    .background(
                        color = color,
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}