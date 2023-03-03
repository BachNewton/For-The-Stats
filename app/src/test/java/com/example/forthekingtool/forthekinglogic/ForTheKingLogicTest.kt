package com.example.forthekingtool.forthekinglogic

import com.example.forthekingtool.forthekinglogic.ForTheKingLogic.calculateAverageExpectedDamage
import com.example.forthekingtool.forthekinglogic.ForTheKingLogic.calculateExactChances
import org.junit.Test

internal class ForTheKingLogicTest {

    @Test
    fun focusTest1() {
        val exactChances = calculateExactChances(0.75, 4, 1)
        val temp = true
    }

    @Test
    fun focusTest2() {
        val exactChances = calculateExactChances(0.75, 4, 2)
        val temp = true
    }

    @Test
    fun focusTest3() {
        val exactChances = calculateExactChances(0.75, 4, 4)
        val temp = true
    }

    @Test
    fun calculateAverageExpectedDamageTest() {
        val averageExpectedDamage =
            calculateAverageExpectedDamage(
                100,
                calculateExactChances(0.75, 4, 1),
                0.1
            )
        val expected = 90.28531249999999
        assert(averageExpectedDamage == expected)
    }
}