package com.android.smshero

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.smshero.ui.MainScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainScreen = MainScreen(this)
        setContentView(mainScreen.createView())
    }
}
