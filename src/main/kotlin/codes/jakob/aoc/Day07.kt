package codes.jakob.aoc

import kotlin.math.abs

object Day07 : Solution() {
    override fun solvePart1(input: String): Any {
        val crabPositions: List<Int> = parseInput(input)
        val fuelPerPosition: Map<Int, Long> = calculateFuelPerPosition(crabPositions) { position, crabPosition ->
            abs(position - crabPosition).toLong()
        }
        return fuelPerPosition.minOf { it.value }
    }

    override fun solvePart2(input: String): Any {
        val crabPositions: List<Int> = parseInput(input)
        val fuelPerPosition: Map<Int, Long> = calculateFuelPerPosition(crabPositions) { position, crabPosition ->
            abs(position - crabPosition).toLong().triangular()
        }
        return fuelPerPosition.minOf { it.value }
    }

    private fun calculateFuelPerPosition(
        crabsPositions: List<Int>,
        fuelEquation: (Int, Int) -> Long
    ): Map<Int, Long> {
        val minPosition: Int = crabsPositions.minOf { it }
        val maxPosition: Int = crabsPositions.maxOf { it }
        return (minPosition..maxPosition).associateWith { position ->
            crabsPositions.sumOf { crabPosition ->
                fuelEquation(position, crabPosition)
            }
        }
    }

    private fun parseInput(input: String): List<Int> {
        return input.split(",").map { it.toInt() }
    }
}

fun main() {
    Day07.solve()
}
