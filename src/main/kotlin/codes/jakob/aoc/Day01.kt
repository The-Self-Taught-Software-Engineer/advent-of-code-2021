package codes.jakob.aoc

object Day01 : Solution() {
    override val identifier: String = this::class.simpleName.toString()

    override fun solvePart1(input: String): String {
        val measurements: List<Int> = splitMultilineInput(input).map { it.toInt() }
        return countIncreases(measurements).toString()
    }

    override fun solvePart2(input: String): String {
        val measurements: List<Int> = splitMultilineInput(input).map { it.toInt() }
        val windows: List<Int> = measurements.windowed(3).map { it.sum() }
        return countIncreases(windows).toString()
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
