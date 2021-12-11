package codes.jakob.aoc.solution

object Day06 : Solution() {
    override fun solvePart1(input: String): Any {
        return simulateDays(parseInput(input), 80)
    }

    override fun solvePart2(input: String): Any {
        return simulateDays(parseInput(input), 256)
    }

    private fun simulateDays(initialTimerValues: List<Int>, days: Int): ULong {
        var fishToAmount: Map<Int, ULong> = initialTimerValues.countBy { it }.mapValues { it.value.toULong() }

        println("Starting with ${fishToAmount.values.sum()} fish")
        repeat(days) { day ->
            fishToAmount = fishToAmount
                .flatMap { (timeUntilProcreation, amount) ->
                    if (timeUntilProcreation == 0) {
                        listOf(6 to amount, 8 to amount)
                    } else {
                        listOf((timeUntilProcreation - 1) to amount)
                    }
                }
                .groupBy({ it.first }, { it.second })
                .mapValues { (_, amounts) -> amounts.sum() }
            println("Population reached ${fishToAmount.values.sum()} fish after day ${day + 1}")
        }

        return fishToAmount.values.sum()
    }

    private fun parseInput(input: String): List<Int> {
        return input.split(",").map { it.toInt() }
    }
}

fun main() {
    Day06.solve()
}
