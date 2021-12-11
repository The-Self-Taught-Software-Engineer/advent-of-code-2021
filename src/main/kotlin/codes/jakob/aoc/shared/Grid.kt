package codes.jakob.aoc.shared

class Grid<T>(input: List<List<(Cell<T>) -> T>>) {
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

    private fun generateMatrix(input: List<List<(Cell<T>) -> T>>): List<List<Cell<T>>> {
        return input.mapIndexed { y: Int, row: List<(Cell<T>) -> T> ->
            row.mapIndexed { x: Int, valueConstructor: (Cell<T>) -> T ->
                Cell(this, x, y, valueConstructor)
            }
        }
    }

    class Cell<T>(
        private val grid: Grid<T>,
        val coordinates: Coordinates,
        valueConstructor: (Cell<T>) -> T,
    ) {
        constructor(
            grid: Grid<T>,
            x: Int,
            y: Int,
            valueConstructor: (Cell<T>) -> T,
        ) : this(grid, Coordinates(x, y), valueConstructor)

        val value: T = valueConstructor(this)

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
