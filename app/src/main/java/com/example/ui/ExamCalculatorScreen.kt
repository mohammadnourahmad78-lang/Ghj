package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ClearConfirmDialog
import com.example.ui.components.ModeSelector
import com.example.ui.components.ResultCardDialog
import com.example.ui.components.SubjectsSection
import com.example.ui.components.SummaryCards
import com.example.ui.components.TopActionBar
import com.example.ui.theme.CairoFontFamily

@Composable
fun ExamCalculatorScreen(
    viewModel: ExamViewModel,
    onNavigateToResults: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scores by viewModel.scores.collectAsState()
    val currentMode by viewModel.currentMode.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val isSubjectsExpanded by viewModel.isSubjectsExpanded.collectAsState()
    val isModeDropdownOpen by viewModel.isModeDropdownOpen.collectAsState()
    val showResultCardDialog by viewModel.showResultCardDialog.collectAsState()
    val showClearConfirmDialog by viewModel.showClearConfirmDialog.collectAsState()
    val studentName by viewModel.studentName.collectAsState()
    val seatNumber by viewModel.seatNumber.collectAsState()
    val calculationResult by viewModel.calculationResult.collectAsState()

    // Base background colors
    val screenBg = if (isDarkMode) Color(0xFF121212) else Color.White
    val contentBg = if (isDarkMode) Color(0xFF121212) else Color.White

    // Provide RTL layout direction for Arabic experience
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .background(screenBg)
                .windowInsetsPadding(WindowInsets.safeDrawing),
            containerColor = screenBg,
            topBar = {
                TopActionBar(
                    isDarkMode = isDarkMode,
                    onToggleDarkMode = { viewModel.toggleDarkMode() },
                    onClearAll = { viewModel.requestClearAll() },
                    onCopy = { viewModel.copyResultToClipboard(context) },
                    onOpenResultStatus = onNavigateToResults
                )
            }
        ) { innerPadding ->
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(contentBg),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .widthIn(max = 560.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 1. Top Dropdown Mode Selector
                    ModeSelector(
                        currentMode = currentMode,
                        isOpen = isModeDropdownOpen,
                        onToggle = { viewModel.toggleModeDropdown() },
                        onSelectMode = { mode -> viewModel.setCalculationMode(mode) },
                        isDarkMode = isDarkMode
                    )

                    // 2. Summary Boxes + Big Black Percentage Card
                    SummaryCards(
                        differentialScore = calculationResult.differentialScore,
                        maxScore = calculationResult.differentialMaxScore,
                        percentage = calculationResult.differentialPercentage,
                        isDarkMode = isDarkMode
                    )

                    // 3. Collapsible Subjects Section
                    SubjectsSection(
                        isExpanded = isSubjectsExpanded,
                        onToggleExpanded = { viewModel.toggleSubjectsExpanded() },
                        scores = scores,
                        onScoreChanged = { subId, newVal -> viewModel.onScoreChanged(subId, newVal) },
                        calculationResult = calculationResult,
                        isDarkMode = isDarkMode
                    )

                    // 4. Quick Actions row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { viewModel.openResultCardDialog() },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .border(
                                    width = 2.dp,
                                    color = if (isDarkMode) Color.White else Color.Black,
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .testTag("btn_open_card"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isDarkMode) Color.Black else Color.White,
                                contentColor = if (isDarkMode) Color.White else Color.Black
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCode,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "بطاقة النتيجة",
                                fontFamily = CairoFontFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = { viewModel.shareResult(context) },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .border(
                                    width = 2.dp,
                                    color = if (isDarkMode) Color.White else Color.Black,
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .testTag("btn_share_result"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isDarkMode) Color.Black else Color.White,
                                contentColor = if (isDarkMode) Color.White else Color.Black
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "مشاركة النتيجة",
                                fontFamily = CairoFontFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))
                }
            }

            // Dialogs
            ResultCardDialog(
                isOpen = showResultCardDialog,
                onDismiss = { viewModel.closeResultCardDialog() },
                calculationResult = calculationResult,
                scores = scores,
                studentName = studentName,
                seatNumber = seatNumber,
                onSaveStudentInfo = { name, seat -> viewModel.updateStudentInfo(name, seat) },
                onShare = { viewModel.shareResult(context) },
                isDarkMode = isDarkMode
            )

            ClearConfirmDialog(
                isOpen = showClearConfirmDialog,
                onConfirm = { viewModel.confirmClearAll() },
                onDismiss = { viewModel.dismissClearDialog() },
                isDarkMode = isDarkMode
            )
        }
    }
}
