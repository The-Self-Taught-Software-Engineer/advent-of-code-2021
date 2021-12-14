package codes.jakob.aoc.solution

import java.util.*

object Day10 : Solution() {
    override fun solvePart1(input: String): Any {
        return parseInput(input)
            .mapNotNull { characters: List<Character> ->
                try {
                    match(characters)
                    null
                } catch (exception: IllegalCharacterException) {
                    exception.actual
                }
            }
            .countBy { it }
            .map { (character: Character, count: Int) -> Scorer.calculateScoreForMismatch(character) * count }
            .sum()
    }

    override fun solvePart2(input: String): Any {
        return parseInput(input)
            .mapNotNull { characters: List<Character> ->
                try {
                    match(characters)
                } catch (exception: IllegalCharacterException) {
                    null
                }
            }
            .filterNot { it.matching }
            .map { matchResult: MatchResult ->
                matchResult.missing.fold(0L) { accumulator: Long, character: Character ->
                    (accumulator * 5) + Scorer.calculateScoreForCompletion(character)
                }
            }
            .sorted()
            .middleOrNull()!!
    }

    private fun match(characters: List<Character>): MatchResult {
        val stack: Stack<Character> = Stack()

        for (current: Character in characters) {
            if (current.type == Character.Type.OPEN) {
                stack.push(current)
            } else {
                val matchingCurrent: Character = current.opposite()
                val top: Character? = stack.peekOrNull()

                if (matchingCurrent == top) {
                    stack.pop()
                } else {
                    throw IllegalCharacterException(top?.opposite(), current)
                }
            }
        }

        val missing: List<Character> = stack.map { it.opposite() }.reversed()

        return MatchResult(characters, stack.isEmpty(), missing)
    }

    private fun parseInput(input: String): List<List<Character>> {
        return input
            .splitMultiline()
            .map { line ->
                line
                    .split("")
                    .filter { it.isNotBlank() }
                    .map { it.toSingleChar() }
                    .map { Character.fromChar(it) ?: error("Character $it not supported") }
            }
    }

    object Scorer {
        private val MISMATCH_SCORES: Map<Character, Int> = mapOf(
            Character.CLOSE_PARENTHESES to 3,
            Character.CLOSE_BRACKET to 57,
            Character.CLOSE_BRACE to 1197,
            Character.CLOSE_ANGLE to 25137,
        )

        private val COMPLETION_SCORES: Map<Character, Int> = mapOf(
            Character.CLOSE_PARENTHESES to 1,
            Character.CLOSE_BRACKET to 2,
            Character.CLOSE_BRACE to 3,
            Character.CLOSE_ANGLE to 4,
        )

        fun calculateScoreForMismatch(character: Character): Int {
            require(character.type == Character.Type.CLOSE) { "Only closing characters are scored" }
            return MISMATCH_SCORES[character] ?: error("Character $character not tracked")
        }

        fun calculateScoreForCompletion(character: Character): Int {
            require(character.type == Character.Type.CLOSE) { "Only closing characters are scored" }
            return COMPLETION_SCORES[character] ?: error("Character $character not tracked")
        }
    }

    enum class Character(val type: Type, val char: Char) {
        OPEN_PARENTHESES(Type.OPEN, '(') {
            override fun opposite(): Character = CLOSE_PARENTHESES
        },
        CLOSE_PARENTHESES(Type.CLOSE, ')') {
            override fun opposite(): Character = OPEN_PARENTHESES
        },
        OPEN_BRACKET(Type.OPEN, '[') {
            override fun opposite(): Character = CLOSE_BRACKET
        },
        CLOSE_BRACKET(Type.CLOSE, ']') {
            override fun opposite(): Character = OPEN_BRACKET
        },
        OPEN_BRACE(Type.OPEN, '{') {
            override fun opposite(): Character = CLOSE_BRACE
        },
        CLOSE_BRACE(Type.CLOSE, '}') {
            override fun opposite(): Character = OPEN_BRACE
        },
        OPEN_ANGLE(Type.OPEN, '<') {
            override fun opposite(): Character = CLOSE_ANGLE
        },
        CLOSE_ANGLE(Type.CLOSE, '>') {
            override fun opposite(): Character = OPEN_ANGLE
        };

        abstract fun opposite(): Character

        override fun toString(): String = char.toString()

        enum class Type {
            OPEN,
            CLOSE;
        }

        companion object {
            fun fromChar(char: Char): Character? {
                return values().find { it.char == char }
            }
        }
    }

    data class MatchResult(
        val characters: List<Character>,
        val matching: Boolean,
        val missing: List<Character>,
    )

    class IllegalCharacterException(
        val expected: Character?,
        val actual: Character,
    ) : RuntimeException("Expected $expected, but found $actual instead.")
}

fun main() {
    Day10.solve()
}
