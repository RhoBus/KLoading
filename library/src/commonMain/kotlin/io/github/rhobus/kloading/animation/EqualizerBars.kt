package io.github.rhobus.kloading.animation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import kotlin.random.Random

/**
 * [EqualizerBars] displays vertical bars that jump abruptly between randomized heights, evoking
 * a music player's audio level meter. This is deliberately distinct from [BarsWave]: where
 * `BarsWave` bars glide smoothly through a shared sine wave, these bars snap independently
 * between pseudo-random keyframes, reading as staccato rather than fluid.
 *
 * @param modifier The modifier to be applied to the container of the animation.
 * @param barCount The number of bars. Defaults to 5.
 * @param barWidth The width of each bar. Defaults to 6.dp.
 * @param barMaxHeight The tallest a bar can jump to. Defaults to 28.dp.
 * @param barMinHeight The shortest a bar can drop to. Defaults to 6.dp.
 * @param barSpacing The horizontal spacing between bars. Defaults to 5.dp.
 * @param color The color of the bars. Defaults to `Color.White`.
 * @param cycleMillis The duration in milliseconds of one full randomized keyframe sequence. Defaults to 900 ms.
 * @param stepsPerCycle The number of random height jumps within one cycle. Defaults to 5.
 * @param seed A seed for the pseudo-random heights, so the pattern is reproducible across recompositions. Defaults to 7.
 */
@Composable
fun EqualizerBars(
    modifier: Modifier = Modifier,
    barCount: Int = 5,
    barWidth: Dp = 6.dp,
    barMaxHeight: Dp = 28.dp,
    barMinHeight: Dp = 6.dp,
    barSpacing: Dp = 5.dp,
    color: Color = Color.White,
    cycleMillis: Int = 900,
    stepsPerCycle: Int = 5,
    seed: Int = 7,
) {
    val infiniteTransition = rememberInfiniteTransition()
    val random = remember { Random(seed) }

    // precompute a distinct random keyframe sequence per bar
    val barKeyframes = remember {
        List(barCount) { List(stepsPerCycle) { random.nextFloat() } }
    }

    Box(
        modifier = modifier
            .height(barMaxHeight)
            .wrapContentWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            horizontalArrangement = Arrangement.spacedBy(barSpacing),
            verticalAlignment = Alignment.Bottom
        ) {
            barKeyframes.forEachIndexed { barIndex, targets ->
                val stepMillis = cycleMillis / stepsPerCycle

                val level by infiniteTransition.animateFloat(
                    initialValue = targets.first(),
                    targetValue = targets.first(),
                    animationSpec = infiniteRepeatable(
                        animation = keyframes {
                            durationMillis = cycleMillis
                            targets.forEachIndexed { i, value ->
                                value at (i * stepMillis) using FastOutSlowInEasing
                            }
                        },
                        repeatMode = RepeatMode.Restart
                    )
                )

                val height = lerp(barMinHeight, barMaxHeight, level)

                Box(
                    modifier = Modifier
                        .width(barWidth)
                        .height(height)
                        .background(color, RoundedCornerShape(3.dp))
                )
            }
        }
    }
}
