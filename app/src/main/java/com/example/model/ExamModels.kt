package com.example.model

/**
 * Data model for a school subject in Syrian Basic Education Certificate (شهادة التعليم الأساسي)
 */
data class SubjectItem(
    val id: String,
    val nameAr: String,
    val maxScore: Int,
    val isLanguage: Boolean = false,
    val isFrench: Boolean = false,
    val isEnglish: Boolean = false,
)

/**
 * Calculation mode selected by the student
 */
enum class CalculationMode(val titleAr: String, val maxScore: Int) {
    DROP_LOWER_LANGUAGE(
        titleAr = "المجموع بعد حذف اللغة الأقل",
        maxScore = 2800
    ),
    INCLUDE_BOTH_LANGUAGES(
        titleAr = "المجموع الكامل مع اللغة الأقل",
        maxScore = 3200
    );
}

/**
 * The standard subjects with official maximum scores:
 * الرياضيات 600
 * اللغة العربية 600
 * اللغة الفرنسية 400
 * اللغة الإنكليزية 400
 * التربية الدينية 200
 * العلوم العامة 400
 * الاجتماعيات 600
 * Total = 3200 (or 2800 after dropping lower language)
 */
val DEFAULT_SUBJECTS = listOf(
    SubjectItem(
        id = "math",
        nameAr = "الرياضيات",
        maxScore = 600
    ),
    SubjectItem(
        id = "arabic",
        nameAr = "اللغة العربية",
        maxScore = 600
    ),
    SubjectItem(
        id = "french",
        nameAr = "اللغة الفرنسية",
        maxScore = 400,
        isLanguage = true,
        isFrench = true
    ),
    SubjectItem(
        id = "english",
        nameAr = "اللغة الإنكليزية",
        maxScore = 400,
        isLanguage = true,
        isEnglish = true
    ),
    SubjectItem(
        id = "religion",
        nameAr = "التربية الدينية",
        maxScore = 200
    ),
    SubjectItem(
        id = "science",
        nameAr = "العلوم العامة",
        maxScore = 400
    ),
    SubjectItem(
        id = "social",
        nameAr = "الاجتماعيات",
        maxScore = 600
    )
)

/**
 * Calculation result containing all computed metrics
 */
data class ExamCalculationResult(
    val currentMode: CalculationMode,
    val differentialScore: Int,
    val differentialMaxScore: Int,
    val differentialPercentage: Float,
    val totalRawScore: Int,
    val totalPossibleMaxScore: Int,
    // Comparison metrics
    val droppedModeScore: Int,
    val droppedModePercentage: Float,
    val fullModeScore: Int,
    val fullModePercentage: Float,
    // Language identification
    val lowerLanguageId: String?,
    val higherLanguageId: String?,
    // Subject stats
    val highestScoreSubjectId: String?,
    val lowestScoreSubjectId: String?,
    val subjectPercentages: Map<String, Float>
)
