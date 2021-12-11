package codes.jakob.aoc.solution

import codes.jakob.aoc.shared.Grid

object Day09 : Solution() {
    override fun solvePart1(input: String): Any {
        val grid: Grid<Int> = Grid(parseInput(input))
        return grid.getLowPoints().sumOf { 1 + it.value }
    }

    override fun solvePart2(input: String): Any {
        val grid: Grid<Int> = Grid(parseInput(input))
        val basins: Map<Grid.Cell<Int>, Collection<Grid.Cell<Int>>> = grid.cells
            .filterNot { it.value == 9 }
            .groupBy { it.getClosestLowPoint() }
        return basins.values.sortedByDescending { it.count() }.take(3).productOf { it.count() }
    }

    private fun parseInput(input: String): List<List<(Grid.Cell<Int>) -> Int>> {
        return input.splitMultiline().map { row ->
            row.split("").filter { it.isNotBlank() }.map { value ->
                { value.toInt() }
            }
        }
    }

    private fun Grid<Int>.getLowPoints(diagonally: Boolean = false): List<Grid.Cell<Int>> {
        return this.cells.map { it.getClosestLowPoint(diagonally) }.distinct()
    }

    private fun Grid.Cell<Int>.getClosestLowPoint(diagonally: Boolean = false): Grid.Cell<Int> {
        val adjacentCells: List<Grid.Cell<Int>> = this.getAdjacent(diagonally)
        return if (adjacentCells.all { this.value < it.value }) {
            this
        } else {
            val localLowPoint: Grid.Cell<Int> = adjacentCells.minByOrNull { it.value }!!
            localLowPoint.getClosestLowPoint(diagonally)
        }
    }
}

fun main() {
    Day09.solve()
}
