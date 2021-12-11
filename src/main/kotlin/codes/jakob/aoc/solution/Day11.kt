package codes.jakob.aoc.solution

import codes.jakob.aoc.shared.Grid

object Day11 : Solution() {
    override fun solvePart1(input: String): Any {
        val grid: Grid<Octopus> = Grid(parseInput(input))
        val octopuses: List<Octopus> = grid.cells.map { it.value }
        return (1..100).sumOf {
            val flashed: Set<Octopus> = simulate(octopuses)
            return@sumOf flashed.count()
        }
    }

    override fun solvePart2(input: String): Any {
        val grid: Grid<Octopus> = Grid(parseInput(input))
        val octopuses: List<Octopus> = grid.cells.map { it.value }
        return generateSequence(1) { it + 1 }.first {
            val flashed: Set<Octopus> = simulate(octopuses)
            return@first flashed.count() == octopuses.count()
        }
    }

    private fun simulate(octopuses: List<Octopus>): Set<Octopus> {
        octopuses.forEach { it.increaseEnergyLevel() }
        val flashed: Set<Octopus> = octopuses.flatMap { it.checkForFlash() }.toSet()
        octopuses.forEach { it.resetFlashStatus() }
        return flashed
    }

    private fun parseInput(input: String): List<List<(Grid.Cell<Octopus>) -> Octopus>> {
        return input.splitMultiline().map { row ->
            row.split("").filter { it.isNotBlank() }.map { value ->
                { cell -> Octopus(cell, value.toInt()) }
            }
        }
    }

    class Octopus(private val cell: Grid.Cell<Octopus>, private var energyLevel: Int) {
        private val willFlash: Boolean get() = energyLevel > 9
        private var didFlash: Boolean = false

        fun checkForFlash(): Set<Octopus> {
            return if (willFlash) flash() else emptySet()
        }

        fun resetFlashStatus() {
            didFlash = false
        }

        fun increaseEnergyLevel(increase: Int = 1) {
            energyLevel += increase
        }

        private fun flash(): Set<Octopus> {
            require(willFlash) { "Octopus $this is not ready to flash" }
            energyLevel = 0
            didFlash = true
            return setOf(this) + cell.getAdjacent(true).flatMap { it.value.adjacentFlashed() }
        }

        private fun adjacentFlashed(): Set<Octopus> {
            if (!didFlash) increaseEnergyLevel(1)
            return if (willFlash) flash() else emptySet()
        }

        override fun toString(): String {
            return "Octopus(energyLevel=$energyLevel)"
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Octopus

            if (cell != other.cell) return false

            return true
        }

        override fun hashCode(): Int {
            return cell.hashCode()
        }
    }
}

fun main() {
    Day11.solve()
}
