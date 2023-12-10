import CompassDirections.*


fun main() {
    fun printMap(map: List<List<TunnelSegment>>, filterLoop: Boolean) {
        map.forEach { row ->
            row.forEach { print(it.toSymbol(filterLoop)) }
            println("")
        }
    }

    fun part1(input: List<String>): Int {
        val map = input.map { line ->
            line.map { char ->
                TunnelSegment(char)
            }
        }
        map.forEachIndexed { yPos, row ->
            row.forEachIndexed { xPos, segment ->
                segment.connect(xPos, yPos, map)
            }
        }
        val startY = input.indexOfFirst { it.contains('S') }
        val startX = input[startY].indexOf('S')
        val startSegment = map[startY][startX]
        val chosenDir = startSegment.connections.entries.first()
        var currentEntry = startSegment.findNextSegment(chosenDir.key.opposite())
        var steps = 1
        while (currentEntry.value != startSegment) {
            currentEntry = currentEntry.value.findNextSegment(currentEntry.key.opposite())
            steps++
        }
        printMap(map, true)
        return steps / 2
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 8)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}

class TunnelSegment(input: Char) {
    val openings: List<CompassDirections>
    val connections = mutableMapOf<CompassDirections, TunnelSegment>()
    var partOfLoop = false

    init {
        openings = when (input) {
            'F' -> listOf(SOUTH, EAST)
            '-' -> listOf(EAST, WEST)
            '7' -> listOf(WEST, SOUTH)
            '|' -> listOf(NORTH, SOUTH)
            'J' -> listOf(NORTH, WEST)
            'L' -> listOf(NORTH, EAST)
            'S' -> listOf(NORTH, EAST, SOUTH, WEST)
            else -> listOf()
        }
    }

    fun toSymbol(filterLoop: Boolean): String {
        return if (!filterLoop || partOfLoop) {
            if (hasConnections(SOUTH, EAST)) "┏"
            else if (hasConnections(EAST, WEST)) "━"
            else if (hasConnections(WEST, SOUTH)) "┓"
            else if (hasConnections(NORTH, SOUTH)) "┃"
            else if (hasConnections(NORTH, WEST)) "┛"
            else if (hasConnections(NORTH, EAST)) "┗"
            else "▫"
        } else {
            "▫"
        }
    }

    fun connect(xPos: Int, yPos: Int, map: List<List<TunnelSegment>>) {
        openings.forEach { opening ->
            when (opening) {
                NORTH -> {
                    if (yPos != 0) {
                        val northSegment = map[yPos - 1][xPos]
                        if (northSegment.openings.contains(SOUTH)) connections[NORTH] = northSegment
                    }
                }

                EAST -> {
                    if (xPos < map[yPos].size - 1) {
                        val eastSegment = map[yPos][xPos + 1]
                        if (eastSegment.openings.contains(WEST)) connections[EAST] = eastSegment
                    }
                }

                SOUTH -> {
                    if (yPos < map.size - 1) {
                        val southSegment = map[yPos + 1][xPos]
                        if (southSegment.openings.contains(NORTH)) connections[SOUTH] = southSegment
                    }
                }

                WEST -> {
                    if (xPos > 0) {
                        val westSegnment = map[yPos][xPos - 1]
                        if (westSegnment.openings.contains(EAST)) connections[WEST] = westSegnment
                    }
                }
            }
        }
    }

    fun findNextSegment(cameFrom: CompassDirections): Map.Entry<CompassDirections, TunnelSegment> {
        partOfLoop = true
        val dirs = connections
            .filter { it.key != cameFrom }
        return dirs.entries.first()
    }

    private fun hasConnections(vararg directions: CompassDirections): Boolean {
        return directions.all { connections[it] != null }
    }
}

enum class CompassDirections {
    NORTH, EAST, SOUTH, WEST;

    fun opposite() = when (this) {
        NORTH -> SOUTH
        EAST -> WEST
        SOUTH -> NORTH
        WEST -> EAST
    }
}
