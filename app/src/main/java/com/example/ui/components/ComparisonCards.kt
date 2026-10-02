package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CalculationMode
import java.util.Locale

@Composable
fun ComparisonCards(
    currentMode: CalculationMode,
    droppedScore: Int,
    droppedPercentage: Float,
    fullScore: Int,
    fullPercentage: Float,
    onSelectMode: (CalculationMode) -> Unit,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Card 1: بحذف اللغة الأقل
        SingleComparisonCard(
            title = "بحذف اللغة الأقل",
            scoreText = "$droppedScore / 2800",
            percentage = droppedPercentage,
            isActive = currentMode == CalculationMode.DROP_LOWER_LANGUAGE,
            isDarkMode = isDarkMode,
            onClick = { onSelectMode(CalculationMode.DROP_LOWER_LANGUAGE) },
            testTag = "comparison_card_drop",
            modifier = Modifier.weight(1f)
        )

        // Card 2: مع اللغتين معاً
        SingleComparisonCard(
            title = "مع اللغتين معاً",
            scoreText = "$fullScore / 3200",
            percentage = fullPercentage,
            isActive = currentMode == CalculationMode.INCLUDE_BOTH_LANGUAGES,
            isDarkMode = isDarkMode,
            onClick = { onSelectMode(CalculationMode.INCLUDE_BOTH_LANGUAGES) },
            testTag = "comparison_card_full",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SingleComparisonCard(
    title: String,
    scoreText: String,
    percentage: Float,
    isActive: Boolean,
    isDarkMode: Boolean,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isActive) {
        if (isDarkMode) Color.White else Color(0xFF111111)
    } else {
        if (isDarkMode) Color(0xFF3A3A3C) else Color(0xFFE2E2E6)
    }

    val cardBg = if (isActive) {
        if (isDarkMode) Color(0xFF242426) else Color(0xFFF7F7FA)
    } else {
        if (isDarkMode) Color(0xFF1A1A1C) else Color.White
    }

    val titleColor = if (isDarkMode) Color.White else Color(0xFF111111)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .border(
                width = if (isActive) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(18.dp)
            )
            .background(cardBg)
            .clickable { onClick() }
            .padding(14.dp)
            .testTag(testTag)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mode active badge
            if (isActive) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isDarkMode) Color.White else Color(0xFF111111))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "الوضع المختار",
                        color = if (isDarkMode) Color.Black else Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            } else {
                Spacer(modifier = Modifier.height(18.dp))
            }

            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = titleColor,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = String.format(Locale.US, "%.2f%%", percentage),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = titleColor,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = scoreText,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                color = if (isDarkMode) Color(0xFFAAAAAA) else Color(0xFF666666),
                textAlign = TextAlign.Center
            )
        }
    }
}
