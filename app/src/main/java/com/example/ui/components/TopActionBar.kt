package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CairoFontFamily

@Composable
fun TopActionBar(
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    onClearAll: () -> Unit,
    onCopy: () -> Unit,
    onOpenResultStatus: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isMenuOpen by remember { mutableStateOf(false) }

    val textColor = if (isDarkMode) Color.White else Color(0xFF111111)
    val menuBg = if (isDarkMode) Color(0xFF252528) else Color.White

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Actions: Dark mode toggle + 3-dots overflow menu
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Dark Mode toggle button
            IconButton(
                onClick = onToggleDarkMode,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        1.dp,
                        if (isDarkMode) Color(0xFF444444) else Color(0xFFDDDDDF),
                        RoundedCornerShape(12.dp)
                    )
                    .testTag("dark_mode_toggle")
            ) {
                Icon(
                    imageVector = if (isDarkMode) Icons.Default.Brightness7 else Icons.Default.Brightness4,
                    contentDescription = if (isDarkMode) "تفعيل المظهر النهاري" else "تفعيل المظهر الليلي",
                    tint = textColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            // 3-dots menu container
            Box {
                IconButton(
                    onClick = { isMenuOpen = true },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            1.dp,
                            if (isDarkMode) Color(0xFF444444) else Color(0xFFDDDDDF),
                            RoundedCornerShape(12.dp)
                        )
                        .testTag("overflow_menu_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "المزيد من الخيارات",
                        tint = textColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                DropdownMenu(
                    expanded = isMenuOpen,
                    onDismissRequest = { isMenuOpen = false },
                    modifier = Modifier
                        .background(menuBg)
                        .border(
                            1.dp,
                            if (isDarkMode) Color(0xFF444444) else Color(0xFFE2E2E6),
                            RoundedCornerShape(14.dp)
                        )
                ) {
                    // Option 1: معرفة النتيجة
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Visibility,
                                    contentDescription = null,
                                    tint = textColor,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "معرفة النتيجة",
                                    fontFamily = CairoFontFamily,
                                    color = textColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        },
                        onClick = {
                            isMenuOpen = false
                            onOpenResultStatus()
                        },
                        modifier = Modifier.testTag("menu_result_status")
                    )

                    // Option 2: نسخ التقرير للحافظة
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = null,
                                    tint = textColor,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "نسخ التقرير للحافظة",
                                    fontFamily = CairoFontFamily,
                                    color = textColor,
                                    fontSize = 14.sp
                                )
                            }
                        },
                        onClick = {
                            isMenuOpen = false
                            onCopy()
                        },
                        modifier = Modifier.testTag("menu_copy")
                    )

                    HorizontalDivider(color = if (isDarkMode) Color(0xFF383838) else Color(0xFFEEEEEE))

                    // Option 3: مسح جميع الدرجات
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.DeleteSweep,
                                    contentDescription = null,
                                    tint = Color(0xFFC62828),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "مسح جميع الدرجات",
                                    fontFamily = CairoFontFamily,
                                    color = Color(0xFFC62828),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        },
                        onClick = {
                            isMenuOpen = false
                            onClearAll()
                        },
                        modifier = Modifier.testTag("menu_clear_all")
                    )
                }
            }
        }

        // Right side: Clean space (عبارة حاسبة المعدل أو النتائج شهادة التعليم الأساسي التاسع حُذفت كما طُلب)
        Spacer(modifier = Modifier.width(1.dp))
    }
}
