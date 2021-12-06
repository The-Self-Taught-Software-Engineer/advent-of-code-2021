package codes.jakob.aoc

object Day06 : Solution() {
    override fun solvePart1(input: String): Any {
        return simulateDays(parseInput(input), 80)
    }

    override fun solvePart2(input: String): Any {
        return simulateDays(parseInput(input), 256)
    }

    private fun simulateDays(initialTimerValues: List<Int>, days: Int): ULong {
        var fishToAmount: Map<Int, ULong> = initialTimerValues
            .groupBy { it }
            .map { it.key to it.value.count().toULong() }
            .toMap()

        println("Starting with ${countTotalFish(fishToAmount)} fish")
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
                .mapValues { (_, amounts) -> countTotalFish(amounts) }
            println("Population reached ${countTotalFish(fishToAmount)} fish after day ${day + 1}")
        }

        return countTotalFish(fishToAmount)
    }

    private fun countTotalFish(amounts: Collection<ULong>): ULong {
        var totalFishAmount: ULong = 0UL
        amounts.forEach { totalFishAmount += it }
        return totalFishAmount
    }

    private fun countTotalFish(fishToAmount: Map<Int, ULong>): ULong {
        return countTotalFish(fishToAmount.values)
    }

    private fun parseInput(input: String): List<Int> {
        return input.split(",").map { it.toInt() }
    }
}

fun main() {
    Day06.solve()
}
