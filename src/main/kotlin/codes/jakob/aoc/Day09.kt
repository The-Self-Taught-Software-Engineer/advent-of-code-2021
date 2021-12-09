package codes.jakob.aoc

object Day09 : Solution() {
    override fun solvePart1(input: String): Any {
        val grid = Grid(parseInput(input))
        return grid.getLowPoints().sumOf { 1 + it.value }
    }

    override fun solvePart2(input: String): Any {
        val grid = Grid(parseInput(input))

        val basins: MutableMap<Grid.Cell, MutableSet<Grid.Cell>> =
            mutableMapOf<Grid.Cell, MutableSet<Grid.Cell>>().withDefault { mutableSetOf() }
        for (cell: Grid.Cell in grid.cells()) {
            if (cell.value == 9) continue
            val closestLowPoint: Grid.Cell = cell.getClosestLowPoint()
            basins[closestLowPoint] = basins.getValue(closestLowPoint).also { it.add(cell) }
        }

        return basins.values.sortedByDescending { it.count() }.take(3).productOf { it.count() }
    }

    private fun parseInput(input: String): List<List<Int>> {
        return input.splitMultiline().map { row ->
            row.split("").filter { it.isNotBlank() }.map { it.toInt() }
        }
    }

    class Grid(input: List<List<Int>>) {
        private val matrix: List<List<Cell>> = generateMatrix(input)

        fun getAdjacent(x: Int, y: Int, diagonally: Boolean = false): List<Cell> {
            return listOfNotNull(
                matrix.getOrNull(y - 1)?.getOrNull(x),
                if (diagonally) matrix.getOrNull(y - 1)?.getOrNull(x + 1) else null,
                matrix.getOrNull(y)?.getOrNull(x + 1),
                if (diagonally) matrix.getOrNull(y + 1)?.getOrNull(x + 1) else null,
                matrix.getOrNull(y + 1)?.getOrNull(x),
                if (diagonally) matrix.getOrNull(y + 1)?.getOrNull(x - 1) else null,
                matrix.getOrNull(y)?.getOrNull(x - 1),
                if (diagonally) matrix.getOrNull(y - 1)?.getOrNull(x - 1) else null,
            )
        }

        fun cells(): List<Cell> {
            return matrix.flatten()
        }

        private fun generateMatrix(input: List<List<Int>>): List<List<Cell>> {
            return input.mapIndexed { y: Int, row: List<Int> ->
                row.mapIndexed { x: Int, value: Int ->
                    Cell(this, value, x, y)
                }
            }
        }

        data class Cell(
            private val grid: Grid,
            val value: Int,
            val coordinates: Coordinates,
        ) {
            constructor(grid: Grid, value: Int, x: Int, y: Int) : this(grid, value, Coordinates(x, y))

            fun getAdjacent(diagonally: Boolean = false): List<Cell> {
                return grid.getAdjacent(coordinates.x, coordinates.y, diagonally)
            }
        }

        data class Coordinates(
            val x: Int,
            val y: Int,
        )
    }

    private fun Grid.getLowPoints(diagonally: Boolean = false): List<Grid.Cell> {
        return this.cells().map { it.getClosestLowPoint(diagonally) }.distinct()
    }

    private fun Grid.Cell.getClosestLowPoint(diagonally: Boolean = false): Grid.Cell {
        val adjacentCells: List<Grid.Cell> = this.getAdjacent(diagonally)
        return if (adjacentCells.all { this.value < it.value }) {
            this
        } else {
            val localLowPoint: Grid.Cell = adjacentCells.minByOrNull { it.value }!!
            localLowPoint.getClosestLowPoint(diagonally)
        }
    }
}

fun main() {
    Day09.solve()
}
