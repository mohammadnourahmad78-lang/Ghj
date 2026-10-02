package com.example.ui

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CairoFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val ALL_SYRIAN_GOVERNORATES = listOf(
    "دمشق",
    "ريف دمشق",
    "حلب",
    "حمص",
    "حماة",
    "اللاذقية",
    "طرطوس",
    "إدلب",
    "درعا",
    "السويداء",
    "القنيطرة",
    "دير الزور",
    "الحسكة",
    "الرقة"
)

val ALL_CERTIFICATES = listOf(
    "التعليم الأساسي - عام - منهاج دمشق",
    "التعليم الأساسي - عام - منهاج إدلب",
    "التعليم الأساسي - عام - مجالس",
    "التعليم الاساسي - شرعي - منهاج مؤقتة",
    "التعليم الأساسي - شرعي - منهاج دمشق",
    "التعليم الأساسي - شرعي - منهاج إدلب",
    "التعليم الأساسي - شرعي - مجالس",
    "الثانوي - علمي - منهاج دمشق",
    "الثانوي - علمي - منهاج إدلب",
    "الثانوي - علمي - مجالس",
    "الثانوي - أدبي - منهاج دمشق",
    "الثانوي - أدبي - منهاج إدلب",
    "الثانوي - أدبي - مجالس",
    "الثانوي - شريعة أدبي - منهاج دمشق",
    "الثانوي - شريعة ادبي - منهاج مؤقتة",
    "ثانوي - تجارية - منهاج حديث",
    "ثانوي - صناعة ألبسة مزدوج - منهاج حديث",
    "ثانوي - خياطة الملابس - منهاج حديث",
    "ثانوي - صيانة أجهزة طبية - منهاج دمشق حديث",
    "ثانوي - ميكاترونكس - منهاج دمشق حديث",
    "ثانوي - ميكانيك وكهرباء المركبات - منهاج دمشق حديث",
    "ثانوي - نجارة الأثاث والزخرفة - منهاج حديث",
    "ثانوي - تصنيع ميكانيكي - منهاج دمشق حديث",
    "ثانوي - تصنيع ميكانيكي مزدوج - منهاج حديث",
    "ثانوي - النماذج والسباكة - منهاج حديث",
    "ثانوي - النسيج - منهاج حديث",
    "ثانوي - التكييف والتبريد - منهاج حديث",
    "ثانوي - الحلاقة والتجميل - منهاج حديث",
    "ثانوي - اللحام وتشكيل المعادن - منهاج حديث",
    "ثانوي - الاتصالات - منهاج دمشق حديث",
    "ثانوي - التدفئة والتمديدات - منهاج حديث",
    "ثانوي - التقنيات الإلكترونية - منهاج دمشق حديث",
    "ثانوي - التقنيات الكهربائية - منهاج دمشق حديث",
    "ثانوي - تقنيات حاسوب - منهاج دمشق حديث"
)

