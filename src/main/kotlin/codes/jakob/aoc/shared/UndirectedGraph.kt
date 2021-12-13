package codes.jakob.aoc.shared

class UndirectedGraph<T>(input: List<Pair<T, T>>) {
    private val edges: Set<UndirectedEdge<T>> = buildEdges(input)
    val vertices: Set<Vertex<T>> = edges.flatMap { setOf(it.a, it.b) }.toSet()

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
            visited.add(current)
            findAllPaths(from, to, visited, allPaths, canVisit)
            visited.removeLast()
        }
    }

    private fun getAdjacent(vertex: Vertex<T>): Set<Vertex<T>> {
        return (edges.filter { it.a == vertex }.map { it.b } + edges.filter { it.b == vertex }.map { it.a }).toSet()
    }

    private fun buildEdges(input: List<Pair<T, T>>): Set<UndirectedEdge<T>> {
        return input.map { (from: T, to: T) -> UndirectedEdge(Vertex(from), Vertex(to)) }.toSet()
    }

    data class Vertex<T>(
        val value: T
    )

    data class UndirectedEdge<T>(
        val a: Vertex<T>,
        val b: Vertex<T>,
    )
}
