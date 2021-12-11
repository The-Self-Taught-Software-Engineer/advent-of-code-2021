package codes.jakob.aoc.solution

object Day02 : Solution() {
    override fun solvePart1(input: String): Any {
        val lines: List<String> = input.splitMultiline()

        var horizontalPosition = 0
        var depth = 0
        for (instruction: String in lines) {
            val value: Int = retrieveValue(instruction)
            if (instruction.startsWith("forward")) {
                horizontalPosition += value
            } else if (instruction.startsWith("up")) {
                depth -= value
            } else if (instruction.startsWith("down")) {
                depth += value
            }
        }

        return horizontalPosition * depth
    }

    override fun solvePart2(input: String): Any {
        val lines: List<String> = input.splitMultiline()

        var horizontalPos = 0
        var depth = 0
        var aim = 0
        for (instruction: String in lines) {
            val value: Int = retrieveValue(instruction)
            if (instruction.startsWith("forward")) {
                horizontalPos += value
                depth += (aim * value)
            } else if (instruction.startsWith("up")) {
                aim -= value
            } else if (instruction.startsWith("down")) {
                aim += value
            }
        }

        return horizontalPos * depth
    }

    private fun retrieveValue(instruction: String): Int {
        return instruction.split(" ")[1].toInt()
    }
}

fun main() {
    Day02.solve()
}
