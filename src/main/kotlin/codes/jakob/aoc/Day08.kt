package codes.jakob.aoc

import codes.jakob.aoc.Day08.Digit.*

object Day08 : Solution() {
    override fun solvePart1(input: String): Any {
        val wantedDigits: Set<Digit> = setOf(ONE, FOUR, SEVEN, EIGHT)
        return parseInput(input).sumOf { entry: Entry ->
            val mappings: Map<Pattern, Digit> = processEntry(entry.inputs).reversed()
            return@sumOf entry.outputs.map { mappings[it]!! }.count { it in wantedDigits }
        }
    }

    override fun solvePart2(input: String): Any {
        return parseInput(input).sumOf { entry: Entry ->
            val mappings: Map<Pattern, Digit> = processEntry(entry.inputs).reversed()
            return@sumOf entry.outputs.map { mappings[it]!!.value }.joinToString("").toInt()
        }
    }

    private fun processEntry(patterns: List<Pattern>): Map<Digit, Pattern> {
        val mappings: MutableMap<Digit, Pattern> = mutableMapOf()

        for (inputPattern: Pattern in patterns) {
            val digit: Digit = when (inputPattern.segments.count()) {
                2 -> ONE
                3 -> SEVEN
                4 -> FOUR
                7 -> EIGHT
                else -> continue
            }
            mappings[digit] = inputPattern
        }

        for (inputPattern: Pattern in patterns.filterNot { it in mappings.values }) {
            val digit: Digit = values().first { inputPattern.isIt(it, mappings) }
            mappings[digit] = inputPattern
        }

        return mappings
    }

    private fun parseInput(input: String): List<Entry> {
        return input.splitMultiline().map { entry ->
            val (inputs: String, outputs: String) = entry.split(" | ")
            return@map Entry(parseSignals(inputs), parseSignals(outputs))
        }
    }

    private fun parseSignals(signals: String): List<Pattern> {
        return signals
            .split(" ")
            .filter { it.isNotBlank() }
            .map { signal ->
                signal
                    .split("")
                    .filter { it.isNotBlank() }
                    .map { it.toCharArray().first() }
                    .let { Pattern.fromSymbols(it) }
            }
    }

    data class Entry(
        val inputs: List<Pattern>,
        val outputs: List<Pattern>,
    )

    data class Pattern(
        val segments: Set<Segment>,
    ) {
        constructor(segments: Collection<Segment>) : this(segments.toSet())

        fun isIt(digit: Digit, mappings: Map<Digit, Pattern>): Boolean {
            return when (digit) {
                ZERO -> count() == 6 && !isIt(SIX, mappings) && !isIt(NINE, mappings)
                ONE -> count() == 2
                TWO -> count() == 5 && !isIt(THREE, mappings) && !isIt(FIVE, mappings)
                THREE -> count() == 5 && intersect(mappings[ONE]!!).count() == 2
                FOUR -> count() == 4
                FIVE -> count() == 5 && !isIt(THREE, mappings) && intersect(mappings[FOUR]!!).count() == 3
                SIX -> count() == 6 && intersect(mappings[SEVEN]!!).count() == 2
                SEVEN -> count() == 3
                EIGHT -> count() == 7
                NINE -> count() == 6 && intersect(mappings[FOUR]!!).count() == 4
            }
        }

        private fun count(): Int = this.segments.count()

        private fun intersect(other: Pattern): Set<Segment> {
            return this.segments.intersect(other.segments)
        }

        companion object {
            fun fromSymbols(symbols: Collection<Char>): Pattern {
                return Pattern(symbols.map { Segment.fromSymbol(it) ?: error("No Segment exists for symbol: $it") })
            }
        }
    }

    enum class Digit(val value: Int) {
        ZERO(0),
        ONE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7),
        EIGHT(8),
        NINE(9);
    }

    enum class Segment(val symbol: Char) {
        A('a'),
        B('b'),
        C('c'),
        D('d'),
        E('e'),
        F('f'),
        G('g');

        companion object {
            fun fromSymbol(symbol: Char): Segment? {
                return values().find { it.symbol == symbol }
            }
        }
    }
}

fun main() {
    Day08.solve()
}
