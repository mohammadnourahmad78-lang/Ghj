package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.CalculationMode
import com.example.model.DEFAULT_SUBJECTS
import com.example.model.ExamCalculationResult
import com.example.ui.theme.CairoFontFamily
import com.example.util.QrCodeView
import java.util.Locale

@Composable
fun ResultCardDialog(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    calculationResult: ExamCalculationResult,
    scores: Map<String, String>,
    studentName: String,
    seatNumber: String,
    onSaveStudentInfo: (String, String) -> Unit,
    onShare: () -> Unit,
    isDarkMode: Boolean
) {
    if (!isOpen) return

    var currentName by remember(studentName) { mutableStateOf(studentName) }
    var currentSeat by remember(seatNumber) { mutableStateOf(seatNumber) }
    var isLandscapeView by remember { mutableStateOf(false) }

    val qrPayload = remember(calculationResult, currentName, currentSeat) {
        "SY_EXAM|NAME=${currentName.ifEmpty { "طالب" }}|SEAT=${currentSeat.ifEmpty { "0" }}|" +
                "MODE=${calculationResult.currentMode.name}|SCORE=${calculationResult.differentialScore}|" +
                "MAX=${calculationResult.differentialMaxScore}|PCT=%.2f%%".format(Locale.US, calculationResult.differentialPercentage)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(if (isLandscapeView) 0.98f else 0.95f)
                .clip(RoundedCornerShape(24.dp))
                .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(24.dp)
                )
                .testTag("result_card_dialog"),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Top control bar: Close & Rotate view toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "إغلاق",
                            tint = Color.Black
                        )
                    }

                    // Indicator / Landscape Toggle
                    Button(
                        onClick = { isLandscapeView = !isLandscapeView },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ScreenRotation,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isLandscapeView) "عرض طولي عادي" else "عرض أفقي (مستطيل الشهادة)",
                            fontFamily = CairoFontFamily,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // The Horizontal / Landscape Card Container
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                ) {
                    Column(
                        modifier = Modifier
                            .width(if (isLandscapeView) 740.dp else 500.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .border(2.5.dp, Color.Black, RoundedCornerShape(20.dp))
                            .background(Color.White)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Official Document Header: Black & White with rounded container
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.5.dp, Color.Black, RoundedCornerShape(16.dp))
                                .background(Color(0xFFFAFAFA))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // QR Code in corner with rounded frame
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(1.5.dp, Color.Black, RoundedCornerShape(12.dp))
                                    .background(Color.White)
                                    .padding(6.dp)
                            ) {
                                QrCodeView(
                                    content = qrPayload,
                                    sizeDp = 64.dp,
                                    foregroundColor = Color.Black,
                                    backgroundColor = Color.White
                                )
                                Text(
                                    text = "التحقق الرقمي",
                                    fontFamily = CairoFontFamily,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }

                            // Center Official Titles
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "الجمهورية العربية السورية",
                                    fontFamily = CairoFontFamily,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Text(
                                    text = "وزارة التربية - مديرية الامتحانات",
                                    fontFamily = CairoFontFamily,
                                    fontSize = 12.sp,
                                    color = Color(0xFF333333)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color.Black)
                                        .padding(horizontal = 14.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "كشف درجات شهادة التعليم الأساسي",
                                        fontFamily = CairoFontFamily,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }

                            // Right Details block
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "الدورة الامتحانية",
                                    fontFamily = CairoFontFamily,
                                    fontSize = 10.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "العام الدراسي 2026",
                                    fontFamily = CairoFontFamily,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Text(
                                    text = calculationResult.currentMode.titleAr,
                                    fontFamily = CairoFontFamily,
                                    fontSize = 10.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Student Information Row: Rounded Rectangle with crisp contrast
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .border(1.5.dp, Color.Black, RoundedCornerShape(14.dp))
                                .background(Color(0xFFF4F4F6))
                                .padding(horizontal = 14.dp, vertical = 9.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "رقم الاكتتاب: ${currentSeat.ifEmpty { "غير محدد" }}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = Color.Black
                            )
                            Text(
                                text = "اسم الطالب: ${currentName.ifEmpty { "طالب نظامي" }}",
                                fontFamily = CairoFontFamily,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Official Marks Table inside smooth rounded container with black & white contrast
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .border(2.dp, Color.Black, RoundedCornerShape(16.dp))
                        ) {
                            // Table Header Row (Solid Black, White text)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.Black)
                                    .padding(vertical = 9.dp, horizontal = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "الحالة / النسبة",
                                    fontFamily = CairoFontFamily,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.weight(1.3f),
                                    textAlign = TextAlign.Start
                                )
                                Text(
                                    text = "العلامة المحصلة",
                                    fontFamily = CairoFontFamily,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "الدرجة العظمى",
                                    fontFamily = CairoFontFamily,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "المادة الدراسية",
                                    fontFamily = CairoFontFamily,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.weight(1.4f),
                                    textAlign = TextAlign.End
                                )
                            }

                            // Table Content Rows with solid dividers and rounded aesthetics
                            DEFAULT_SUBJECTS.forEachIndexed { index, sub ->
                                val scoreVal = scores[sub.id]?.toIntOrNull() ?: 0
                                val pct = calculationResult.subjectPercentages[sub.id] ?: 0f
                                val isDropped = (sub.id == calculationResult.lowerLanguageId) &&
                                        (calculationResult.currentMode == CalculationMode.DROP_LOWER_LANGUAGE)

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(if (isDropped) Color(0xFFF4F4F4) else Color.White)
                                        .padding(vertical = 8.dp, horizontal = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Status / Percentage
                                    Text(
                                        text = if (isDropped) "[تُحذف - الأدنى]" else String.format(Locale.US, "%.1f%%", pct),
                                        fontFamily = if (isDropped) CairoFontFamily else FontFamily.Monospace,
                                        fontSize = 11.sp,
                                        fontWeight = if (isDropped) FontWeight.Bold else FontWeight.Normal,
                                        color = Color.Black,
                                        modifier = Modifier.weight(1.3f),
                                        textAlign = TextAlign.Start
                                    )

                                    // Score
                                    Text(
                                        text = scoreVal.toString(),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        color = Color.Black,
                                        modifier = Modifier.weight(1f),
                                        textAlign = TextAlign.Center
                                    )

                                    // Max score
                                    Text(
                                        text = sub.maxScore.toString(),
                                        fontSize = 13.sp,
                                        fontFamily = FontFamily.Monospace,
                                        color = Color(0xFF444444),
                                        modifier = Modifier.weight(1f),
                                        textAlign = TextAlign.Center
                                    )

                                    // Name
                                    Text(
                                        text = sub.nameAr,
                                        fontFamily = CairoFontFamily,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black,
                                        modifier = Modifier.weight(1.4f),
                                        textAlign = TextAlign.End
                                    )
                                }

                                if (index < DEFAULT_SUBJECTS.size - 1) {
                                    HorizontalDivider(color = Color.Black, thickness = 1.dp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Results Summary Box (Black rounded table with white text)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.5.dp, Color.Black, RoundedCornerShape(16.dp))
                                .background(Color.Black)
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Left: Percentage
                            Column(horizontalAlignment = Alignment.Start) {
                                Text(
                                    text = "المعدل",
                                    fontFamily = CairoFontFamily,
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = String.format(Locale.US, "%.2f %%", calculationResult.differentialPercentage),
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color.White
                                )
                            }

                            // Center: Total Raw
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "المجموع العام",
                                    fontFamily = CairoFontFamily,
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = "${calculationResult.totalRawScore} / 3200",
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color.White
                                )
                            }

                            // Right: Differential Score
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "المجموع التفاضلي",
                                    fontFamily = CairoFontFamily,
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = "${calculationResult.differentialScore} / ${calculationResult.differentialMaxScore}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Optional inputs for Name & Seat number
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = currentSeat,
                        onValueChange = {
                            currentSeat = it
                            onSaveStudentInfo(currentName, it)
                        },
                        label = { Text("تعديل رقم الاكتتاب", fontFamily = CairoFontFamily, fontSize = 11.sp) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = currentName,
                        onValueChange = {
                            currentName = it
                            onSaveStudentInfo(it, currentSeat)
                        },
                        label = { Text("تعديل اسم الطالب", fontFamily = CairoFontFamily, fontSize = 11.sp) },
                        modifier = Modifier.weight(1.3f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action buttons: Share & Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEEEEEE),
                            contentColor = Color.Black
                        )
                    ) {
                        Text("إغلاق", fontFamily = CairoFontFamily, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onShare,
                        modifier = Modifier
                            .weight(1.3f)
                            .height(46.dp)
                            .testTag("share_card_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "مشاركة",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("مشاركة البطاقة كـ نص", fontFamily = CairoFontFamily, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
