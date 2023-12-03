fun main() {
    fun part1(input: List<String>): Int {
        return input
            .map { line ->
                val (game, moves) = line.split(':')
                val gameNumber = game.split(' ')[1].toInt()
                Game(gameNumber, 12, 13, 14).parse(moves)
            }
            .filter { it.valid }
            .sumOf { it.number }
    }

    fun part2(input: List<String>): Int {
        return input
            .map { line ->
                val (game, moves) = line.split(':')
                val gameNumber = game.split(' ')[1].toInt()
                Game(gameNumber, 12, 13, 14).parse(moves)
            }
            .sumOf { game -> game.minRed * game.minGreen * game.minBlue }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

class Game(val number: Int, private val maxRed: Int, private val maxGreen: Int, private val maxBlue: Int) {
    var valid: Boolean = true
    var minRed = 0
    var minGreen = 0
    var minBlue = 0

    private fun addMove(red: Int?, green: Int?, blue: Int?) {
        if ((red ?: 0) > maxRed || (green ?: 0) > maxGreen || (blue ?: 0) > maxBlue) {
            valid = false
        }
        if (red?.let { it > minRed } == true) minRed = red
        if (green?.let { it > minGreen } == true) minGreen = green
        if (blue?.let { it > minBlue } == true) minBlue = blue
    }

    fun parse(input: String): Game {
        input.split(';')
            .map { move ->
                move
                    .trim()
                    .split(',')
                    .associate { blocks ->
                        val (number, color) = blocks.trim().split(' ')
                        color.trim() to number.toInt()
                    }
            }
            .forEach { blocks ->
                addMove(blocks["red"] ?: 0, blocks["green"] ?: 0, blocks["blue"] ?: 0)
            }
        return this
    }
}