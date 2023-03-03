package com.example.forthekingtool.forthekinglogic

import com.example.forthekingtool.probability.BinomialDistributionCalculator

object ForTheKingLogic {

    // Note: Focus filled 5 and 6 are not listed on the wiki: https://fortheking.fandom.com/wiki/Focus
    val focusToChanceBoost =
        mapOf(0 to 0.0, 1 to 0.1, 2 to 0.15, 3 to 0.17, 4 to 0.19, 5 to 0.19, 6 to 0.19)

    fun calculateExactChances(rollChance: Double, totalRolls: Int, focus: Int): List<Double> {
        val rollChanceWithFocus = rollChance + (focusToChanceBoost[focus] ?: 0.0)
        val totalRollsWithoutFocus = totalRolls - focus
        val exactChancesAfterFocus = BinomialDistributionCalculator.calculateExactChances(rollChanceWithFocus, totalRollsWithoutFocus)
        val exactChancesBeforeFocus = List(focus) { 0.0 }
        return exactChancesBeforeFocus + exactChancesAfterFocus
    }
}