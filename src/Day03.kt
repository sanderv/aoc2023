fun main() {
    fun part1(input: List<String>): Int {
        val engine = Engine(input)
        return engine.findPartNumbers().sum()
    }

    fun part2(input: List<String>): Int {
        val engine = Engine(input, '*')
        return engine.findGearRatios().sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

class Engine(input: List<String>, wantedSymbol: Char? = null) {
    private val schematic: List<String>
    private val symbols = mutableListOf<Symbol>()
    private val partNumbers = mutableListOf<PartNumber>()

    init {
        schematic = input.map { ".$it." }.toMutableList()
        schematic.add(0, ".".repeat(input[0].length + 2))
        schematic.add(".".repeat(input[0].length + 2))

        for (x in 0..schematic[0].length - 1) {
            for (y in 0..schematic.size - 1) {
                val char = schematic[y][x]
                if (!char.isDigit() && char != '.') {
                    if (wantedSymbol == null || char == wantedSymbol) {
                        symbols.add(Symbol(x, y))
                    }
                }
            }
        }

        var x: Int
        var y = 0

        while (y < schematic.size) {
            x = 0
            val line = schematic[y]
            while (x < line.length) {
                val skipped = line.drop(x).takeWhile { !it.isDigit() }
                x += skipped.length
                val number = line.drop(x).takeWhile { it.isDigit() }
                if (number.isNotEmpty()) {
                    partNumbers.add(PartNumber(number.toInt(), x, y, number.length))
                }
                x += number.length
            }
            y++
        }
    }

    fun findPartNumbers(): List<Int> {
        return partNumbers
            .filter { nextToSymbol(it) }
            .map { it.number }
    }

    fun findGearRatios(): List<Int> {
        return symbols
            .map { symbol ->
                partNumbers.filter { it.isNextTo(symbol) }
            }
            .filter { it.size == 2 }
            .map { it[0].number * it[1].number }
    }

    private fun nextToSymbol(partNumber: PartNumber): Boolean {
        return symbols.any { symbol ->
            partNumber.isNextTo(symbol)
        }
    }
}

class Symbol(val x: Int, val y: Int) {
    override fun toString() = "($x, $y)"
}

class PartNumber(val number: Int, val x: Int, val y: Int, val length: Int) {
    fun isNextTo(symbol: Symbol): Boolean {
        val minX = x - 1
        val maxX = x + length
        val minY = y - 1
        val maxY = y + 1
        return symbol.x in minX..maxX && symbol.y in minY..maxY


    }

    override fun toString() = "$number ($x, $y) -> $length"
}