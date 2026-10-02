package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.model.AppScreen
import com.example.ui.ExamCalculatorScreen
import com.example.ui.ExamViewModel
import com.example.ui.ResultsQueryScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: ExamViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkMode by viewModel.isDarkMode.collectAsState()
            val currentScreen by viewModel.currentScreen.collectAsState()

            MyApplicationTheme(darkTheme = isDarkMode) {
                AnimatedContent(
                    targetState = currentScreen,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "ScreenTransition"
                ) { screen ->
                    when (screen) {
                        AppScreen.CALCULATOR -> {
                            ExamCalculatorScreen(
                                viewModel = viewModel,
                                onNavigateToResults = { viewModel.navigateTo(AppScreen.RESULTS_QUERY) }
                            )
                        }
                        AppScreen.RESULTS_QUERY -> {
                            ResultsQueryScreen(
                                viewModel = viewModel,
                                onBack = { viewModel.navigateTo(AppScreen.CALCULATOR) }
                            )
                        }
                    }
                }
            }
        }
    }
}
