package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.CalculationMode
import com.example.model.DEFAULT_SUBJECTS

class ExamPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("exam_calculator_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_MODE = "calculation_mode"
        private const val KEY_DARK_MODE = "dark_mode"
        private const val KEY_STUDENT_NAME = "student_name"
        private const val KEY_SEAT_NUMBER = "seat_number"
        private const val KEY_PREFIX_SUBJECT = "score_"
    }

    fun saveScore(subjectId: String, score: String) {
        prefs.edit().putString(KEY_PREFIX_SUBJECT + subjectId, score).apply()
    }

    fun getScore(subjectId: String): String {
        return prefs.getString(KEY_PREFIX_SUBJECT + subjectId, "") ?: ""
    }

    fun getAllScores(): Map<String, String> {
        val map = mutableMapOf<String, String>()
        for (subject in DEFAULT_SUBJECTS) {
            map[subject.id] = getScore(subject.id)
        }
        return map
    }

    fun clearAllScores() {
        val editor = prefs.edit()
        for (subject in DEFAULT_SUBJECTS) {
            editor.remove(KEY_PREFIX_SUBJECT + subject.id)
        }
        editor.apply()
    }

    fun saveMode(mode: CalculationMode) {
        prefs.edit().putString(KEY_MODE, mode.name).apply()
    }

    fun getMode(): CalculationMode {
        val name = prefs.getString(KEY_MODE, CalculationMode.DROP_LOWER_LANGUAGE.name)
        return try {
            CalculationMode.valueOf(name ?: CalculationMode.DROP_LOWER_LANGUAGE.name)
        } catch (_: Exception) {
            CalculationMode.DROP_LOWER_LANGUAGE
        }
    }

    fun saveDarkMode(isDark: Boolean) {
        prefs.edit().putBoolean(KEY_DARK_MODE, isDark).apply()
    }

    fun isDarkMode(): Boolean {
        // Default is white/light theme as requested
        return prefs.getBoolean(KEY_DARK_MODE, false)
    }

    fun saveStudentInfo(name: String, seatNumber: String) {
        prefs.edit()
            .putString(KEY_STUDENT_NAME, name)
            .putString(KEY_SEAT_NUMBER, seatNumber)
            .apply()
    }

    fun getStudentName(): String = prefs.getString(KEY_STUDENT_NAME, "") ?: ""
    fun getSeatNumber(): String = prefs.getString(KEY_SEAT_NUMBER, "") ?: ""
}
