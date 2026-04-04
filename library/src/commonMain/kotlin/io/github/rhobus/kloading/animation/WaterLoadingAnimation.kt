package io.github.rhobus.kloading.animation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin

// default colors
private val defaultFrontWaveColor = Color(0xFF4FC3F7)
private val defaultBackWaveColor = Color(0xFF0288D1)

// animation label
const val waveTransition = "waveTransition"
const val fillProgress = "fillProgress"
const val waveOffset = "waveOffset"

/**
 * [WaterWaveAnimation] displays a circular progress indicator with two symmetric sine waves.
 * It features a vertical rocking effect, smooth fill transitions, and a customizable border.
 *
 * @param modifier The modifier to be applied to the layout.
 * @param progress Filling level from 0.0 (empty) to 1.0 (full).
 * @param size The diameter of the circular container.
 * @param frontWaveColor Color of the primary wave.
 * @param backWaveColor Color of the secondary wave (inverted phase).
 * @param durationMillis Duration of one horizontal wave cycle.
 * @param amplitudeRatio Wave height relative to the container height.
 * @param waveLengthRatio Wave width relative to the container width.
 * @param rockingAmount Vertical oscillation magnitude to simulate container movement.
 */
@Composable
fun WaterWaveAnimation(
    modifier: Modifier = Modifier,
    progress: Float = 0.5f,
    size: Dp = 120.dp,
    frontWaveColor: Color = defaultFrontWaveColor,
    backWaveColor: Color = defaultBackWaveColor,
    durationMillis: Int = 1000,
    amplitudeRatio: Float = 0.07f,
    waveLengthRatio: Float = 1.25f,
    rockingAmount: Float = 0.03f,
) {
    val infiniteTransition = rememberInfiniteTransition(label = waveTransition)

    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 50, easing = FastOutSlowInEasing),
        label = fillProgress
    )

    val waveMovement by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = waveOffset
    )

    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
    ) {
        Canvas(modifier = Modifier.fillMaxSize().clip(CircleShape)) {
            val canvasWidth = this.size.width
            val canvasHeight = this.size.height
            val amplitude = canvasHeight * amplitudeRatio
            val waveLength = canvasWidth * waveLengthRatio

            val rockingOffset =
                sin(waveMovement * 2 * PI).toFloat() * (canvasHeight * rockingAmount)
            val baseWaterLevel = (canvasHeight * (1f - animatedProgress)) + rockingOffset
            val frequency = (2f * PI / waveLength).toFloat()

            fun drawWave(color: Color, offset: Float, isInverted: Boolean) {
                val path = Path()
                val phase =
                    (offset * waveLength * frequency) + (if (isInverted) PI.toFloat() else 0f)

                path.moveTo(0f, baseWaterLevel)

                for (x in 0..canvasWidth.toInt()) {
                    val relativeX = x.toFloat()
                    val direction = if (isInverted) -1f else 1f
                    val y =
                        baseWaterLevel + (sin((relativeX * direction) * frequency + phase) * amplitude)
                    path.lineTo(relativeX, y)
                }

                path.lineTo(canvasWidth, canvasHeight)
                path.lineTo(0f, canvasHeight)
                path.close()
                drawPath(path = path, color = color)
            }

            // Draw waves
            drawWave(backWaveColor, waveMovement, isInverted = true)
            drawWave(frontWaveColor, waveMovement, isInverted = false)

        }
    }
}
