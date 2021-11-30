package codes.jakob.aoc

import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

abstract class Solution {
    abstract val identifier: String

    abstract fun solvePart1(input: String): String

    abstract fun solvePart2(input: String): String

    fun solve() {
        println("Solution for part 1: " + solvePart1(retrieveInput()))
        println("Solution for part 2: " + solvePart2(retrieveInput()))
    }

    fun retrieveInput(): String {
        val inputDirectoryPath: Path = Paths.get("").resolve(INPUT_PATH).toAbsolutePath()
        return File("$inputDirectoryPath/$identifier.$INPUT_FILE_EXTENSION").readText()
    }

    fun splitMultilineInput(multiline: String): List<String> {
        return multiline.split("\n")
    }

    companion object {
        const val INPUT_PATH = "src/main/resources/inputs"
        const val INPUT_FILE_EXTENSION = "txt"
    }
}
