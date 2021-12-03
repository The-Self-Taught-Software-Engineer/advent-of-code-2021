package codes.jakob.aoc

object Day03 : Solution() {
    override fun solvePart1(input: String): Any {
        val rows: List<List<Int>> = convertToBitString(input)

        val mostCommonBits: List<Int> = rows.first().indices.map { findMostCommonBit(rows, it) }
        val leastCommonBits: List<Int> = mostCommonBits.map { it.bitFlip() }

        val gammaRate: Int = mostCommonBits.binaryToDecimal()
        val epsilonRate: Int = leastCommonBits.binaryToDecimal()

        return gammaRate * epsilonRate
    }

    override fun solvePart2(input: String): Any {
        val rows: List<List<Int>> = convertToBitString(input)

        val oxygenRating: Int = calculateRating(rows, this::findMostCommonBit)
        val co2ScrubberRating: Int = calculateRating(rows, this::findLeastCommonBit)

        return oxygenRating * co2ScrubberRating
    }

    private fun convertToBitString(input: String): List<List<Int>> {
        return input
            .splitMultiline()
            .map { row -> row.split("").filter { it.isNotBlank() }.map { it.toInt() } }
    }

    private fun calculateRating(rows: List<List<Int>>, bitFinder: (List<List<Int>>, Int) -> Int): Int {
        var matches: List<List<Int>> = rows
        for (column: Int in rows.first().indices) {
            val bit: Int = bitFinder(matches, column)
            matches = matches.filterNot { row -> row[column] != bit }
            if (matches.count() == 1) break
        }
        require(matches.count() == 1) { "Expected exactly one match to be left over" }
        return matches.first().binaryToDecimal()
    }

    private fun findMostCommonBit(rows: List<List<Int>>, column: Int, equallyCommonDefault: Int = 1): Int {
        val meanBit: Double = rows.sumOf { it[column] } / rows.count().toDouble()
        return if (meanBit == 0.5) equallyCommonDefault else if (meanBit > 0.5) 1 else 0
    }

    private fun findLeastCommonBit(rows: List<List<Int>>, column: Int, equallyCommonDefault: Int = 0): Int {
        val meanBit: Double = rows.sumOf { it[column] } / rows.count().toDouble()
        if (meanBit == 0.5) {
            return equallyCommonDefault
        }
        val mostCommonBit: Int = if (meanBit > 0.5) 1 else 0
        return mostCommonBit.bitFlip()
    }
}

fun main() {
    Day03.solve()
}
