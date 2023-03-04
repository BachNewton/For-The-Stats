package com.example.forthekingtool.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.forthekingtool.R
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.forthekingtool.forthekinglogic.ForTheKingLogic
import com.example.forthekingtool.probability.BinomialDistributionCalculator
import com.example.forthekingtool.ui.data.TabData
import com.example.forthekingtool.ui.theme.ForTheKingToolTheme

@Composable
fun MainUi() {
    val tabSelected = remember { mutableStateOf(0) }

    MainUiContainer(tabSelected) {
        val tabsData = remember {
            List(3) {
                TabData(
                    rolls = mutableStateOf(6),
                    focus = mutableStateOf(2),
                    rollChanceString = mutableStateOf("75"),
                    damageString = mutableStateOf("10"),
                    criticalChanceString = mutableStateOf("5")
                )
            }
        }

        val tabData = tabsData[tabSelected.value]
        val rolls = tabData.rolls
        val focus = tabData.focus
        val rollChanceString = tabData.rollChanceString
        val damageString = tabData.damageString
        val criticalChanceString = tabData.criticalChanceString

        val damage = if (damageString.value.isEmpty()) 0 else damageString.value.toInt()

        val rollChance =
            if (rollChanceString.value.isEmpty()) 0.0 else rollChanceString.value.toDouble() / 100

        val criticalChance =
            if (criticalChanceString.value.isEmpty()) 0.0 else criticalChanceString.value.toDouble() / 100

        val criticalBoostFromFocus = focus.value * ForTheKingLogic.criticalBoostPerFocus

        val criticalChanceWithFocus = criticalChance.plus(criticalBoostFromFocus).coerceAtMost(1.0)

        val exactChances =
            ForTheKingLogic.calculateExactChances(rollChance, rolls.value, focus.value)

        val atLeastChances =
            BinomialDistributionCalculator.calculateAtLeastChances(exactChances)

        ChanceOutputRow {
            ChanceOutput(exactChances, atLeastChances, damage, criticalChanceWithFocus)
        }

        Text(
            "Expected Damage: ${
                String.format(
                    "%.1f", ForTheKingLogic.calculateAverageExpectedDamage(
                        damage,
                        exactChances,
                        criticalChanceWithFocus
                    )
                )
            }"
        )

        InputRow {
            DamageInput(damageString)
            Spacer(Modifier.width(15.dp))
            ChanceInput(rollChanceString)
            if (focus.value > 0) {
                ChanceBoost(ForTheKingLogic.focusToChanceBoost[focus.value] ?: 0.0)
            }
        }

        InputRow {
            CriticalInput(criticalChanceString)
            if (focus.value > 0) {
                ChanceBoost(criticalBoostFromFocus)
            }
        }

        RollIconsRow {
            RollIcons(rolls, focus)
        }

        FocusRow {
            FocusButton("-") {
                focus.value = focus.value.minus(1).coerceAtLeast(0)
            }
            Text("Focus", modifier = Modifier.padding(horizontal = 15.dp))
            FocusButton("+") {
                focus.value = focus.value.plus(1).coerceAtMost(rolls.value)
            }
        }
    }
}

@Composable
private fun MainUiContainer(tabSelected: MutableState<Int>, Content: @Composable () -> Unit) {
    Box {
        Column(Modifier.background(MaterialTheme.colors.primary)) {
            TopAppBar(
                title = { Text("For The Stats!") },
                actions = { TopAppBarTabs(tabSelected) },
                backgroundColor = MaterialTheme.colors.primary
            )
            SubTabsRow {
                SubTabs()
            }
        }

        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Content()
        }
    }
}

@Composable
private fun SubTabsRow(Content: @Composable () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Content()
    }
}

@Composable
private fun SubTabs() {
    SubTab(R.drawable.sword, false) { /* TODO */ }
    SubTab(R.drawable.bow, true) { /* TODO */ }
    SubTab(R.drawable.book, false) { /* TODO */ }
    SubTab(R.drawable.instrument, false) {/* TODO */ }
    SubTab(R.drawable.gun, false) { /* TODO */ }
}

@Composable
private fun SubTab(@DrawableRes id: Int, isSelected: Boolean, onClick: () -> Unit) {
    val defaultModifier = Modifier
        .clickable { onClick() }
        .size(46.dp)

    val selectedModifier = defaultModifier.background(Color.White)
    val tintColor = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.onPrimary

    Image(
        painterResource(id),
        null,
        colorFilter = ColorFilter.tint(tintColor),
        modifier = if (isSelected) selectedModifier else defaultModifier
    )
}

@Composable
private fun TopAppBarTabs(tabSelected: MutableState<Int>) {
    Tab(R.drawable.blacksmith, tabSelected.value == 0) { tabSelected.value = 0 }
    Tab(R.drawable.hunter, tabSelected.value == 1) { tabSelected.value = 1 }
    Tab(R.drawable.scholar, tabSelected.value == 2) { tabSelected.value = 2 }
}

