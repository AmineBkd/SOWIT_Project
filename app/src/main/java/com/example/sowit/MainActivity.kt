package com.example.sowit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.sowit.ui.map.MapScreen
import com.example.sowit.ui.theme.SOWITTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.light(
                getColor(R.color.white),
                getColor(R.color.black),
            )
        )
        setContent {
            SOWITTheme {
                MapScreen()
            }
        }
    }
}