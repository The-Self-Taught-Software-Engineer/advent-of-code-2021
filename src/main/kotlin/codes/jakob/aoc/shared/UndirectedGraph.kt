package codes.jakob.aoc.shared

import java.util.*

class UndirectedGraph<T>(input: List<Pair<T, T>>) {
    private val edges: Set<UndirectedEdge<T>> = buildEdges(input)
    private val adjacentVertices: Map<Vertex<T>, Set<Vertex<T>>> = buildAdjacentVertices(edges)
    val vertices: Set<Vertex<T>> = edges.flatMap { setOf(it.a, it.b) }.toSet()

    fun findBestPath(
        from: Vertex<T>,
        to: Vertex<T>,
        heuristic: (T) -> Long,
        cost: (T, T) -> Long,
    ): List<Vertex<T>> {
        require(from in vertices && to in vertices) { "The given start and end points are unknown" }

        // For node n, gScores[n] is the cost of the cheapest path from start to n currently known.
        val gScores: MutableMap<Vertex<T>, Long> =
            mutableMapOf<Vertex<T>, Long>().withDefault { Long.MAX_VALUE }.also { it[from] = 0L }
        // For node n, fScores[n] == gScores[n] + heuristic(n). fScores[n] represents our current best guess as to
        // how short a path from start to finish can be if it goes through n.
        val fScores: MutableMap<Vertex<T>, Long> =
            mutableMapOf<Vertex<T>, Long>().withDefault { Long.MAX_VALUE }.also { it[from] = heuristic(from.value) }
        // The priority queue of discovered nodes that may need to be (re-)expanded.
        val comparator = Comparator { o1: Vertex<T>, o2: Vertex<T> ->
            fScores.getValue(o1).compareTo(fScores.getValue(o2))
        }
        val open: PriorityQueue<Vertex<T>> = PriorityQueue(comparator).also { it.add(from) }
        // For node n, cameFrom[n] is the node immediately preceding it on the cheapest path from start
        // to n currently known.
        val cameFrom: MutableMap<Vertex<T>, Vertex<T>> = mutableMapOf()

        while (open.isNotEmpty()) {
            val current: Vertex<T> = open.remove()
            if (current == to) return reconstructPath(current, cameFrom)
            for (adjacent: Vertex<T> in getAdjacent(current)) {
                // gScore is the distance from start to the adjacent through current
                val gScore: Long = gScores.getValue(current) + cost(current.value, adjacent.value)
                if (gScore < gScores.getValue(adjacent)) {
                    cameFrom[adjacent] = current
                    gScores[adjacent] = gScore
                    fScores[adjacent] = gScore + heuristic(adjacent.value)
                    if (adjacent !in open) open.add(adjacent)
                }
            }
        }

        error("There is no path between $from and $to")
    }

    private fun reconstructPath(last: Vertex<T>, cameFrom: Map<Vertex<T>, Vertex<T>>): List<Vertex<T>> {
        val path: MutableList<Vertex<T>> = mutableListOf(last)
        var current: Vertex<T> = last
        while (current in cameFrom.keys) {
            current = cameFrom[current]!!
            path.add(current)
        }
        return path.reversed()
    }

    fun findAllPaths(
        from: Vertex<T>,
        to: Vertex<T>,
        canVisit: (Vertex<T>, Collection<Vertex<T>>) -> Boolean,
    ): Set<List<Vertex<T>>> {
        require(from in vertices && to in vertices) { "The given start and end points are unknown" }

        val allPaths: MutableSet<List<Vertex<T>>> = mutableSetOf()
        val visited: MutableList<Vertex<T>> = mutableListOf<Vertex<T>>().also { it.add(from) }
        findAllPaths(from, to, visited, allPaths, canVisit)

        return allPaths
    }

    private fun findAllPaths(
        from: Vertex<T>,
        to: Vertex<T>,
        visited: MutableList<Vertex<T>>,
        allPaths: MutableSet<List<Vertex<T>>>,
        canVisit: (Vertex<T>, Collection<Vertex<T>>) -> Boolean,
    ) {
        val visitedFiltered: Collection<Vertex<T>> = visited.filterNot { canVisit(it, visited) }
        val vertices: Collection<Vertex<T>> = getAdjacent(visited.last()).filterNot { it in visitedFiltered }

        if (to in vertices) {
            visited.add(to)
            allPaths.add(visited.toList())
            visited.removeLast()
        }

        for (current: Vertex<T> in vertices) {
            if (current == to) continue
            visited.add(current)
            findAllPaths(from, to, visited, allPaths, canVisit)
            visited.removeLast()
        }
    }

    private fun getAdjacent(vertex: Vertex<T>): Set<Vertex<T>> {
        return adjacentVertices[vertex]!!
    }

    private fun buildEdges(input: List<Pair<T, T>>): Set<UndirectedEdge<T>> {
        return input.map { (from: T, to: T) -> UndirectedEdge(Vertex(from), Vertex(to)) }.toSet()
    }

    private fun buildAdjacentVertices(edges: Set<UndirectedEdge<T>>): Map<Vertex<T>, Set<Vertex<T>>> {
        val adjacentVertices: MutableMap<Vertex<T>, MutableSet<Vertex<T>>> = mutableMapOf()
        for (edge in edges) {
            adjacentVertices.putIfAbsent(edge.a, mutableSetOf())
            adjacentVertices.getValue(edge.a).add(edge.b)
            adjacentVertices.putIfAbsent(edge.b, mutableSetOf())
            adjacentVertices.getValue(edge.b).add(edge.a)
        }
        return adjacentVertices
    }

    data class Vertex<T>(
        val value: T
    )

    data class UndirectedEdge<T>(
        val a: Vertex<T>,
        val b: Vertex<T>,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as UndirectedEdge<*>

            if (a != other.a) return false
            if (b != other.b) return false
            if (a != other.b) return false
            if (b != other.a) return false

            return true
        }

        override fun hashCode(): Int {
            return 31 * (a.hashCode() + b.hashCode())
        }
    }
}
