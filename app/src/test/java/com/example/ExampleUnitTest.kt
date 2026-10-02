package com.example

import com.example.model.CalculationMode
import com.example.model.DEFAULT_SUBJECTS
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun verifySubjectsAndMaxScores() {
        val totalMax = DEFAULT_SUBJECTS.sumOf { it.maxScore }
        assertEquals("Total max score of all 7 subjects must be 3200", 3200, totalMax)

        val math = DEFAULT_SUBJECTS.first { it.id == "math" }
        assertEquals(600, math.maxScore)

        val arabic = DEFAULT_SUBJECTS.first { it.id == "arabic" }
        assertEquals(600, arabic.maxScore)

        val french = DEFAULT_SUBJECTS.first { it.id == "french" }
        assertEquals(400, french.maxScore)

        val english = DEFAULT_SUBJECTS.first { it.id == "english" }
        assertEquals(400, english.maxScore)

        val religion = DEFAULT_SUBJECTS.first { it.id == "religion" }
        assertEquals(200, religion.maxScore)

        val science = DEFAULT_SUBJECTS.first { it.id == "science" }
        assertEquals(400, science.maxScore)

        val social = DEFAULT_SUBJECTS.first { it.id == "social" }
        assertEquals(600, social.maxScore)
    }

    @Test
    fun verifyDropLowerLanguageMode() {
        val frenchScore = 50
        val englishScore = 400
        val mathScore = 600
        val arabicScore = 550
        val religionScore = 200
        val scienceScore = 380
        val socialScore = 580

        // In drop mode: French is lower (50 < 400), so 50 is dropped
        val higherLang = maxOf(frenchScore, englishScore)
        assertEquals(400, higherLang)

        val differentialScore = mathScore + arabicScore + religionScore + scienceScore + socialScore + higherLang
        assertEquals(2710, differentialScore)

        val maxScore = CalculationMode.DROP_LOWER_LANGUAGE.maxScore
        assertEquals(2800, maxScore)

        val percentage = (differentialScore.toFloat() / maxScore) * 100f
        assertTrue(percentage in 96.7f..96.8f)
    }

    @Test
    fun verifyIncludeBothLanguagesMode() {
        val frenchScore = 50
        val englishScore = 400
        val mathScore = 600
        val arabicScore = 550
        val religionScore = 200
        val scienceScore = 380
        val socialScore = 580

        val totalRawScore = mathScore + arabicScore + frenchScore + englishScore + religionScore + scienceScore + socialScore
        assertEquals(2760, totalRawScore)

        val maxScore = CalculationMode.INCLUDE_BOTH_LANGUAGES.maxScore
        assertEquals(3200, maxScore)

        val percentage = (totalRawScore.toFloat() / maxScore) * 100f
        assertTrue(percentage in 86.2f..86.3f)
    }
}
