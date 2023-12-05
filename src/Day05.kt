fun main() {
    fun getBlocks(input: List<String>): MutableList<List<String>> {
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
        return blocks
    }

    fun calculateResult(
        blocks: MutableList<List<String>>,
        seeds: Iterable<Long>
    ): Long {
        val seedMaps = blocks.drop(1)
            .map { SeedMap(it) }
        var minResult = Long.MAX_VALUE

        seeds.forEach { seed ->
            var result = seed
            seedMaps.forEach {
                result = it.map(result)
            }
            minResult = if (result < minResult) result else minResult
        }
        return minResult
    }

    fun part1(input: List<String>): Long {
        val blocks = getBlocks(input)
        val seeds = blocks[0][0].split(": ")[1].split(' ').map { it.toLong() }
        return calculateResult(blocks, seeds)
    }

    fun part2(input: List<String>): Long {
        val blocks = getBlocks(input)
        val numbers = blocks[0][0].split(": ")[1].split(' ').map { it.toLong() }
        val seedRanges = numbers.chunked(2)
            .map { (start, length) -> start..<(start + length) }
        return seedRanges.mapIndexed { index, seeds ->
            println("LongRange $index")
            calculateResult(blocks, seeds)
        }.min()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
//    check(part1(testInput) == 35L)
//    check(part2(testInput) == 46L)

    val input = readInput("Day05")
//    part1(input).println()
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
