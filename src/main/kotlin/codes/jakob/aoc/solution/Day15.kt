@file:Suppress("SameParameterValue")

package codes.jakob.aoc.solution

import codes.jakob.aoc.shared.Grid
import codes.jakob.aoc.shared.UndirectedGraph
import codes.jakob.aoc.shared.UndirectedGraph.Vertex

object Day15 : Solution() {
    override fun solvePart1(input: String): Any {
        val grid: Grid<Int> = Grid(parseInput(input))
        val shortestPath: List<Vertex<Grid.Cell<Int>>> = findShortestPath(grid)
        return shortestPath.map { it.value }.drop(1).sumOf { it.value }
    }

    override fun solvePart2(input: String): Any {
        val grid: Grid<Int> = enlargeGrid(Grid(parseInput(input)), 5)
        val shortestPath: List<Vertex<Grid.Cell<Int>>> = findShortestPath(grid)
        return shortestPath.map { it.value }.drop(1).sumOf { it.value }
    }

    private fun findShortestPath(grid: Grid<Int>): List<Vertex<Grid.Cell<Int>>> {
        val start: Grid.Cell<Int> = grid.matrix.first().first()
        val end: Grid.Cell<Int> = grid.matrix.last().last()

        val edges: List<Pair<Grid.Cell<Int>, Grid.Cell<Int>>> =
            grid.cells.flatMap { cell: Grid.Cell<Int> -> cell.getAdjacent(false).map { cell to it } }
        val graph: UndirectedGraph<Grid.Cell<Int>> = UndirectedGraph(edges)

        return graph.findBestPath(
            graph.vertices.first { it.value == start },
            graph.vertices.first { it.value == end },
            { cell: Grid.Cell<Int> -> cell.distanceTo(end).toLong() },
            { _, adjacent: Grid.Cell<Int> -> adjacent.value.toLong() },
        )
    }

    private fun enlargeGrid(grid: Grid<Int>, times: Int): Grid<Int> {
        val height: Int = grid.matrix.count()
        val width: Int = grid.matrix.first().count()
        val input: List<List<(Grid.Cell<Int>) -> Int>> =
            List(height * times) { y: Int ->
                List(width * times) { x: Int ->
                    val down: Int = y / height
                    val right: Int = x / width
                    val originalY: Int = y % height
                    val originalX: Int = x % width
                    var value = grid.matrix[originalY][originalX].value + down + right
                    if (value != 9) value %= 9
                    { value }
                }
            }
        return Grid(input)
    }

    private fun parseInput(input: String): List<List<(Grid.Cell<Int>) -> Int>> {
        return input.splitMultiline().map { row: String ->
            row.split("").filter { it.isNotBlank() }.map { value: String ->
                { value.toInt() }
            }
        }
    }
}

fun main() {
    Day15.solve()
}
