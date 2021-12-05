package codes.jakob.aoc

import codes.jakob.aoc.Day05.Point.Companion.fromCommaSeparated
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

object Day05 : Solution() {
    private const val CONNECTOR_ARROW = " -> "

    override fun solvePart1(input: String): Any {
        val lines: Collection<Line> = parseLines(input).filterNot { it.isDiagonal() }
        return countOverlappingPoints(lines)
    }

    override fun solvePart2(input: String): Any {
        val lines: Collection<Line> = parseLines(input)
        return countOverlappingPoints(lines)
    }

    private fun countOverlappingPoints(lines: Collection<Line>): Int {
        val points: Collection<Point> = lines.flatMap { it.points }
        val pointsFrequencies: Map<Point, Int> = points.groupBy { it }.mapValues { it.value.count() }
        return pointsFrequencies.values.count { it > 1 }
    }

    private fun parseLines(input: String): List<Line> {
        return input
            .splitMultiline()
            .map { it.split(CONNECTOR_ARROW) }
            .map { (from, to) -> Line(fromCommaSeparated(from), fromCommaSeparated(to)) }
    }

    data class Point(
        val x: Int,
        val y: Int,
    ) {
        companion object {
            fun fromCommaSeparated(input: String): Point {
                val (x: String, y: String) = input.split(",")
                return Point(x.toInt(), y.toInt())
            }
        }
    }

    data class Line(
        val from: Point,
        val to: Point,
    ) {
        private val euclideanDistance: Double = euclideanDistance()
        private val hops: Int = if (isDiagonal()) abs(from.x - to.x) else euclideanDistance.toInt()
        val points: List<Point> by lazy { listOf(from) + pointsInBetween() + listOf(to) }

        fun isDiagonal(): Boolean {
            return !(from.x == to.x || from.y == to.y)
        }

        private fun pointsInBetween(): List<Point> {
            return (1 until hops).map { pointAtDistanceInBetween(it) }
        }

        private fun pointAtDistanceInBetween(hopsFrom: Int): Point {
            val t: Double = (hopsFrom * (euclideanDistance / hops)) / euclideanDistance
            val x: Double = (1 - t) * from.x + t * to.x
            val y: Double = (1 - t) * from.y + t * to.y
            return Point(x.roundToInt(), y.roundToInt())
        }

        private fun euclideanDistance(): Double {
            return sqrt((to.x - from.x).toDouble().pow(2) + (to.y - from.y).toDouble().pow(2))
        }
    }
}

fun main() {
    Day05.solve()
}
