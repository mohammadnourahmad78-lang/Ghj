package com.example.ui

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ExamDataStore
import com.example.model.AppScreen
import com.example.model.CalculationMode
import com.example.model.DEFAULT_SUBJECTS
import com.example.model.ExamCalculationResult
import com.example.model.SubjectItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class ExamViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = ExamDataStore(application)

    private val _currentScreen = MutableStateFlow(AppScreen.CALCULATOR)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _scores = MutableStateFlow<Map<String, String>>(emptyMap())
    val scores: StateFlow<Map<String, String>> = _scores.asStateFlow()

    private val _currentMode = MutableStateFlow(CalculationMode.DROP_LOWER_LANGUAGE)
    val currentMode: StateFlow<CalculationMode> = _currentMode.asStateFlow()

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _isSubjectsExpanded = MutableStateFlow(true)
    val isSubjectsExpanded: StateFlow<Boolean> = _isSubjectsExpanded.asStateFlow()

    private val _isModeDropdownOpen = MutableStateFlow(false)
    val isModeDropdownOpen: StateFlow<Boolean> = _isModeDropdownOpen.asStateFlow()

    private val _showResultCardDialog = MutableStateFlow(false)
    val showResultCardDialog: StateFlow<Boolean> = _showResultCardDialog.asStateFlow()

    private val _showClearConfirmDialog = MutableStateFlow(false)
    val showClearConfirmDialog: StateFlow<Boolean> = _showClearConfirmDialog.asStateFlow()

    private val _studentName = MutableStateFlow("")
    val studentName: StateFlow<String> = _studentName.asStateFlow()

    private val _seatNumber = MutableStateFlow("")
    val seatNumber: StateFlow<String> = _seatNumber.asStateFlow()

    private val _calculationResult = MutableStateFlow(calculateResult(emptyMap(), CalculationMode.DROP_LOWER_LANGUAGE))
    val calculationResult: StateFlow<ExamCalculationResult> = _calculationResult.asStateFlow()

    init {
        observeDataStore()
    }

    private fun observeDataStore() {
        viewModelScope.launch {
            dataStore.scoresFlow.collect { savedScores ->
                _scores.value = savedScores
                recompute()
            }
        }
        viewModelScope.launch {
            dataStore.modeFlow.collect { savedMode ->
                _currentMode.value = savedMode
                recompute()
            }
        }
        viewModelScope.launch {
            dataStore.darkModeFlow.collect { isDark ->
                _isDarkMode.value = isDark
            }
        }
        viewModelScope.launch {
            dataStore.studentNameFlow.collect { name ->
                _studentName.value = name
            }
        }
        viewModelScope.launch {
            dataStore.seatNumberFlow.collect { seat ->
                _seatNumber.value = seat
            }
        }
    }

    fun onScoreChanged(subjectId: String, newValue: String) {
        // Allow digits only and max length 4
        val digitsOnly = newValue.filter { it.isDigit() }
        val sanitized = if (digitsOnly.length > 1 && digitsOnly.startsWith("0")) {
            digitsOnly.trimStart('0').ifEmpty { "0" }
        } else {
            digitsOnly
        }.take(4)

        val updated = _scores.value.toMutableMap()
        updated[subjectId] = sanitized
        _scores.value = updated
        recompute()

        viewModelScope.launch {
            dataStore.saveScore(subjectId, sanitized)
        }
    }

    fun setFullScore(subjectId: String, maxScore: Int) {
        onScoreChanged(subjectId, maxScore.toString())
    }

    fun setCalculationMode(mode: CalculationMode) {
        _currentMode.value = mode
        _isModeDropdownOpen.value = false
        recompute()

        viewModelScope.launch {
            dataStore.saveMode(mode)
        }
    }

    fun toggleDarkMode() {
        val next = !_isDarkMode.value
        _isDarkMode.value = next
        viewModelScope.launch {
            dataStore.saveDarkMode(next)
        }
    }

    fun toggleSubjectsExpanded() {
        _isSubjectsExpanded.value = !_isSubjectsExpanded.value
    }

    fun toggleModeDropdown() {
        _isModeDropdownOpen.value = !_isModeDropdownOpen.value
    }

    fun setModeDropdownOpen(open: Boolean) {
        _isModeDropdownOpen.value = open
    }

    fun openResultCardDialog() {
        _showResultCardDialog.value = true
    }

    fun closeResultCardDialog() {
        _showResultCardDialog.value = false
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun requestClearAll() {
        _showClearConfirmDialog.value = true
    }

    fun dismissClearDialog() {
        _showClearConfirmDialog.value = false
    }

    fun confirmClearAll() {
        _showClearConfirmDialog.value = false
        val cleared = mutableMapOf<String, String>()
        for (sub in DEFAULT_SUBJECTS) {
            cleared[sub.id] = ""
        }
        _scores.value = cleared
        recompute()

        viewModelScope.launch {
            dataStore.clearAllScores()
        }
        Toast.makeText(getApplication(), "تم تصفير جميع الدرجات", Toast.LENGTH_SHORT).show()
    }

    fun updateStudentInfo(name: String, seat: String) {
        _studentName.value = name
        _seatNumber.value = seat
        viewModelScope.launch {
            dataStore.saveStudentInfo(name, seat)
        }
    }

    private fun recompute() {
        _calculationResult.value = calculateResult(_scores.value, _currentMode.value)
    }

    private fun calculateResult(
        scoresMap: Map<String, String>,
        mode: CalculationMode
    ): ExamCalculationResult {
        var rawTotal = 0
        val subjectPercentages = mutableMapOf<String, Float>()

        for (subject in DEFAULT_SUBJECTS) {
            val scoreVal = scoresMap[subject.id]?.toIntOrNull() ?: 0
            rawTotal += scoreVal
            val pct = if (subject.maxScore > 0) (scoreVal.toFloat() / subject.maxScore) * 100f else 0f
            subjectPercentages[subject.id] = pct
        }

        // Compare languages (French vs English)
        val frenchScore = scoresMap["french"]?.toIntOrNull() ?: 0
        val englishScore = scoresMap["english"]?.toIntOrNull() ?: 0

        val lowerLanguageId = if (frenchScore < englishScore) {
            "french"
        } else if (englishScore < frenchScore) {
            "english"
        } else {
            "french"
        }
        val higherLanguageId = if (lowerLanguageId == "french") "english" else "french"

        // Mode 1: Drop lower language (out of 2800)
        val otherSum = (scoresMap["math"]?.toIntOrNull() ?: 0) +
                (scoresMap["arabic"]?.toIntOrNull() ?: 0) +
                (scoresMap["religion"]?.toIntOrNull() ?: 0) +
                (scoresMap["science"]?.toIntOrNull() ?: 0) +
                (scoresMap["social"]?.toIntOrNull() ?: 0)

        val higherLanguageScore = maxOf(frenchScore, englishScore)
        val droppedScore = otherSum + higherLanguageScore
        val droppedPercentage = (droppedScore.toFloat() / 2800f) * 100f

        // Mode 2: Include both languages (out of 3200)
        val fullScore = rawTotal
        val fullPercentage = (fullScore.toFloat() / 3200f) * 100f

        val diffScore = if (mode == CalculationMode.DROP_LOWER_LANGUAGE) droppedScore else fullScore
        val diffMax = mode.maxScore
        val diffPct = if (mode == CalculationMode.DROP_LOWER_LANGUAGE) droppedPercentage else fullPercentage

        // Determine highest and lowest scoring subjects among entered scores
        var highestId: String? = null
        var lowestId: String? = null
        var maxRatio = -1f
        var minRatio = 101f

        var validCount = 0
        for (subject in DEFAULT_SUBJECTS) {
            val raw = scoresMap[subject.id]
            if (!raw.isNullOrBlank()) {
                validCount++
                val ratio = subjectPercentages[subject.id] ?: 0f
                if (ratio > maxRatio) {
                    maxRatio = ratio
                    highestId = subject.id
                }
                if (ratio < minRatio) {
                    minRatio = ratio
                    lowestId = subject.id
                }
            }
        }

        if (validCount < 2) {
            highestId = null
            lowestId = null
        }

        return ExamCalculationResult(
            currentMode = mode,
            differentialScore = diffScore,
            differentialMaxScore = diffMax,
            differentialPercentage = diffPct,
            totalRawScore = rawTotal,
            totalPossibleMaxScore = 3200,
            droppedModeScore = droppedScore,
            droppedModePercentage = droppedPercentage,
            fullModeScore = fullScore,
            fullModePercentage = fullPercentage,
            lowerLanguageId = lowerLanguageId,
            higherLanguageId = higherLanguageId,
            highestScoreSubjectId = highestId,
            lowestScoreSubjectId = lowestId,
            subjectPercentages = subjectPercentages
        )
    }

    fun shareResult(context: Context) {
        val result = _calculationResult.value
        val report = buildShareText(result)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "نتيجة شهادة التعليم الأساسي")
            putExtra(Intent.EXTRA_TEXT, report)
        }
        val chooser = Intent.createChooser(intent, "مشاركة النتيجة عبر")
        context.startActivity(chooser)
    }

    fun copyResultToClipboard(context: Context) {
        val result = _calculationResult.value
        val report = buildShareText(result)
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Exam Result", report)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "تم نسخ تقرير النتيجة إلى الحافظة", Toast.LENGTH_SHORT).show()
    }

    private fun buildShareText(result: ExamCalculationResult): String {
        val sb = StringBuilder()
        sb.append("📋 تقرير نتيجة شهادة التعليم الأساسي\n")
        if (_studentName.value.isNotBlank()) {
            sb.append("👤 اسم الطالب: ${_studentName.value}\n")
        }
        if (_seatNumber.value.isNotBlank()) {
            sb.append("🔢 رقم الاكتتاب: ${_seatNumber.value}\n")
        }
        sb.append("───────────────\n")
        sb.append("📌 الوضع المختار: ${result.currentMode.titleAr}\n")
        sb.append(
            String.format(
                Locale.US,
                "🎯 المعدل: %.2f%%\n",
                result.differentialPercentage
            )
        )
        sb.append("📊 المجموع التفاضلي: ${result.differentialScore} / ${result.differentialMaxScore}\n")
        sb.append("───────────────\n")
        sb.append("📝 تفاصيل المواد:\n")

        for (subject in DEFAULT_SUBJECTS) {
            val score = _scores.value[subject.id]?.toIntOrNull() ?: 0
            val pct = result.subjectPercentages[subject.id] ?: 0f
            val isLowerLang = subject.id == result.lowerLanguageId
            val tag = if (isLowerLang && result.currentMode == CalculationMode.DROP_LOWER_LANGUAGE) " [تُحذف]" else ""
            sb.append(
                String.format(
                    Locale.US,
                    "• %s: %d / %d (%.1f%%)%s\n",
                    subject.nameAr,
                    score,
                    subject.maxScore,
                    pct,
                    tag
                )
            )
        }

        sb.append("───────────────\n")
        sb.append("📈 مقارنة النسبتين:\n")
        sb.append(
            String.format(
                Locale.US,
                "1. بحذف اللغة الأدنى: %d / 2800 (%.2f%%)\n",
                result.droppedModeScore,
                result.droppedModePercentage
            )
        )
        sb.append(
            String.format(
                Locale.US,
                "2. مع اللغتين معاً: %d / 3200 (%.2f%%)\n",
                result.fullModeScore,
                result.fullModePercentage
            )
        )
        sb.append("───────────────\n")
        sb.append("مجموع المواد الكلي: ${result.totalRawScore} / 3200\n")
        sb.append("✨ تم الحساب بواسطة تطبيق حاسبة النتائج")
        return sb.toString()
    }
}
