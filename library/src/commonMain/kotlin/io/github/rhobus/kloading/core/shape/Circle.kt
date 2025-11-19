package io.github.rhobus.kloading.core.shape

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A simple circular shape with customizable size and color.
 *
 * @param size The diameter of the circle. Defaults to 8.dp.
 * @param color The fill color of the circle. Defaults to [Color.White].
 * @param modifier Optional [Modifier] for additional layout or styling.
 */

@Composable
fun Circle(
    size: Dp = 8.dp,
    color: Color = Color.White,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .background(color, CircleShape)
    )
}