import kotlin.math.abs

fun main() {
    fun getUniverse(input: List<String>): Universe {
        val galaxies = input
            .mapIndexed { y, line ->
                line
                    .mapIndexed { x, char ->
                        if (char == '#') Galaxy(x, y)
                        else null
                    }
                    .filterNotNull()
            }
            .flatten()
        return Universe(galaxies)
    }

    fun part1(input: List<String>): Long {
        val universe = getUniverse(input)
        universe.expand(2)
        return universe.countDistances()
    }

    fun part2(input: List<String>): Long {
        val universe = getUniverse(input)
        universe.expand(1000000)
        return universe.countDistances()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 374L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}

class Galaxy(var x: Int, var y: Int) {
    fun distance(other: Galaxy) = abs(other.x - x) + abs(other.y - y)

    override fun toString(): String {
        return "($x, $y)"
    }
}

class Universe(val galaxies: List<Galaxy>) {
    fun xSize() = galaxies.maxOf { it.x }
    fun ySize() = galaxies.maxOf { it.y }

    fun expand(times: Int) {
        for (row in ySize() - 1 downTo 0) {
            if (rowIsEmpty(row)) {
                expandRow(row, times)
            }
        }
        for (column in xSize() - 1 downTo 0) {
            if (columnIsEmpty(column)) {
                expandColumn(column, times)
            }
        }
    }

    fun countDistances(): Long {
        var countGalaxies = galaxies
        var totalCount = 0L

        while (countGalaxies.size > 1) {
            val head = countGalaxies.first()
            val tail = countGalaxies.drop(1)
            totalCount = tail.fold(totalCount) { acc, galaxy -> acc + head.distance(galaxy) }
            countGalaxies = tail
        }

        return totalCount
    }

    private fun rowIsEmpty(y: Int): Boolean = galaxies.none { it.y == y }
    private fun columnIsEmpty(x: Int): Boolean = galaxies.none { it.x == x }

    private fun expandRow(y: Int, times: Int) {
        galaxies
            .filter { it.y > y }
            .forEach { it.y += (times - 1) }
    }

    private fun expandColumn(x: Int, times: Int) {
        galaxies
            .filter { it.x > x }
            .forEach { it.x += (times - 1) }
    }
}