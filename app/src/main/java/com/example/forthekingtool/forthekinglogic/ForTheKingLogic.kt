package com.example.forthekingtool.forthekinglogic

import com.example.forthekingtool.probability.BinomialDistributionCalculator

object ForTheKingLogic {

    // Note: Focus filled 5 and 6 are not listed on the wiki: https://fortheking.fandom.com/wiki/Focus
    val focusToChanceBoost =
        mapOf(0 to 0.0, 1 to 0.1, 2 to 0.15, 3 to 0.17, 4 to 0.19, 5 to 0.19, 6 to 0.19)

    // From: https://fortheking.fandom.com/wiki/Critical_hit
    const val criticalBoostPerFocus = 0.05
    private const val criticalDamageModifier = 1.25

    fun calculateExactChances(rollChance: Double, totalRolls: Int, focus: Int): List<Double> {
        val rollChanceWithFocus =
            rollChance.plus(focusToChanceBoost[focus] ?: 0.0).coerceAtMost(1.0)
        val totalRollsWithoutFocus = totalRolls - focus
        val exactChancesAfterFocus = BinomialDistributionCalculator.calculateExactChances(
            rollChanceWithFocus,
            totalRollsWithoutFocus
        )
        val exactChancesBeforeFocus = List(focus) { 0.0 }
        return exactChancesBeforeFocus + exactChancesAfterFocus
    }

    fun calculateChanceToCritical(perfectChance: Double, criticalChance: Double): Double {
        return perfectChance * criticalChance
    }

    fun calculateCriticalDamage(damage: Int): Double {
        return damage * criticalDamageModifier
    }

    fun calculateAverageExpectedDamage(
        totalPossibleDamage: Int,
        exactChances: List<Double>,
        criticalChance: Double
    ): Double {
        val averageExpectedDamages = mutableListOf<Double>()

        exactChances.forEachIndexed { index, exactChance ->
            val damage = (totalPossibleDamage * index) / (exactChances.size - 1.0)
            val expectedDamage = damage * exactChance
            averageExpectedDamages.add(expectedDamage)
        }

        val chanceToCritical = calculateChanceToCritical(exactChances.last(), criticalChance)
        val chanceToPerfect = exactChances.last()
        val chanceToOnlyPerfect = chanceToPerfect - chanceToCritical

        val expectedPerfectDamage = totalPossibleDamage * chanceToOnlyPerfect
        val expectedCriticalDamage = calculateCriticalDamage(totalPossibleDamage) * chanceToCritical

        averageExpectedDamages[averageExpectedDamages.lastIndex] = expectedPerfectDamage
        averageExpectedDamages.add(expectedCriticalDamage)

        return averageExpectedDamages.sum()
    }
}