package codes.jakob.aoc.shared

class Grid<T>(input: List<List<T>>) {
    private val matrix: List<List<Cell<T>>> = generateMatrix(input)
    val cells: List<Cell<T>> = matrix.flatten()

    fun getAdjacent(x: Int, y: Int, diagonally: Boolean = false): List<Cell<T>> {
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

    private fun generateMatrix(input: List<List<T>>): List<List<Cell<T>>> {
        return input.mapIndexed { y: Int, row: List<T> ->
            row.mapIndexed { x: Int, value: T ->
                Cell(this, value, x, y)
            }
        }
    }

    data class Cell<T>(
        private val grid: Grid<T>,
        val value: T,
        val coordinates: Coordinates,
    ) {
        constructor(grid: Grid<T>, value: T, x: Int, y: Int) : this(grid, value, Coordinates(x, y))

        fun getAdjacent(diagonally: Boolean = false): List<Cell<T>> {
            return grid.getAdjacent(coordinates.x, coordinates.y, diagonally)
        }

        override fun toString(): String {
            return "Cell(value=$value, coordinates=$coordinates)"
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Cell<*>

            if (grid != other.grid) return false
            if (coordinates != other.coordinates) return false

            return true
        }

        override fun hashCode(): Int {
            var result: Int = grid.hashCode()
            result = 31 * result + coordinates.hashCode()
            return result
        }
    }

    data class Coordinates(
        val x: Int,
        val y: Int,
    )
}
