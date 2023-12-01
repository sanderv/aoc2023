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
        val result = if (found != null) {
            (words.indexOf(found) + 1).toString() + replaceWordsWithDigits(line.drop(1))
        } else {
            line[0] + replaceWordsWithDigits(line.drop(1))
        }

        return result
    }

    fun part1(input: List<String>): Int {
        return input
            .map { getValue(it) }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return input
            .map { replaceWordsWithDigits(it) }
            .map { getValue(it) }
            .sum()
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 142)
    check(part2(testInput) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
