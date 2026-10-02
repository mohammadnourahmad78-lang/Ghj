package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CalculationMode
import com.example.ui.theme.CairoFontFamily

@Composable
fun ModeSelector(
    currentMode: CalculationMode,
    isOpen: Boolean,
    onToggle: () -> Unit,
    onSelectMode: (CalculationMode) -> Unit,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    val pillBg = if (isDarkMode) Color.Black else Color.White
    val pillBorderColor = if (isDarkMode) Color.White else Color.Black
    val textColor = if (isDarkMode) Color.White else Color.Black

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Main pill button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .border(
                    width = 2.dp,
                    color = pillBorderColor,
                    shape = RoundedCornerShape(22.dp)
                )
                .background(pillBg)
                .clickable { onToggle() }
                .padding(horizontal = 20.dp, vertical = 14.dp)
                .testTag("mode_selector_button"),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Arrow icon (Up when open, Down when closed)
                Icon(
                    imageVector = if (isOpen) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isOpen) "إغلاق الخيارات" else "فتح الخيارات",
                    tint = textColor,
                    modifier = Modifier.size(24.dp)
                )

                // Right Arabic text title
                Text(
                    text = currentMode.titleAr,
                    fontFamily = CairoFontFamily,
                    color = textColor,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End
                )
            }
        }

        // Dropdown menu options
        AnimatedVisibility(
            visible = isOpen,
            enter = fadeIn(tween(200)) + expandVertically(tween(250)),
            exit = fadeOut(tween(150)) + shrinkVertically(tween(200))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(
                        width = 2.dp,
                        color = pillBorderColor,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .background(pillBg)
                    .testTag("mode_dropdown_container")
            ) {
                // Option 1: حذف اللغة الأقل
                ModeOptionRow(
                    title = CalculationMode.DROP_LOWER_LANGUAGE.titleAr,
                    subtitle = "الدرجة العظمى 2800 (تُحذف أدنى علامة بين الإنكليزي والفرنسي)",
                    isSelected = currentMode == CalculationMode.DROP_LOWER_LANGUAGE,
                    isDarkMode = isDarkMode,
                    onClick = { onSelectMode(CalculationMode.DROP_LOWER_LANGUAGE) },
                    testTag = "mode_option_drop"
                )

                HorizontalDivider(
                    color = if (isDarkMode) Color.White.copy(alpha = 0.3f) else Color.Black.copy(alpha = 0.2f),
                    thickness = 1.dp
                )

                // Option 2: حساب اللغتين معاً
                ModeOptionRow(
                    title = CalculationMode.INCLUDE_BOTH_LANGUAGES.titleAr,
                    subtitle = "الدرجة العظمى 3200 (تُحسب علامات اللغتين الإنكليزية والفرنسية معاً)",
                    isSelected = currentMode == CalculationMode.INCLUDE_BOTH_LANGUAGES,
                    isDarkMode = isDarkMode,
                    onClick = { onSelectMode(CalculationMode.INCLUDE_BOTH_LANGUAGES) },
                    testTag = "mode_option_full"
                )
            }
        }
    }
}

@Composable
private fun ModeOptionRow(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    isDarkMode: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val textColor = if (isDarkMode) Color.White else Color.Black
    val subtextColor = if (isDarkMode) Color(0xFFAAAAAA) else Color(0xFF555555)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .testTag(testTag),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Selection radio/check indicator
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = if (isSelected) textColor else (if (isDarkMode) Color(0xFF666666) else Color(0xFFCCCCCC)),
                    shape = CircleShape
                )
                .background(if (isSelected) textColor else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = if (isDarkMode) Color.Black else Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Texts
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.weight(1f).padding(start = 12.dp)
        ) {
            Text(
                text = title,
                fontFamily = CairoFontFamily,
                color = textColor,
                fontSize = 15.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                textAlign = TextAlign.End
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontFamily = CairoFontFamily,
                color = subtextColor,
                fontSize = 12.sp,
                textAlign = TextAlign.End
            )
        }
    }
}
