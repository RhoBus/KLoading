package io.github.rhobus.kloading.core.shape

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A simple square-shaped container with customizable size, shape, and color.
 *
 * @param size The width and height of the square. Defaults to 48.dp.
 * @param shape The shape used to clip the square. Defaults to an 8.dp rounded corner shape.
 * @param color The background color of the square. Defaults to [Color.White].
 * @param modifier Optional [Modifier] for additional layout or styling.
 */
@Composable
fun Square(
    size: Dp = 48.dp,
    shape: Shape = RoundedCornerShape(8.dp),
    color: Color = Color.White,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(color)
    )
}