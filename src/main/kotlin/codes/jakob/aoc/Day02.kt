package codes.jakob.aoc

object Day02 : Solution() {
    override val identifier: String = this::class.simpleName.toString()

    override fun solvePart1(input: String): String {
        val lines: List<String> = splitMultilineInput(input)

        var horizontalPos = 0
        var depth = 0
        for (instruction in lines) {
            val value: Int = retrieveValue(instruction)
            if (instruction.startsWith("forward")) {
                horizontalPos += value
            } else if (instruction.startsWith("up")) {
                depth -= value
            } else if (instruction.startsWith("down")) {
                depth += value
            }
        }

        return (horizontalPos * depth).toString()
    }

    override fun solvePart2(input: String): String {
        val lines: List<String> = splitMultilineInput(input)

        var horizontalPos = 0
        var depth = 0
        var aim = 0
        for (instruction in lines) {
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

        return (horizontalPos * depth).toString()
    }

    private fun retrieveValue(instruction: String): Int {
        return instruction.split(" ")[1].toInt()
    }
}

fun main() {
    Day02.solve()
}