@Composable
private fun Tab(@DrawableRes id: Int, isSelected: Boolean, onClick: () -> Unit) {
    val defaultModifier = Modifier
        .clickable { onClick() }
        .size(65.dp)
        .padding(horizontal = 5.dp)

    val selectedModifier = defaultModifier.background(Color.White)

    Image(
        painterResource(id),
        null,
        if (isSelected) selectedModifier else defaultModifier
    )
}

@Composable
private fun ChanceBoost(chance: Double) {
    val chanceBoostFormatted = (chance * 100).toInt().toString()
    Text(" + $chanceBoostFormatted%")
}

@Composable
private fun FocusRow(Content: @Composable () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 15.dp)
    ) { Content() }
}

@Composable
private fun FocusButton(text: String, onClick: () -> Unit) {
    Button(modifier = Modifier.size(52.dp), onClick = onClick) {
        Text(text, fontSize = 24.sp)
    }
}

@Composable
private fun ChanceOutputRow(Content: @Composable () -> Unit) {
    Row(modifier = Modifier.padding(vertical = 5.dp)) { Content() }
}

@Composable
private fun ChanceOutput(
    exactChances: List<Double>,
    atLeastChances: List<Double>,
    damage: Int,
    criticalChance: Double
) {
    Column {
        Text("Fail all rolls: ")
        for (i in 1 until atLeastChances.size - 1) {
            Text("At least $i: ")
        }
        Text("Perfect: ")
        Text("Critical: ")
    }
    Column(horizontalAlignment = Alignment.End) {
        Text(exactChances[0].toChance())
        for (i in 1 until atLeastChances.size - 1) {
            Text(atLeastChances[i].toChance())
        }
        Text(exactChances.last().toChance())
        Text(
            ForTheKingLogic.calculateChanceToCritical(exactChances.last(), criticalChance)
                .toChance()
        )
    }
    Column(horizontalAlignment = Alignment.End) {
        for (i in atLeastChances.indices) {
            Text(" to do ${(i * damage) / (atLeastChances.size - 1)}")
        }
        Text(" to do ${(ForTheKingLogic.calculateCriticalDamage(damage)).toInt()}")
    }
}

private fun Double.toChance(): String {
    return String.format("%.2f%%", this * 100)
}

@Composable
private fun InputRow(Content: @Composable () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 5.dp)
    ) { Content() }
}

@Composable
private fun CriticalInput(criticalChance: MutableState<String>) {
    QuickNumberInput(label = "Critical Chance", number = criticalChance)
}

@Composable
private fun ChanceInput(rollChance: MutableState<String>) {
    QuickNumberInput(label = "Roll", number = rollChance)
}

@Composable
private fun DamageInput(damage: MutableState<String>) {
    QuickNumberInput(label = "Damage", number = damage)
}

@Composable
private fun QuickNumberInput(label: String, number: MutableState<String>) {
    val focusManager = LocalFocusManager.current

    Text("$label: ")

    TextField(
        value = number.value,
        onValueChange = {
            if (it.length > 1) {
                focusManager.clearFocus()
            }
            number.value = it
        },
        modifier = Modifier
            .width(65.dp)
            .onFocusChanged {
                if (it.isFocused) {
                    number.value = ""
                }
            }
            .border(1.dp, MaterialTheme.colors.primary),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = TextStyle(fontSize = 28.sp, textAlign = TextAlign.Center),
        singleLine = true,
        keyboardActions = KeyboardActions { focusManager.clearFocus() }
    )
}

@Composable
private fun RollIconsRow(Content: @Composable () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        Content()
    }
}

@Composable
private fun RollIcons(rolls: MutableState<Int>, focus: MutableState<Int>) {
    repeat(6) {
        RollIcon(it, rolls, focus)
    }
}

@Composable
private fun RollIcon(index: Int, rolls: MutableState<Int>, focus: MutableState<Int>) {
    val id = if (focus.value > index) {
        R.drawable.focus
    } else if (index < rolls.value) {
        R.drawable.luck
    } else {
        R.drawable.luck_disabled
    }

    Image(
        painterResource(id),
        null,
        Modifier.clickable {
            rolls.value = index + 1
            focus.value = focus.value.coerceAtMost(rolls.value)
        })
}

@Preview(
    name = "MainUi - Dark",
    showSystemUi = true
)
@Composable
fun MainUiPreviewDark() {
    ForTheKingToolTheme(darkTheme = true) {
        MainUi()
    }
}

@Preview(
    name = "MainUi - Light",
    showSystemUi = true
)
@Composable
fun MainUiPreviewLight() {
    ForTheKingToolTheme(darkTheme = false) {
        MainUi()
    }
}

@Preview(
    name = "MainUi - Pixel 3",
    showSystemUi = true,
    device = Devices.PIXEL_3
)
@Composable
fun MainUiPreviewPixel3() {
    ForTheKingToolTheme {
        MainUi()
    }
}