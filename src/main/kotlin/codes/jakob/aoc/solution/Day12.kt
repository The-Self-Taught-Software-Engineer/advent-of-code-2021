package codes.jakob.aoc.solution

import codes.jakob.aoc.shared.UndirectedGraph
import codes.jakob.aoc.shared.UndirectedGraph.Vertex

object Day12 : Solution() {
    override fun solvePart1(input: String): Any {
        val graph: UndirectedGraph<Cave> = UndirectedGraph(parseInput(input))
        val paths: Set<List<Vertex<Cave>>> = findAllPaths(graph) { vertex, visited ->
            vertex !in visited || vertex.value.size == Cave.Size.BIG
        }
        return paths.count()
    }

    override fun solvePart2(input: String): Any {
        val graph: UndirectedGraph<Cave> = UndirectedGraph(parseInput(input))
        val paths: Set<List<Vertex<Cave>>> = findAllPaths(graph) { vertex, visited ->
            val smallCaveVisitedFrequencies: Map<Vertex<Cave>, Int> =
                visited.filter { it.value.size == Cave.Size.SMALL }.countBy { it }
            val canVisitSmallCaveAgain: Boolean =
                smallCaveVisitedFrequencies.none { it.value > 1 } && (!vertex.value.isStart && !vertex.value.isEnd)
            return@findAllPaths vertex !in visited || vertex.value.size == Cave.Size.BIG || canVisitSmallCaveAgain
        }
        return paths.count()
    }

    private fun findAllPaths(
        graph: UndirectedGraph<Cave>,
        canVisit: (Vertex<Cave>, Collection<Vertex<Cave>>) -> Boolean,
    ): Set<List<Vertex<Cave>>> {
        val start: Vertex<Cave> = graph.vertices.first { it.value.isStart }
        val end: Vertex<Cave> = graph.vertices.first { it.value.isEnd }
        return graph.findAllPaths(start, end, canVisit)
    }

    private fun parseInput(input: String): List<Pair<Cave, Cave>> {
        return input
            .splitMultiline()
            .map { line: String ->
                val (from: String, to: String) = line.split("-")
                return@map Cave(from) to Cave(to)
            }
    }

    data class Cave(
        val id: String,
        val size: Size = if (id.all { it.isUpperCase() }) Size.BIG else Size.SMALL,
        val isStart: Boolean = id == "start",
        val isEnd: Boolean = id == "end",
    ) {
        enum class Size {
            SMALL,
            BIG;
        }
    }
}

fun main() {
    Day12.solve()
}
