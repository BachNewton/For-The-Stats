package com.example.forthekingtool

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.forthekingtool.ui.MainUi
import com.example.forthekingtool.ui.theme.ForTheKingToolTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ForTheKingToolTheme { MainUi() } }
    }
}
