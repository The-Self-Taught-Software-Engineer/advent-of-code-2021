@file:Suppress("unused")

package codes.jakob.aoc

import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

abstract class Solution {
    abstract fun solvePart1(input: String): Any

    abstract fun solvePart2(input: String): Any

    private val identifier: String = getClassName()

    fun solve() {
        println("Solution for part 1: ${solvePart1(retrieveInput())}")
        println("Solution for part 2: ${solvePart2(retrieveInput())}")
    }

    fun retrieveInput(): String {
        val inputDirectoryPath: Path = Paths.get("").resolve(INPUT_PATH).toAbsolutePath()
        return File("$inputDirectoryPath/$identifier.$INPUT_FILE_EXTENSION").readText()
    }

    private fun getClassName(): String = this::class.simpleName.toString()

    companion object {
        const val INPUT_PATH = "src/main/resources/inputs"
        const val INPUT_FILE_EXTENSION = "txt"
    }
}

fun String.splitMultiline(): List<String> = split("\n")

fun <T> Iterable<T>.productOf(selector: (T) -> Int): Int {
    var product = 1
    for (element in this) product *= selector(element)
    return product
}

/**
 * Calculates the [triangular number](https://en.wikipedia.org/wiki/Triangular_number) of the given number.
 */
fun Long.triangular(): Long = ((this * (this + 1)) / 2)

fun <T, K> Collection<T>.countBy(keySelector: (T) -> K): Map<K, Int> {
    return this.groupBy(keySelector).mapValues { it.value.count() }
}

fun List<Int>.binaryToDecimal(): Int {
    require(this.all { it == 0 || it == 1 }) { "Expected bit string, but received $this" }
    return Integer.parseInt(this.joinToString(""), 2)
}

fun Int.bitFlip(): Int {
    require(this == 0 || this == 1) { "Expected bit, but received $this" }
    return this.xor(1)
}

fun String.toBitString(): List<Int> {
    val bits: List<String> = split("").filter { it.isNotBlank() }
    require(bits.all { it == "0" || it == "1" }) { "Expected bit string, but received $this" }
    return bits.map { it.toInt() }
}

/**
 * [Transposes](https://en.wikipedia.org/wiki/Transpose) the given list of nested lists (a matrix, in essence).
 *
 * This function is adapted from this [post](https://stackoverflow.com/a/66401340).
 */
fun <T> List<List<T>>.transpose(): List<List<T>> {
    val result: MutableList<MutableList<T>> = (this.first().indices).map { mutableListOf<T>() }.toMutableList()
    this.forEach { columns -> result.zip(columns).forEach { (rows, cell) -> rows.add(cell) } }
    return result
}

/**
 * Returns any given [Map] with its keys and values reversed (i.e., the keys becoming the values and vice versa).
 * Note in case of duplicate values, they will be overridden in the key-set unpredictably.
 */
fun <K, V> Map<K, V>.reversed(): Map<V, K> {
    return HashMap<V, K>(this.count()).also { reversedMap: HashMap<V, K> ->
        this.entries.forEach { reversedMap[it.value] = it.key }
    }
}
