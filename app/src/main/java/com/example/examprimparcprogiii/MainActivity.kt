package com.example.examprimparcprogiii

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.examprimparcprogiii.ui.BuhoLectorApp
import com.example.examprimparcprogiii.ui.theme.ExamPrimParcProgIIITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Íconos claros en la barra de estado, porque la TopAppBar es marrón
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        setContent {
            ExamPrimParcProgIIITheme {
                BuhoLectorApp()
            }
        }
    }
}
