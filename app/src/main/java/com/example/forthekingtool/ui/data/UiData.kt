package com.example.forthekingtool.ui.data

import androidx.compose.runtime.MutableState

data class TabData(
    val subTabSelected: MutableState<Int>,
    val subTabsData: List<SubTabData>
)

data class SubTabData(
    val rolls: MutableState<Int>,
    val focus: MutableState<Int>,
    val rollChanceString: MutableState<String>,
    val damageString: MutableState<String>,
    val criticalChanceString: MutableState<String>
)