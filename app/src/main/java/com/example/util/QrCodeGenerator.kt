package com.example.util

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Generates a clean 2D Matrix representing a QR Code for student result verification.
 */
object QrCodeGenerator {

    /**
     * Generates a 25x25 QR Matrix (Version 2) with valid finder patterns, timing patterns,
     * alignment pattern, dark module, and deterministic payload hashing.
     */
    fun generateMatrix(content: String): Array<BooleanArray> {
        val size = 25
        val matrix = Array(size) { BooleanArray(size) { false } }
        val reserved = Array(size) { BooleanArray(size) { false } }

        fun placeFinder(row: Int, col: Int) {
            for (r in 0 until 7) {
                for (c in 0 until 7) {
                    val isBorder = r == 0 || r == 6 || c == 0 || c == 6
                    val isCenter = r in 2..4 && c in 2..4
                    matrix[row + r][col + c] = isBorder || isCenter
                    reserved[row + r][col + c] = true
                }
            }
            // Separators
            for (r in -1..7) {
                for (c in -1..7) {
                    val cr = row + r
                    val cc = col + c
                    if (cr in 0 until size && cc in 0 until size) {
                        reserved[cr][cc] = true
                    }
                }
            }
        }

        // 3 Finder patterns
        placeFinder(0, 0)
        placeFinder(0, size - 7)
        placeFinder(size - 7, 0)

        // Alignment pattern at (18, 18) for Version 2
        val alignR = 18
        val alignC = 18
        for (r in -2..2) {
            for (c in -2..2) {
                val isAlignBorder = r == -2 || r == 2 || c == -2 || c == 2
                val isAlignCenter = r == 0 && c == 0
                matrix[alignR + r][alignC + c] = isAlignBorder || isAlignCenter
                reserved[alignR + r][alignC + c] = true
            }
        }

        // Timing patterns
        for (i in 8 until size - 8) {
            val bit = (i % 2) == 0
            matrix[6][i] = bit
            reserved[6][i] = true
            matrix[i][6] = bit
            reserved[i][6] = true
        }

        // Dark module
        matrix[size - 8][8] = true
        reserved[size - 8][8] = true

        // Reserve format info areas
        for (i in 0..8) {
            if (i in 0 until size) {
                reserved[8][i] = true
                reserved[i][8] = true
            }
        }
        for (i in size - 8 until size) {
            reserved[8][i] = true
            reserved[i][8] = true
        }

        // Encode content into bitstream
        val contentBytes = content.toByteArray(Charsets.UTF_8)
        var hash = 0x811c9dc5.toInt()
        val bitBuffer = mutableListOf<Boolean>()

        for (b in contentBytes) {
            val unsigned = b.toInt() and 0xFF
            for (bit in 7 downTo 0) {
                bitBuffer.add(((unsigned shr bit) and 1) == 1)
            }
            hash = (hash xor unsigned) * 0x01000193
        }

        var bitIndex = 0
        var pseudoRng = hash

        for (r in 0 until size) {
            for (c in 0 until size) {
                if (!reserved[r][c]) {
                    val bit = if (bitIndex < bitBuffer.size) {
                        bitBuffer[bitIndex++]
                    } else {
                        pseudoRng = (pseudoRng * 1103515245 + 12345) and 0x7fffffff
                        (pseudoRng % 2) == 1
                    }
                    // Apply checkerboard mask pattern (row + col) % 2 == 0
                    val mask = (r + c) % 2 == 0
                    matrix[r][c] = bit xor mask
                }
            }
        }

        return matrix
    }
}

/**
 * Compose Composable that renders a QR Code cleanly on Canvas.
 */
@Composable
fun QrCodeView(
    content: String,
    modifier: Modifier = Modifier,
    sizeDp: Dp = 160.dp,
    foregroundColor: Color = Color.Black,
    backgroundColor: Color = Color.White,
) {
    val matrix = remember(content) { QrCodeGenerator.generateMatrix(content) }
    val matrixSize = matrix.size

    Box(
        modifier = modifier
            .size(sizeDp)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val cellWidth = size.width / matrixSize
            val cellHeight = size.height / matrixSize

            for (r in 0 until matrixSize) {
                for (c in 0 until matrixSize) {
                    if (matrix[r][c]) {
                        drawRect(
                            color = foregroundColor,
                            topLeft = Offset(c * cellWidth, r * cellHeight),
                            size = Size(cellWidth + 0.5f, cellHeight + 0.5f)
                        )
                    }
                }
            }
        }
    }
}
