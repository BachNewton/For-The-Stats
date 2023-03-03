package com.example.forthekingtool.probability

import kotlin.math.pow

object BinomialDistributionCalculator {

    fun calculateAtLeastChances(changes: List<Double>): List<Double> {
        val atLeastChances = changes.toMutableList()

        for (i in atLeastChances.indices) {
            for (j in i + 1 until atLeastChances.size) {
                atLeastChances[i] += atLeastChances[j]
            }
        }

        return atLeastChances
    }

    fun calculateExactChances(rollChance: Double, totalRolls: Int): List<Double> {
        val chances = mutableListOf<Double>()

        for (successes in 0..totalRolls) {
            val exactly = probabilityMassFunction(totalRolls, successes, rollChance)
            chances.add(exactly)
        }

        return chances
    }

    @Suppress("UnnecessaryVariable")
    private fun probabilityMassFunction(
        independentBernoulliTrials: Int,
        successes: Int,
        probability: Double
    ): Double {
        val n = independentBernoulliTrials
        val k = successes
        val p = probability

        return n.choose(k) * p.pow(k) * (1 - p).pow(n - k)
    }

    fun Int.choose(k: Int): Int {
        val numerator = this.factorial()
        val denominator = k.factorial() * (this - k).factorial()
        return numerator / denominator
    }

    fun Int.factorial(): Int {
        if (this == 0) return 1
        if (this == 1) return this

        return this * (this - 1).factorial()
    }
}