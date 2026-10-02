package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SubjectItem
import java.util.Locale

@Composable
fun SubjectRowItem(
    subject: SubjectItem,
    score: String,
    onScoreChanged: (String) -> Unit,
    onSetFullScore: () -> Unit,
    isLowerLanguage: Boolean,
    isHighest: Boolean,
    isLowest: Boolean,
    percentage: Float,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val parsedScore = score.toIntOrNull() ?: 0
    val isOverMax = parsedScore > subject.maxScore

    // Color definitions matching the screenshot
    val goldBg = if (isDarkMode) Color(0xFF2E2611) else Color(0xFFFFF9E6)
    val defaultBg = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val targetBg = if (isLowerLanguage) goldBg else defaultBg

    val rowBg by animateColorAsState(targetValue = targetBg, animationSpec = tween(300), label = "rowBg")

    val textColor = if (isDarkMode) Color.White else Color.Black
    val secondaryText = if (isDarkMode) Color(0xFFAAAAAA) else Color(0xFF555555)
    val dashedBorderColor = if (isDarkMode) Color.White else Color.Black
    val inputBorderColor = if (isOverMax) {
        Color(0xFFB00020)
    } else if (isDarkMode) {
        Color.White
    } else {
        Color.Black
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(rowBg)
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .testTag("subject_row_${subject.id}")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header row: Subject Name + Badges (تُحذف / أقوى مادة / أضعف مادة / النسبة)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left chips: percentage + highest/lowest indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (score.isNotBlank()) {
                        Text(
                            text = String.format(Locale.US, "%.0f%%", percentage),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (percentage >= 80) Color(0xFF2E7D32) else if (percentage < 50) Color(0xFFC62828) else secondaryText,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    if (percentage >= 80) Color(0xFFE8F5E9)
                                    else if (percentage < 50) Color(0xFFFFEBEE)
                                    else if (isDarkMode) Color(0xFF2A2A2A) else Color(0xFFF0F0F2)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    if (isHighest) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFE8F5E9))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowUpward,
                                contentDescription = "أعلى درجة",
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "الأعلى",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                        }
                    } else if (isLowest) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFFFEBEE))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDownward,
                                contentDescription = "أدنى درجة",
                                tint = Color(0xFFC62828),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "الأدنى",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFC62828)
                            )
                        }
                    }
                }

                // Right title + "تُحذف" badge if lower language
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (isLowerLanguage) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFB58000))
                                .padding(horizontal = 10.dp, vertical = 2.dp)
                                .testTag("delete_badge_${subject.id}")
                        ) {
                            Text(
                                text = "تُحذف",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Text(
                        text = subject.nameAr,
                        style = TextStyle(
                            fontFamily = com.example.ui.theme.CairoFontFamily,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = textColor
                        ),
                        textAlign = TextAlign.End
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Score boxes row: Left is Max Score (dashed border), Right is Input field (solid border)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Max score box (Dashed border, tap to set full mark)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .dashedBorder(
                            strokeWidth = 1.5.dp,
                            color = dashedBorderColor,
                            cornerRadius = 14.dp,
                            dashLength = 6.dp,
                            gapLength = 3.dp
                        )
                        .clickable { onSetFullScore() }
                        .padding(horizontal = 8.dp)
                        .testTag("max_score_btn_${subject.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = subject.maxScore.toString(),
                        style = TextStyle(
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = FontFamily.Monospace,
                            color = textColor
                        ),
                        textAlign = TextAlign.Center
                    )
                }

                // Input score box (Solid border, exactly like screenshot)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .border(
                            width = if (isOverMax) 2.dp else 1.5.dp,
                            color = inputBorderColor,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .background(
                            if (isDarkMode) Color(0xFF252525) else Color.White,
                            RoundedCornerShape(14.dp)
                        )
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    BasicTextField(
                        value = score,
                        onValueChange = onScoreChanged,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("score_input_${subject.id}"),
                        textStyle = TextStyle(
                            fontSize = 19.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Monospace,
                            color = if (isOverMax) Color(0xFFB00020) else textColor,
                            textAlign = TextAlign.Center
                        ),
                        singleLine = true,
                        cursorBrush = SolidColor(if (isDarkMode) Color.White else Color.Black),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                if (score.isEmpty()) {
                                    Text(
                                        text = "0",
                                        style = TextStyle(
                                            fontSize = 19.sp,
                                            fontWeight = FontWeight.Normal,
                                            fontFamily = FontFamily.Monospace,
                                            color = if (isDarkMode) Color(0xFF666666) else Color(0xFFAAAAAA),
                                            textAlign = TextAlign.Center
                                        )
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                }
            }

            // Warning note if user enters more than maximum mark
            if (isOverMax) {
                Text(
                    text = "العلامة المدخلة أكبر من الدرجة العظمى (${subject.maxScore})",
                    color = Color(0xFFB00020),
                    fontSize = 11.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, end = 4.dp)
                )
            }
        }
    }
}
