package io.itcorner.moodle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import io.itcorner.moodle.core.ui.theme.MoodleTheme
import io.itcorner.moodle.navigation.MoodleAppRoot

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoodleTheme { MoodleAppRoot() }
        }
    }
}
