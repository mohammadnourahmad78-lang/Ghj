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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CalculationMode
import com.example.model.DEFAULT_SUBJECTS
import com.example.model.ExamCalculationResult
import com.example.ui.theme.CairoFontFamily

@Composable
fun SubjectsSection(
    isExpanded: Boolean,
    onToggleExpanded: () -> Unit,
    scores: Map<String, String>,
    onScoreChanged: (String, String) -> Unit,
    calculationResult: ExamCalculationResult,
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
        // Accordion header pill: علامات المواد
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
                .clickable { onToggleExpanded() }
                .padding(horizontal = 20.dp, vertical = 14.dp)
                .testTag("subjects_accordion_header"),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "طي المواد" else "فتح المواد",
                    tint = textColor,
                    modifier = Modifier.size(24.dp)
                )

                Text(
                    text = "علامات المواد",
                    fontFamily = CairoFontFamily,
                    color = textColor,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End
                )
            }
        }

        // Subjects Table Container
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn(tween(250)) + expandVertically(tween(300)),
            exit = fadeOut(tween(200)) + shrinkVertically(tween(250))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .border(
                        width = 2.dp,
                        color = pillBorderColor,
                        shape = RoundedCornerShape(22.dp)
                    )
                    .background(pillBg)
                    .padding(14.dp)
                    .testTag("subjects_table_container")
            ) {
                // Table of Subjects
                DEFAULT_SUBJECTS.forEachIndexed { index, subject ->
                    val isLowerLang = (subject.id == calculationResult.lowerLanguageId) &&
                            (calculationResult.currentMode == CalculationMode.DROP_LOWER_LANGUAGE)

                    SubjectRowItem(
                        subject = subject,
                        score = scores[subject.id] ?: "",
                        onScoreChanged = { newScore -> onScoreChanged(subject.id, newScore) },
                        onSetFullScore = { onScoreChanged(subject.id, subject.maxScore.toString()) },
                        isLowerLanguage = isLowerLang,
                        isHighest = (subject.id == calculationResult.highestScoreSubjectId),
                        isLowest = (subject.id == calculationResult.lowestScoreSubjectId),
                        percentage = calculationResult.subjectPercentages[subject.id] ?: 0f,
                        isDarkMode = isDarkMode
                    )

                    if (index < DEFAULT_SUBJECTS.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = if (isDarkMode) Color.White.copy(alpha = 0.25f) else Color.Black.copy(alpha = 0.2f),
                            thickness = 1.dp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Footer Box: مجموع المواد
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            width = 2.dp,
                            color = pillBorderColor,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .background(if (isDarkMode) Color(0xFF141414) else Color(0xFFFAFAFC))
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                        .testTag("total_raw_score_box")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = calculationResult.totalRawScore.toString(),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = textColor
                        )

                        Text(
                            text = "مجموع المواد",
                            fontFamily = CairoFontFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    }
                }
            }
        }
    }
}
