package codes.jakob.aoc

import org.junit.Test
import java.io.File
import java.nio.file.Paths
import kotlin.test.assertEquals

class SolutionTest {
    @Test
    fun `The content of an input file can be correctly retrieved`() {
        val testFileName = "Day00"
        val testFileContent = "Just a test!"

        val sut = object : Solution() {
            override val identifier: String = testFileName

            override fun solvePart1(input: String): String {
                return input
            }

            override fun solvePart2(input: String): String {
                return input
            }
        }

        // Create temporary test file
        val inputDirectoryPath = Paths.get("").resolve(Solution.INPUT_PATH).toAbsolutePath()
        File("$inputDirectoryPath/$testFileName.${Solution.INPUT_FILE_EXTENSION}").writeText(testFileContent)

        assertEquals(testFileContent, sut.retrieveInput())

        // Delete temporary test file
        File("$inputDirectoryPath/$testFileName.${Solution.INPUT_FILE_EXTENSION}").delete()
    }
}
