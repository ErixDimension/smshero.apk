package com.android.smshero

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.android.smshero.navigation.AppNavGraph
import com.android.smshero.ui.theme.SmsHeroTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SmsHeroTheme { AppNavGraph() } }
    }
}
