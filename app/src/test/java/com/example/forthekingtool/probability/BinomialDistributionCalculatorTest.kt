package com.example.forthekingtool.probability

import com.example.forthekingtool.probability.BinomialDistributionCalculator.calculateAtLeastChances
import com.example.forthekingtool.probability.BinomialDistributionCalculator.calculateExactChances
import com.example.forthekingtool.probability.BinomialDistributionCalculator.choose
import com.example.forthekingtool.probability.BinomialDistributionCalculator.factorial
import org.junit.Test


internal class BinomialDistributionCalculatorTest {

    @Test
    fun factorial_0() {
        val actual = 0.factorial()
        val expected = 1
        assert(actual == expected)
    }

    @Test
    fun factorial_1() {
        val actual = 1.factorial()
        val expected = 1
        assert(actual == expected)
    }

    @Test
    fun factorial_2() {
        val actual = 2.factorial()
        val expected = 2
        assert(actual == expected)
    }

    @Test
    fun factorial_3() {
        val actual = 3.factorial()
        val expected = 6
        assert(actual == expected)
    }

    @Test
    fun factorial_4() {
        val actual = 4.factorial()
        val expected = 24
        assert(actual == expected)
    }

    @Test
    fun factorial_5() {
        val actual = 5.factorial()
        val expected = 120
        assert(actual == expected)
    }

    @Test
    fun factorial_6() {
        val actual = 6.factorial()
        val expected = 720
        assert(actual == expected)
    }

    @Test
    fun five_choose_three() {
        val actual = 5.choose(3)
        val expected = 10
        assert(actual == expected)
    }

    @Test
    fun six_choose_four() {
        val actual = 6.choose(4)
        val expected = 15
        assert(actual == expected)
    }

    @Test
    fun four_choose_one() {
        val actual = 4.choose(1)
        val expected = 4
        assert(actual == expected)
    }

    @Test
    fun three_choose_zero() {
        val actual = 3.choose(0)
        val expected = 1
        assert(actual == expected)
    }

    @Test
    fun calculate_temp() {
        val exactChances = calculateExactChances(0.75, 4)
        val atLeastChances = calculateAtLeastChances(exactChances)

        val exactlyZero = 0.00390625
        val atLeastZero = 1.0
        val atLeastOne = 0.99609375
        val atLeastTwo = 0.94921875
        val atLeastThree = 0.73828125
        val atLeastFour = 0.31640625

        assert(exactlyZero == exactChances[0])
        assert(atLeastZero == atLeastChances[0])
        assert(atLeastOne == atLeastChances[1])
        assert(atLeastTwo == atLeastChances[2])
        assert(atLeastThree == atLeastChances[3])
        assert(atLeastFour == atLeastChances[4])
    }
}