package codes.jakob.aoc

object Day02 : Solution() {
    override fun solvePart1(input: String): Any {
        val instructions: List<Pair<String, Int>> = input.splitMultiline().map { parseInstruction(it) }

        var horizontalPosition = 0
        var depth = 0
        for ((direction: String, value: Int) in instructions) {
            when (direction) {
                "forward" -> horizontalPosition += value
                "up" -> depth -= value
                "down" -> depth += value
            }
        }

        return horizontalPosition * depth
    }

    override fun solvePart2(input: String): Any {
        val instructions: List<Pair<String, Int>> = input.splitMultiline().map { parseInstruction(it) }

        var horizontalPos = 0
        var depth = 0
        var aim = 0
        for ((direction: String, value: Int) in instructions) {
            when (direction) {
                "forward" -> {
                    horizontalPos += value
                    depth += (aim * value)
                }
                "up" -> aim -= value
                "down" -> aim += value
            }
        }

        return horizontalPos * depth
    }

    private fun parseInstruction(instruction: String): Pair<String, Int> {
        val (direction, value) = instruction.split(" ")
        return direction to value.toInt()
    }
}

fun main() {
    Day02.solve()
}
