package io.github.rhobus.kloading.animation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * [BricksAnimation] displays an animation where four bricks appear one by one, then disappear all at once, and repeat.
 * The bricks are arranged in a 2x2 grid.
 *
 * @param modifier The modifier to be applied to the container of the animation.
 * @param brickSize The size of each individual brick (width and height). Defaults to 20.dp.
 * @param spacing The spacing between the bricks. Defaults to 4.dp.
 * @param color The color of the bricks. Defaults to `Color.White`.
 * @param animationDuration The duration in milliseconds for a single brick to pop in or out. Defaults to 250 ms.
 * @param delayBetweenPopIn The delay in milliseconds between each brick appearing. Defaults to 150 ms.
 * @param delayBeforePopOut The delay in milliseconds after all bricks have appeared and before they all disappear. Defaults to 700 ms.
 */
@Composable
fun BricksAnimation(
    modifier: Modifier = Modifier,
    brickSize: Dp = 20.dp,
    spacing: Dp = 4.dp,
    color: Color = Color.White,
    animationDuration: Int = 250,
    delayBetweenPopIn: Int = 150,
    delayBeforePopOut: Int = 700,
) {
    val totalBricks = 4

    val brickVisibilityStates = remember {
        mutableStateListOf<Boolean>().apply {
            repeat(totalBricks) { add(false) }
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            for (i in 0 until totalBricks) {
                brickVisibilityStates[i] = true
                delay(delayBetweenPopIn.toLong())
            }

            delay(delayBeforePopOut.toLong())

            for (i in 0 until totalBricks) {
                brickVisibilityStates[i] = false
            }

            delay(delayBetweenPopIn.toLong())
        }
    }

    Column(
        modifier = modifier.wrapContentSize(),
        verticalArrangement = Arrangement.spacedBy(spacing),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (row in 0 until 2) {
            Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                for (col in 0 until 2) {
                    val brickIndex = row * 2 + col

                    val scale by animateFloatAsState(
                        targetValue = if (brickVisibilityStates[brickIndex]) 1f else 0f,
                        animationSpec = tween(animationDuration),
                        label = "brickScale_${brickIndex}"
                    )

                    Box(
                        modifier = Modifier
                            .size(brickSize)
                            .graphicsLayer(
                                scaleX = scale,
                                scaleY = scale,
                                alpha = scale
                            )
                            .background(
                                color = color,
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                }
            }
        }
    }
}