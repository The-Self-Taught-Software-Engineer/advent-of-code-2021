package codes.jakob.aoc

import kotlin.math.abs

object Day07 : Solution() {
    override fun solvePart1(input: String): Any {
        val crabPositions: List<Int> = parseInput(input)
        val fuelPerPosition: Map<Int, Long> = calculateFuelConsumptionPerPosition(crabPositions) { start, end ->
            abs(start - end).toLong()
        }
        return fuelPerPosition.minOf { it.value }
    }

    override fun solvePart2(input: String): Any {
        val crabPositions: List<Int> = parseInput(input)
        val fuelPerPosition: Map<Int, Long> = calculateFuelConsumptionPerPosition(crabPositions) { start, end ->
            abs(start - end).toLong().triangular()
        }
        return fuelPerPosition.minOf { it.value }
    }

    /**
     * Returns the total fuel consumption per position in [startingPositions] to move each entity to each of those.
     *
     * @param fuelEquation A lambda receiving the start and the end position as the arguments,
     * returning how much fuel this move would consume.
     */
    private fun calculateFuelConsumptionPerPosition(
        startingPositions: List<Int>,
        fuelEquation: (Int, Int) -> Long,
    ): Map<Int, Long> {
        val minPosition: Int = startingPositions.minOf { it }
        val maxPosition: Int = startingPositions.maxOf { it }
        return (minPosition..maxPosition).associateWith { end ->
            startingPositions.sumOf { start ->
                fuelEquation(start, end)
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
