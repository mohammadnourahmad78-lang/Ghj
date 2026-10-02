package com.example.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Custom modifier that draws a dashed rounded rectangle border, matching the
 * exact dashed outline style from the screenshots for max scores (e.g. 600, 2800).
 */
fun Modifier.dashedBorder(
    strokeWidth: Dp = 1.5.dp,
    color: Color,
    cornerRadius: Dp = 16.dp,
    dashLength: Dp = 6.dp,
    gapLength: Dp = 4.dp
): Modifier = drawWithCache {
    val strokeWidthPx = strokeWidth.toPx()
    val halfStroke = strokeWidthPx / 2f
    val cornerRadiusPx = cornerRadius.toPx()
    val dashEffect = PathEffect.dashPathEffect(
        floatArrayOf(dashLength.toPx(), gapLength.toPx()),
        0f
    )
    val stroke = Stroke(
        width = strokeWidthPx,
        pathEffect = dashEffect
    )

    onDrawWithContent {
        drawContent()
        drawRoundRect(
            color = color,
            topLeft = androidx.compose.ui.geometry.Offset(halfStroke, halfStroke),
            size = androidx.compose.ui.geometry.Size(
                size.width - strokeWidthPx,
                size.height - strokeWidthPx
            ),
            cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
            style = stroke
        )
    }
}
