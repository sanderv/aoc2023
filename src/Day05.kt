fun main() {
    fun part1(input: List<String>): Long {
        val blocks = mutableListOf<List<String>>()
        var currentBlock = mutableListOf<String>()
        input.forEach { line ->
            if (line.isNotBlank()) {
                currentBlock.add(line)
            } else {
                blocks.add(currentBlock)
                currentBlock = mutableListOf()
            }
        }
        blocks.add(currentBlock) // add the last block
        val seeds = blocks[0][0].split(": ")[1].split(' ').map { it.toLong() }
        val seedMaps = blocks.drop(1)
            .map { SeedMap(it) }

        val result = seeds.map { seed ->
            var result = seed
            seedMaps.forEach {
                result = it.map(result)
            }
            result
        }
        return result.min()
    }

    fun part2(input: List<String>): Long {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

class SeedMap(input: List<String>) {
    val name = input.first()
    val mappings = input
        .drop(1)
        .map {
            val (designationStart, sourceStart, length) = it.split(' ')
            val start = sourceStart.toLong()
            val delta = designationStart.toLong() - start
            Mapping(start..<(start + length.toLong()), delta)
        }

    fun map(value: Long): Long =
        mappings
            .firstOrNull() { value in it.sourceRange }
            ?.map(value)
            ?: value

}

class Mapping(val sourceRange: LongRange, val delta: Long) {
    fun map(value: Long) = value + delta
}