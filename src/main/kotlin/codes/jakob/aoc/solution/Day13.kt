package codes.jakob.aoc.solution

import codes.jakob.aoc.shared.Coordinates
import codes.jakob.aoc.shared.Grid

object Day13 : Solution() {
    private val DEFAULT_CELL_VALUE: (Grid.Cell<Boolean>) -> Boolean = { false }

    override fun solvePart1(input: String): Any {
        val foldedGrid: Grid<Boolean> = foldOver(input, 1)
        return foldedGrid.cells.count { it.value }
    }

    override fun solvePart2(input: String): Any {
        val foldedGrid: Grid<Boolean> = foldOver(input)
        return "\n" + foldedGrid.toPrettyString()
    }

    private fun foldOver(input: String, foldFirstNOnly: Int? = null): Grid<Boolean> {
        val (coordinates: List<Coordinates>, instructions: List<FoldInstruction>) = parseInput(input)
        val coordinateValues: Map<Coordinates, (Grid.Cell<Boolean>) -> Boolean> = coordinates.associateWith { { true } }
        val grid: Grid<Boolean> = Grid(coordinateValues, DEFAULT_CELL_VALUE)
        return instructions
            .take(foldFirstNOnly ?: instructions.count())
            .fold(grid) { acc: Grid<Boolean>, fi: FoldInstruction -> acc.foldOver(fi) }
    }

    private fun parseInput(input: String): Pair<List<Coordinates>, List<FoldInstruction>> {
        val (coordinateLines: String, instructionLines: String) = input.split("\n\n")
        val coordinates: List<Coordinates> = coordinateLines.splitMultiline().map { line: String ->
            val (x: String, y: String) = line.split(",")
            Coordinates(x.toInt(), y.toInt())
        }
        val instructions: List<FoldInstruction> = instructionLines.splitMultiline().map { line: String ->
            val (axis: String, value: String) = line.split("fold along ").last().split("=")
            FoldInstruction(FoldInstruction.Axis.valueOf(axis.uppercase()), value.toInt())
        }
        return coordinates to instructions
    }

    data class FoldInstruction(
        val axis: Axis,
        val value: Int,
    ) {
        val linesMerged: Set<Pair<Int, Int>> = linesMerged()

        private fun linesMerged(): Set<Pair<Int, Int>> {
            return (1..value).map { distance: Int ->
                (value - distance) to (value + distance)
            }.toSet()
        }

        enum class Axis {
            X,
            Y;
        }
    }

    @Suppress("UNUSED_ANONYMOUS_PARAMETER")
    private fun Grid<Boolean>.foldOver(instruction: FoldInstruction): Grid<Boolean> {
        val (matrix: List<List<Grid.Cell<Boolean>>>, coordinatesConstructor: (Int, Int) -> Coordinates) =
            when (instruction.axis) {
                FoldInstruction.Axis.X -> this.matrix to { x: Int, y: Int -> Coordinates(x, y) }
                FoldInstruction.Axis.Y -> this.matrix.transpose() to { x: Int, y: Int -> Coordinates(y, x) }
            }
        val coordinateValues: Map<Coordinates, (Grid.Cell<Boolean>) -> Boolean> = matrix
            .mapIndexed { y: Int, row: List<Grid.Cell<Boolean>> ->
                instruction.linesMerged.map { (xRetained: Int, xDiscarded: Int) ->
                    val coordinates: Coordinates = coordinatesConstructor(xRetained, y)
                    val value: Boolean =
                        row.getOrNull(xRetained)?.value ?: false || row.getOrNull(xDiscarded)?.value ?: false
                    return@map coordinates to value
                }
            }
            .flatten()
            .associate { (coordinates: Coordinates, value: Boolean) -> coordinates to { cell: Grid.Cell<Boolean> -> value } }
        return Grid(coordinateValues, DEFAULT_CELL_VALUE)
    }

    private fun Grid<Boolean>.toPrettyString(): String {
        return this.matrix.joinToString("\n") { row ->
            row.joinToString("") { cell ->
                if (cell.value) "█" else "░"
            }
        }
    }
}

fun main() {
    Day13.solve()
}
