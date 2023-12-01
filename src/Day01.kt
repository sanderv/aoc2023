fun main() {
    val words = listOf(
        "one",
        "two",
        "three",
        "four",
        "five",
        "six",
        "seven",
        "eight",
        "nine",
    )

    fun getValue(line: String): Int {
        val digits = line
            .filter { it.isDigit() }

        return "${digits.first()}${digits.last()}".toInt()
    }

    fun replaceWordsWithDigits(line: String): String {
        if (line.isEmpty()) {
            return ""
        }
        val found = (words.firstOrNull { line.startsWith(it) })
        val result = if (found != null) (words.indexOf(found) + 1).toString() else line.take(1)
        return result + replaceWordsWithDigits(line.drop(1))
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { getValue(it) }
    }

    fun part2(input: List<String>): Int {
        return input
            .map { replaceWordsWithDigits(it) }
            .sumOf { getValue(it) }
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 142)
    check(part2(testInput) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
