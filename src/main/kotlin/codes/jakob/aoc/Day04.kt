package codes.jakob.aoc

object Day04 : Solution() {
    override fun solvePart1(input: String): Any {
        val lines: List<String> = input.split("\n\n")

        val numberDrawer: ListIterator<Int> = lines.first().split(",").map { it.toInt() }.listIterator()
        val boards: Collection<BingoBoard> = parseBoards(lines.drop(1))

        while (numberDrawer.hasNext()) {
            val drawnNumber: Int = numberDrawer.next()

            val winnerGrouping: Map<Boolean, List<BingoBoard>> = boards.groupBy { it.markNumber(drawnNumber) }
            if (winnerGrouping.containsKey(true)) {
                val winnerGroup: List<BingoBoard> = winnerGrouping[true]!!
                require(winnerGroup.count() == 1) { "Expected only a single winner board" }
                val winnerBoard: BingoBoard = winnerGroup.first()

                val allUnmarkedNumbers: Collection<Int> = winnerBoard.getAllUnmarkedNumbers()
                return allUnmarkedNumbers.sum() * drawnNumber
            }
        }

        throw IllegalStateException("No winning board was found")
    }

    override fun solvePart2(input: String): Any {
        val lines: List<String> = input.split("\n\n")

        val numberDrawer: ListIterator<Int> = lines.first().split(",").map { it.toInt() }.listIterator()
        val boards: MutableList<BingoBoard> = parseBoards(lines.drop(1)).toMutableList()

        while (numberDrawer.hasNext()) {
            val drawnNumber: Int = numberDrawer.next()

            val winnerGrouping: Map<Boolean, List<BingoBoard>> = boards.groupBy { it.markNumber(drawnNumber) }
            if (winnerGrouping.containsKey(true)) {
                val winnerGroup: List<BingoBoard> = winnerGrouping[true]!!
                boards.removeAll(winnerGroup)

                if (boards.isEmpty()) {
                    val lastBoardToWin: BingoBoard = winnerGroup.first()
                    val allUnmarkedNumbers: Collection<Int> = lastBoardToWin.getAllUnmarkedNumbers()
                    return allUnmarkedNumbers.sum() * drawnNumber
                }
            }
        }

        throw IllegalStateException("No last winning board was found")
    }

    private fun parseBoards(boards: List<String>): Collection<BingoBoard> {
        return boards.map { board ->
            BingoBoard(
                board.split("\n").map { row ->
                    row.split(" ").filter { it.isNotBlank() }.map { it.toInt() }
                }
            )
        }
    }

    class BingoBoard(grid: List<List<Int>>) {
        private val board: List<List<Cell>> = grid.map { row -> row.map { Cell(it) } }
        private val transposedBoard: List<List<Cell>> = board.transpose()
        private val numbersMap: Map<Int, Cell> = board.flatten().associateBy { it.value }

        /**
         * Returns if the given [value] is in any [Cell] on the [BingoBoard].
         */
        operator fun contains(value: Int): Boolean = numbersMap.containsKey(value)

        /**
         * Marks the given [number] if it exists on the board.
         *
         * @return A boolean signifying if the [BingoBoard] won.
         */
        fun markNumber(number: Int): Boolean {
            if (number !in this) {
                return false
            }
            numbersMap[number]!!.mark()
            return checkIfWon()
        }

        fun getAllUnmarkedNumbers(): Collection<Int> {
            return board.flatten().filterNot { it.isMarked }.map { it.value }
        }

        /**
         * Checks if there is either a row or a column on the board that has all marked numbers.
         * Note that this method can short-circuit in terms of both checking within a row or column, and between them.
         */
        private fun checkIfWon(): Boolean {
            return (board.asSequence().map { row -> row.all { it.isMarked } }.any { it }
                    || transposedBoard.asSequence().map { column -> column.all { it.isMarked } }.any { it })
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as BingoBoard

            if (board != other.board) return false

            return true
        }

        override fun hashCode(): Int {
            return board.hashCode()
        }

        private data class Cell(
            val value: Int,
            var isMarked: Boolean = false,
        ) {
            fun mark() {
                isMarked = true
            }
        }
    }
}

fun main() {
    Day04.solve()
}
