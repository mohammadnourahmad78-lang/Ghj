package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CairoFontFamily
import java.util.Locale

@Composable
fun SummaryCards(
    differentialScore: Int,
    maxScore: Int,
    percentage: Float,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    val boxBg = if (isDarkMode) Color.Black else Color.White
    val boxBorderColor = if (isDarkMode) Color.White else Color.Black
    val textColor = if (isDarkMode) Color.White else Color.Black

    // Animated fast & smooth count-up for percentage
    val animatedPercentage = remember { Animatable(0f) }
    LaunchedEffect(percentage) {
        animatedPercentage.animateTo(
            targetValue = percentage,
            animationSpec = tween(durationMillis = 450, easing = FastOutSlowInEasing)
        )
    }

    // Gentle pulse animation whenever percentage updates
    val pulseScale = remember { Animatable(1f) }
    LaunchedEffect(percentage) {
        if (percentage > 0f) {
            pulseScale.animateTo(
                targetValue = 1.10f,
                animationSpec = tween(durationMillis = 130, easing = FastOutSlowInEasing)
            )
            pulseScale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Two Boxes side-by-side
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Left Box: الدرجة العظمى (White with black border in light mode, Black with white border in dark mode)
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "الدرجة العظمى",
                    fontFamily = CairoFontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            width = 2.dp,
                            color = boxBorderColor,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .background(boxBg)
                        .testTag("max_score_box"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = maxScore.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = textColor
                    )
                }
            }

            // Right Box: المجموع التفاضلي (White with black border in light mode, Black with white border in dark mode)
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "المجموع التفاضلي",
                    fontFamily = CairoFontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            width = 2.dp,
                            color = boxBorderColor,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .background(boxBg)
                        .testTag("diff_score_box"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = differentialScore.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = textColor
                    )
                }
            }
        }

        // Main Result Box: معدّلك (White with Black rounded border in light mode, Black with White rounded border in dark mode)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .border(
                    width = 2.dp,
                    color = boxBorderColor,
                    shape = RoundedCornerShape(22.dp)
                )
                .background(boxBg)
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .testTag("differential_result_card")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Circular Ring + Percentage text with gentle pulse scale
                Row(
                    modifier = Modifier.scale(pulseScale.value),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PercentageCircularRing(
                        percentage = animatedPercentage.value,
                        size = 46.dp,
                        strokeWidth = 5.dp,
                        ringColor = textColor,
                        trackColor = if (isDarkMode) Color(0xFF333333) else Color(0xFFE2E2E6)
                    )

                    Text(
                        text = String.format(Locale.US, "%.2f %%", animatedPercentage.value),
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = textColor,
                        textAlign = TextAlign.Start
                    )
                }

                // Right: "معدّلك"
                Text(
                    text = "معدّلك",
                    fontFamily = CairoFontFamily,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}