@Composable
fun ResultsQueryScreen(
    viewModel: ExamViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var selectedYear by remember { mutableStateOf("2026 - 2025") }
    var isYearOpen by remember { mutableStateOf(false) }

    var selectedGov by remember { mutableStateOf("حلب") }
    var isGovOpen by remember { mutableStateOf(false) }
    var govSearchQuery by remember { mutableStateOf("") }

    var selectedCert by remember { mutableStateOf("التعليم الأساسي - عام - منهاج دمشق") }
    var isCertOpen by remember { mutableStateOf(false) }
    var certSearchQuery by remember { mutableStateOf("") }

    var seatNumber by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }

    var showResultDialog by remember { mutableStateOf(false) }
    var resultDialogMessage by remember { mutableStateOf("") }
    var showTopStudentsDialog by remember { mutableStateOf(false) }

    // Color system adhering strictly to the user prompt:
    // In light mode: Pure White background, Solid Black rounded borders (2.dp), Black text.
    // In dark mode: Pure Black background, Solid White rounded borders (2.dp), White text.
    val screenBg = if (isDarkMode) Color.Black else Color.White
    val cardBg = if (isDarkMode) Color.Black else Color.White
    val borderCol = if (isDarkMode) Color.White else Color.Black
    val textColor = if (isDarkMode) Color.White else Color.Black

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .background(screenBg)
                .windowInsetsPadding(WindowInsets.safeDrawing),
            containerColor = screenBg
        ) { innerPadding ->
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .widthIn(max = 560.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Top App Header Banner: White with black border in light mode, Black with white border in dark mode
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                            .border(
                                width = 2.dp,
                                color = borderCol,
                                shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                            )
                            .background(cardBg)
                            .padding(horizontal = 20.dp, vertical = 18.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            // Top bar icons: Back and Dark Mode
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = onBack,
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .border(1.5.dp, borderCol, RoundedCornerShape(12.dp))
                                        .background(cardBg)
                                        .testTag("results_back_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "الرجوع للحاسبة",
                                        tint = textColor
                                    )
                                }

                                IconButton(
                                    onClick = { viewModel.toggleDarkMode() },
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .border(1.5.dp, borderCol, RoundedCornerShape(12.dp))
                                        .background(cardBg)
                                ) {
                                    Icon(
                                        imageVector = if (isDarkMode) Icons.Default.Brightness7 else Icons.Default.Brightness4,
                                        contentDescription = "تبديل المظهر",
                                        tint = textColor
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Main titles
                            Text(
                                text = "الاستعلام عن النتائج",
                                fontFamily = CairoFontFamily,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "أدخل البيانات المطلوبة للبحث عن النتيجة الامتحانية",
                                fontFamily = CairoFontFamily,
                                fontSize = 13.sp,
                                color = textColor.copy(alpha = 0.8f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Floating Form Card (White with Black 2.dp border in light mode, Black with White 2.dp border in dark mode)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .border(2.dp, borderCol, RoundedCornerShape(22.dp))
                            .background(cardBg)
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // 1. السنة الدراسية
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "السنة الدراسية",
                                fontFamily = CairoFontFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .border(2.dp, borderCol, RoundedCornerShape(14.dp))
                                    .background(cardBg)
                                    .clickable { isYearOpen = !isYearOpen }
                                    .padding(horizontal = 16.dp, vertical = 14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (isYearOpen) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                        contentDescription = null,
                                        tint = textColor
                                    )
                                    Text(
                                        text = selectedYear,
                                        fontFamily = CairoFontFamily,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textColor
                                    )
                                }
                            }

                            AnimatedVisibility(visible = isYearOpen) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 6.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .border(2.dp, borderCol, RoundedCornerShape(14.dp))
                                        .background(cardBg)
                                ) {
                                    listOf("2026 - 2025", "2025 - 2024").forEach { yr ->
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    selectedYear = yr
                                                    isYearOpen = false
                                                }
                                                .padding(horizontal = 16.dp, vertical = 12.dp)
                                        ) {
                                            Text(
                                                text = yr,
                                                fontFamily = CairoFontFamily,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = textColor
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // 2. المحافظة
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "المحافظة",
                                fontFamily = CairoFontFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .border(2.dp, borderCol, RoundedCornerShape(14.dp))
                                    .background(cardBg)
                                    .clickable { isGovOpen = !isGovOpen }
                                    .padding(horizontal = 16.dp, vertical = 14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (isGovOpen) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                        contentDescription = null,
                                        tint = textColor
                                    )
                                    Text(
                                        text = selectedGov,
                                        fontFamily = CairoFontFamily,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = textColor
                                    )
                                }
                            }

                            // Searchable Governorates dropdown
                            AnimatedVisibility(visible = isGovOpen) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 6.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .border(2.dp, borderCol, RoundedCornerShape(14.dp))
                                        .background(cardBg)
                                        .padding(8.dp)
                                ) {
                                    // Search bar (ابحث هنا)
                                    OutlinedTextField(
                                        value = govSearchQuery,
                                        onValueChange = { govSearchQuery = it },
                                        placeholder = { Text("ابحث هنا", fontFamily = CairoFontFamily, fontSize = 13.sp, color = textColor.copy(alpha = 0.6f)) },
                                        trailingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = textColor) },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = borderCol,
                                            unfocusedBorderColor = borderCol,
                                            focusedTextColor = textColor,
                                            unfocusedTextColor = textColor,
                                            focusedContainerColor = cardBg,
                                            unfocusedContainerColor = cardBg
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    val filteredGovs = ALL_SYRIAN_GOVERNORATES.filter {
                                        it.contains(govSearchQuery.trim(), ignoreCase = true)
                                    }

                                    LazyColumn(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .heightIn(max = 200.dp)
                                    ) {
                                        items(filteredGovs) { gov ->
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        selectedGov = gov
                                                        isGovOpen = false
                                                        govSearchQuery = ""
                                                    }
                                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                                            ) {
                                                Text(
                                                    text = gov,
                                                    fontFamily = CairoFontFamily,
                                                    fontSize = 14.sp,
                                                    color = textColor,
                                                    textAlign = TextAlign.End,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            }
                                            HorizontalDivider(color = borderCol.copy(alpha = 0.3f))
                                        }
                                    }
                                }
                            }
                        }

                        // 3. الشهادة
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "الشهادة",
                                fontFamily = CairoFontFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .border(2.dp, borderCol, RoundedCornerShape(14.dp))
                                    .background(cardBg)
                                    .clickable { isCertOpen = !isCertOpen }
                                    .padding(horizontal = 16.dp, vertical = 14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (isCertOpen) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                        contentDescription = null,
                                        tint = textColor
                                    )
                                    Text(
                                        text = selectedCert,
                                        fontFamily = CairoFontFamily,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = textColor,
                                        maxLines = 1,
                                        textAlign = TextAlign.End,
                                        modifier = Modifier.weight(1f).padding(start = 8.dp)
                                    )
                                }
                            }

                            // Searchable Certificates dropdown
                            AnimatedVisibility(visible = isCertOpen) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 6.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .border(2.dp, borderCol, RoundedCornerShape(14.dp))
                                        .background(cardBg)
                                        .padding(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = certSearchQuery,
                                        onValueChange = { certSearchQuery = it },
                                        placeholder = { Text("ابحث هنا", fontFamily = CairoFontFamily, fontSize = 13.sp, color = textColor.copy(alpha = 0.6f)) },
                                        trailingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = textColor) },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = borderCol,
                                            unfocusedBorderColor = borderCol,
                                            focusedTextColor = textColor,
                                            unfocusedTextColor = textColor,
                                            focusedContainerColor = cardBg,
                                            unfocusedContainerColor = cardBg
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    val filteredCerts = ALL_CERTIFICATES.filter {
                                        it.contains(certSearchQuery.trim(), ignoreCase = true)
                                    }

                                    LazyColumn(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .heightIn(max = 240.dp)
                                    ) {
                                        items(filteredCerts) { cert ->
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        selectedCert = cert
                                                        isCertOpen = false
                                                        certSearchQuery = ""
                                                    }
                                                    .padding(horizontal = 14.dp, vertical = 11.dp)
                                            ) {
                                                Text(
                                                    text = cert,
                                                    fontFamily = CairoFontFamily,
                                                    fontSize = 13.sp,
                                                    color = textColor,
                                                    textAlign = TextAlign.End,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            }
                                            HorizontalDivider(color = borderCol.copy(alpha = 0.3f))
                                        }
                                    }
                                }
                            }
                        }

                        // 4. رقم الاكتتاب
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "رقم الاكتتاب",
                                fontFamily = CairoFontFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            OutlinedTextField(
                                value = seatNumber,
                                onValueChange = { seatNumber = it.filter { ch -> ch.isDigit() }.take(8) },
                                placeholder = { Text("123456", fontFamily = CairoFontFamily, fontSize = 14.sp, color = textColor.copy(alpha = 0.4f)) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = borderCol,
                                    unfocusedBorderColor = borderCol,
                                    focusedTextColor = textColor,
                                    unfocusedTextColor = textColor,
                                    focusedContainerColor = cardBg,
                                    unfocusedContainerColor = cardBg
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // 5. زر استعلام عن النتيجة: White with 2.dp Black border in light mode, Black with 2.dp White border in dark mode
                        Button(
                            onClick = {
                                if (seatNumber.isBlank()) {
                                    resultDialogMessage = "يرجى إدخال رقم الاكتتاب للمتابعة"
                                    showResultDialog = true
                                    return@Button
                                }
                                isSearching = true
                                coroutineScope.launch {
                                    delay(1000)
                                    isSearching = false
                                    resultDialogMessage = "تم الاتصال بسيرفر وزارة التربية السورية:\nالنتائج قيد التدقيق والمعالجة الامتحانية ولم تصدر بعد."
                                    showResultDialog = true
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .border(
                                    width = 2.dp,
                                    color = borderCol,
                                    shape = RoundedCornerShape(14.dp)
                                ),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = cardBg,
                                contentColor = textColor
                            ),
                            enabled = !isSearching
                        ) {
                            if (isSearching) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = textColor,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("جارٍ الاستعلام...", fontFamily = CairoFontFamily, fontSize = 14.sp, color = textColor)
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = textColor
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "استعلام عن النتيجة",
                                    fontFamily = CairoFontFamily,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textColor
                                )
                            }
                        }

                        // 6. زر الطلاب الأوائل: White with 2.dp Black border in light mode, Black with 2.dp White border in dark mode
                        Button(
                            onClick = { showTopStudentsDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .border(
                                    width = 2.dp,
                                    color = borderCol,
                                    shape = RoundedCornerShape(14.dp)
                                ),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = cardBg,
                                contentColor = textColor
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = textColor
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "الطلاب الأوائل",
                                fontFamily = CairoFontFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            // Results Dialog
            if (showResultDialog) {
                AlertDialog(
                    onDismissRequest = { showResultDialog = false },
                    title = {
                        Text(
                            text = "حالة النتيجة",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End,
                            color = textColor
                        )
                    },
                    text = {
                        Text(
                            text = resultDialogMessage,
                            fontFamily = CairoFontFamily,
                            fontSize = 14.sp,
                            textAlign = TextAlign.End,
                            color = textColor
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = { showResultDialog = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = cardBg,
                                contentColor = textColor
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.border(1.5.dp, borderCol, RoundedCornerShape(10.dp))
                        ) {
                            Text("حسناً", fontFamily = CairoFontFamily, fontWeight = FontWeight.Bold)
                        }
                    },
                    containerColor = cardBg,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.border(2.dp, borderCol, RoundedCornerShape(20.dp))
                )
            }

            // Top Students Dialog
            if (showTopStudentsDialog) {
                AlertDialog(
                    onDismissRequest = { showTopStudentsDialog = false },
                    title = {
                        Text(
                            text = "لوحة الطلاب الأوائل ($selectedGov)",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End,
                            color = textColor
                        )
                    },
                    text = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "سيتم تفعيل لوحة الشرف ونشر أسماء المتفوقين في محافظة $selectedGov فور الإعلان الرسمي لنتائج الدورة الامتحانية 2026.",
                                fontFamily = CairoFontFamily,
                                fontSize = 13.sp,
                                textAlign = TextAlign.End,
                                color = textColor
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = { showTopStudentsDialog = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = cardBg,
                                contentColor = textColor
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.border(1.5.dp, borderCol, RoundedCornerShape(10.dp))
                        ) {
                            Text("إغلاق", fontFamily = CairoFontFamily, fontWeight = FontWeight.Bold)
                        }
                    },
                    containerColor = cardBg,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.border(2.dp, borderCol, RoundedCornerShape(20.dp))
                )
            }
        }
    }
}
