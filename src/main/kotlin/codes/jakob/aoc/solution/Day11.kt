package codes.jakob.aoc.solution

import codes.jakob.aoc.shared.Grid

object Day11 : Solution() {
    override fun solvePart1(input: String): Any {
        val grid: Grid<Octopus> = Grid(parseInput(input)).also { grid -> grid.cells.forEach { it.value.cell = it } }
        return (1..100).sumOf {
            grid.cells.forEach { it.value.energyLevel++ }
            val flashed: Set<Octopus> = getFlashedOctopuses(grid)
            grid.cells.forEach { it.value.resetFlashStatus() }
            return@sumOf flashed.count()
        }
    }

    override fun solvePart2(input: String): Any {
        val grid: Grid<Octopus> = Grid(parseInput(input)).also { grid -> grid.cells.forEach { it.value.cell = it } }
        return generateSequence(1) { it + 1 }.indexOfFirst {
            grid.cells.forEach { it.value.energyLevel++ }
            val flashed: Set<Octopus> = getFlashedOctopuses(grid)
            grid.cells.forEach { it.value.resetFlashStatus() }
            return@indexOfFirst flashed.count() == grid.cells.count()
        } + 1
    }

    private fun getFlashedOctopuses(grid: Grid<Octopus>): Set<Octopus> {
        return grid.cells
            .filter { it.value.willFlash }
            .flatMap { it.value.startFlash() }
            .toSet()
    }

    private fun parseInput(input: String): List<List<Octopus>> {
        return input.splitMultiline().map { row ->
            row.split("").filter { it.isNotBlank() }.map { Octopus(it.toInt()) }
        }
    }

    data class Octopus(
        var energyLevel: Int,
    ) {
        var cell: Grid.Cell<Octopus>? = null
        val willFlash: Boolean get() = energyLevel > 9
        private var didFlash: Boolean = false

        fun startFlash(): Set<Octopus> {
            return if (!didFlash) {
                energyLevel = 0
                didFlash = true
                setOf(this) + cell!!.getAdjacent(true).flatMap { it.value.checkForFlash() }
            } else emptySet()
        }

        fun resetFlashStatus() {
            didFlash = false
        }

        private fun checkForFlash(): Set<Octopus> {
            if (!didFlash) energyLevel++
            return if (willFlash) startFlash() else emptySet()
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
            return cell?.hashCode() ?: 0
        }
    }
}

fun main() {
    Day11.solve()
}
