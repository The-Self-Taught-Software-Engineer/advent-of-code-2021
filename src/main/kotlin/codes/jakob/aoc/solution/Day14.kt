package codes.jakob.aoc.solution

object Day14 : Solution() {
    override fun solvePart1(input: String): Any {
        val (polymerTemplate: List<Char>, elementInsertionRules: Map<Pair<Char, Char>, Char>) = parseInput(input)
        val processedElementPairFrequencies: Map<Pair<Char, Char>, ULong> =
            processPolymerTemplate(polymerTemplate, elementInsertionRules, 10)
        return calculateMostLeastCommonDifference(processedElementPairFrequencies)
    }

    override fun solvePart2(input: String): Any {
        val (polymerTemplate: List<Char>, pairInsertionRules: Map<Pair<Char, Char>, Char>) = parseInput(input)
        val processedElementPairFrequencies: Map<Pair<Char, Char>, ULong> =
            processPolymerTemplate(polymerTemplate, pairInsertionRules, 40)
        return calculateMostLeastCommonDifference(processedElementPairFrequencies)
    }

    private fun processPolymerTemplate(
        polymerTemplate: List<Char>,
        elementInsertionRules: Map<Pair<Char, Char>, Char>,
        times: Int,
    ): Map<Pair<Char, Char>, ULong> {
        val polymerTemplatePairs: List<Pair<Char, Char>> =
            polymerTemplate.windowed(2).map { it.first() to it.last() }
        val elementPairCounts: Map<Pair<Char, Char>, ULong> =
            polymerTemplatePairs.countBy { it }.mapValues { it.value.toULong() }

        val processedElementPairCounts: MutableMap<Pair<Char, Char>, ULong> =
            (1..times).fold(elementPairCounts) { accumulator: Map<Pair<Char, Char>, ULong>, _ ->
                val newElementPairCounts: MutableMap<Pair<Char, Char>, ULong> =
                    mutableMapOf<Pair<Char, Char>, ULong>().withDefault { 0UL }

                for ((elementPair: Pair<Char, Char>, count: ULong) in accumulator.entries) {
                    val insertElement: Char = elementInsertionRules[elementPair] ?: continue

                    val firstInsertion: Pair<Char, Char> = Pair(elementPair.first, insertElement)
                    val secondInsertion: Pair<Char, Char> = Pair(insertElement, elementPair.second)

                    newElementPairCounts[firstInsertion] = newElementPairCounts.getValue(firstInsertion) + count
                    newElementPairCounts[secondInsertion] = newElementPairCounts.getValue(secondInsertion) + count
                }

                return@fold newElementPairCounts
            }.toMutableMap()
        // The count of the last element pair of the polymer template is off-by-one:
        processedElementPairCounts[polymerTemplatePairs.last()] =
            (processedElementPairCounts[polymerTemplatePairs.last()] ?: 0UL) + 1UL

        return processedElementPairCounts
    }

    private fun calculateMostLeastCommonDifference(elementPairCounts: Map<Pair<Char, Char>, ULong>): ULong {
        val elementCounts: Map<Char, ULong> = elementPairCounts.entries
            .fold(mapOf()) { accumulator: Map<Char, ULong>, entry: Map.Entry<Pair<Char, Char>, ULong> ->
                val frequencies: MutableMap<Char, ULong> = accumulator.toMutableMap().withDefault { 0UL }

                val (firstElement: Char, secondElement: Char) = entry.key

                frequencies[firstElement] = frequencies.getValue(firstElement) + entry.value
                frequencies[secondElement] = frequencies.getValue(secondElement) + entry.value

                return@fold frequencies
            }
            .mapValues { it.value / 2UL }

        val mostCommonElement: Map.Entry<Char, ULong> = elementCounts.maxByOrNull { it.value }!!
        val leastCommonElement: Map.Entry<Char, ULong> = elementCounts.minByOrNull { it.value }!!

        return (mostCommonElement.value - leastCommonElement.value)
    }

    private fun parseInput(input: String): Pair<List<Char>, Map<Pair<Char, Char>, Char>> {
        val (templateLine: String, rulesLines: String) = input.split("\n\n")

        val template: List<Char> = templateLine.split("").filter { it.isNotBlank() }.map { it.toSingleChar() }
        val rules: Map<Pair<Char, Char>, Char> = rulesLines.splitMultiline().map { rule ->
            val (pair: String, result: String) = rule.split(" -> ")
            val (first: String, second: String) = pair.split("").filter { it.isNotBlank() }
            return@map Pair(first.toSingleChar(), second.toSingleChar()) to result.toSingleChar()
        }.toMap()

        return template to rules
    }
}

fun main() {
    Day14.solve()
}
