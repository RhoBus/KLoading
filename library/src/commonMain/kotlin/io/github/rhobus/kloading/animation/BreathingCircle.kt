package io.github.rhobus.kloading.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

/**
 * [BreathingCircle] displays a circle that scales and fades through a four-phase breathing
 * cycle: inhale (grow), hold, exhale (shrink), hold. Unlike a simple reverse-repeating tween,
 * explicit holds at both extremes give the motion a calm, deliberate rhythm suited to slow
 * or ambient loading states.
 *
 * @param modifier The modifier to be applied to the container of the animation.
 * @param maxSize The diameter of the circle at full inhale. Defaults to 48.dp.
 * @param minScale The scale factor relative to [maxSize] at full exhale (0f–1f). Defaults to 0.55f.
 * @param color The color of the circle. Defaults to `Color.White`.
 * @param inhaleMillis Duration of the inhale (grow) phase. Defaults to 1600 ms.
 * @param holdMillis Duration of each hold phase, at full inhale and full exhale. Defaults to 500 ms.
 * @param exhaleMillis Duration of the exhale (shrink) phase. Defaults to 1600 ms.
 */
@Composable
fun BreathingCircle(
    modifier: Modifier = Modifier,
    maxSize: Dp = 48.dp,
    minScale: Float = 0.55f,
    color: Color = Color.White,
    inhaleMillis: Int = 800,
    holdMillis: Int = 250,
    exhaleMillis: Int = 800,
) {
    val scale = remember { Animatable(minScale) }
    val alpha = remember { Animatable(0.5f) }

    LaunchedEffect(Unit) {
        while (true) {
            // inhale
            scale.animateTo(1f, tween(inhaleMillis, easing = FastOutSlowInEasing))
            alpha.animateTo(1f, tween(inhaleMillis, easing = FastOutSlowInEasing))
            delay(holdMillis.toLong().milliseconds)

            // exhale
            scale.animateTo(minScale, tween(exhaleMillis, easing = FastOutSlowInEasing))
            alpha.animateTo(0.5f, tween(exhaleMillis, easing = FastOutSlowInEasing))
            delay(holdMillis.toLong().milliseconds)
        }
    }

    Box(
        modifier = modifier
            .size(maxSize)
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
                this.alpha = alpha.value
            }
            .background(color, CircleShape)
    )
}
