package codes.jakob.aoc

object Day01 : Solution() {
    override fun solvePart1(input: String): Any {
        val measurements: List<Int> = input.splitMultiline().map { it.toInt() }
        return countIncreases(measurements)
    }

    override fun solvePart2(input: String): Any {
        val measurements: List<Int> = input.splitMultiline().map { it.toInt() }
        val windows: List<Int> = measurements.windowed(3).map { it.sum() }
        return countIncreases(windows)
    }

    private fun countIncreases(values: List<Int>): Int {
        var counter = 0
        var previousValue: Int = Int.MAX_VALUE
        for (value: Int in values) {
            if (value > previousValue) counter++
            previousValue = value
        }
        return counter
    }
}

fun main() {
    Day01.solve()
}
