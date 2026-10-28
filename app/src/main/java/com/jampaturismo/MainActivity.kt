package com.jampaturismo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jampaturismo.core.theme.JampaTurismoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val systemDark = androidx.compose.foundation.isSystemInDarkTheme()
            var isDarkTheme by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(systemDark) }

            JampaTurismoTheme(darkTheme = isDarkTheme) {
                JampaTurismoApp(
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = { isDarkTheme = !isDarkTheme }
                )
            }
        }

    }
}
