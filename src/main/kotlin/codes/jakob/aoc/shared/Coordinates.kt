package codes.jakob.aoc.shared

import kotlin.math.abs

data class Coordinates(
    val x: Int,
    val y: Int,
) {
    fun distanceTo(other: Coordinates, diagonally: Boolean): Int {
        return if (diagonally) {
            0
        } else {
            abs(this.x - other.x) + abs(this.y - other.y)
        }
    }
}
