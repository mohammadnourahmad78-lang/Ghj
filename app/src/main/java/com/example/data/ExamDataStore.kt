package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.CalculationMode
import com.example.model.DEFAULT_SUBJECTS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "exam_calculator_datastore")

/**
 * Jetpack DataStore implementation for reactive and persistent storage of
 * subject scores, mode, theme, and student information.
 */
class ExamDataStore(private val context: Context) {

    companion object {
        private val KEY_MODE = stringPreferencesKey("calculation_mode")
        private val KEY_DARK_MODE = booleanPreferencesKey("dark_mode")
        private val KEY_STUDENT_NAME = stringPreferencesKey("student_name")
        private val KEY_SEAT_NUMBER = stringPreferencesKey("seat_number")

        fun getSubjectKey(subjectId: String) = stringPreferencesKey("score_$subjectId")
    }

    val scoresFlow: Flow<Map<String, String>> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val scores = mutableMapOf<String, String>()
            for (sub in DEFAULT_SUBJECTS) {
                scores[sub.id] = preferences[getSubjectKey(sub.id)] ?: ""
            }
            scores
        }

    val modeFlow: Flow<CalculationMode> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { preferences ->
            val name = preferences[KEY_MODE] ?: CalculationMode.DROP_LOWER_LANGUAGE.name
            try {
                CalculationMode.valueOf(name)
            } catch (_: Exception) {
                CalculationMode.DROP_LOWER_LANGUAGE
            }
        }

    val darkModeFlow: Flow<Boolean> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { preferences ->
            preferences[KEY_DARK_MODE] ?: false
        }

    val studentNameFlow: Flow<String> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { preferences -> preferences[KEY_STUDENT_NAME] ?: "" }

    val seatNumberFlow: Flow<String> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { preferences -> preferences[KEY_SEAT_NUMBER] ?: "" }

    suspend fun saveScore(subjectId: String, score: String) {
        context.dataStore.edit { preferences ->
            preferences[getSubjectKey(subjectId)] = score
        }
    }

    suspend fun clearAllScores() {
        context.dataStore.edit { preferences ->
            for (sub in DEFAULT_SUBJECTS) {
                preferences.remove(getSubjectKey(sub.id))
            }
        }
    }

    suspend fun saveMode(mode: CalculationMode) {
        context.dataStore.edit { preferences ->
            preferences[KEY_MODE] = mode.name
        }
    }

    suspend fun saveDarkMode(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_DARK_MODE] = isDark
        }
    }

    suspend fun saveStudentInfo(name: String, seat: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_STUDENT_NAME] = name
            preferences[KEY_SEAT_NUMBER] = seat
        }
    }
}
