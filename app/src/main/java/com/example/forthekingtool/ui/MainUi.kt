package com.example.forthekingtool.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.forthekingtool.R
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.forthekingtool.probability.BinomialDistributionCalculator
import com.example.forthekingtool.ui.theme.ForTheKingToolTheme

@Composable
fun MainUi() {
    Box {
        TopAppBar(title = {Text("For The Stats!")})

        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val rolls = remember { mutableStateOf(3) }
            val rollChanceString = remember { mutableStateOf("75") }
            val damageString = remember { mutableStateOf("10") }

            val damage = if (damageString.value.isEmpty()) 0 else damageString.value.toInt()
            val rollChance = if (rollChanceString.value.isEmpty()) 0.0 else rollChanceString.value.toDouble() / 100
            val exactChances = BinomialDistributionCalculator.calculateExactChances(rollChance, rolls.value)
            val atLeastChances = BinomialDistributionCalculator.calculateAtLeastChances(exactChances)

            InputRow {
                DamageInput(damageString)
            }

            ChanceOutputRow {
                ChanceOutput(exactChances, atLeastChances, damage)
            }

            InputRow {
                ChanceInput(rollChanceString)
            }

            RollIconsRow {
                RollIcons(rolls)
            }
        }
    }

}

@Composable
private fun ChanceOutputRow(Content: @Composable () -> Unit) {
    Row(modifier = Modifier.padding(bottom = 5.dp, top = 5.dp)) { Content() }
}

@Composable
private fun ChanceOutput(exactChances: List<Double>, atLeastChances: List<Double>, damage: Int) {
    Column {
        Text("Fail all rolls: ")
        for (i in 1 until atLeastChances.size - 1) {
            Text("At least $i: ")
        }
        Text("Perfect: ")
    }
    Column(horizontalAlignment = Alignment.End) {
        Text(exactChances[0].toChance())
        for (i in 1 until atLeastChances.size - 1) {
            Text(atLeastChances[i].toChance())
        }
        Text(exactChances.last().toChance())
    }
    Column(horizontalAlignment = Alignment.End) {
        for (i in atLeastChances.indices) {
            Text(" to do ${(i * damage) / (atLeastChances.size - 1)}")
        }
    }
}

private fun Double.toChance(): String {
    return String.format("%.2f%%", this * 100)
}

@Composable
private fun InputRow(Content: @Composable () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) { Content() }
}

@Composable
private fun ChanceInput(rollChance: MutableState<String>) {
    QuickNumberInput(label = "Roll Chance", number = rollChance)
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
            .padding(top = 5.dp)
    ) {
        Content()
    }
}

@Composable
private fun RollIcons(rolls: MutableState<Int>) {
    Image(
        painterResource(id = R.drawable.luck),
        null,
        Modifier.clickable { rolls.value = 1 })
    Image(
        painterResource(id = if (rolls.value > 1) R.drawable.luck else R.drawable.luck_disabled),
        null,
        Modifier.clickable { rolls.value = 2 })
    Image(
        painterResource(id = if (rolls.value > 2) R.drawable.luck else R.drawable.luck_disabled),
        null,
        Modifier.clickable { rolls.value = 3 })
    Image(
        painterResource(id = if (rolls.value > 3) R.drawable.luck else R.drawable.luck_disabled),
        null,
        Modifier.clickable { rolls.value = 4 })
    Image(
        painterResource(id = if (rolls.value > 4) R.drawable.luck else R.drawable.luck_disabled),
        null,
        Modifier.clickable { rolls.value = 5 })
    Image(
        painterResource(id = if (rolls.value > 5) R.drawable.luck else R.drawable.luck_disabled),
        null,
        Modifier.clickable { rolls.value = 6 })
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