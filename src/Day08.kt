fun main() {

    fun findLCM(a: Long, b: Long): Long {
        val larger = if (a > b) a else b
        val maxLcm = a * b
        var lcm = larger
        while (lcm <= maxLcm) {
            if (lcm % a == 0L && lcm % b == 0L) {
                return lcm
            }
            lcm += larger
        }
        return maxLcm
    }

    fun findLCMOfListOfNumbers(numbers: List<Long>): Long {
        var result = numbers[0]
        for (i in 1 until numbers.size) {
            result = findLCM(result, numbers[i])
        }
        return result
    }

    fun part1(input: List<String>): Int {
        val lr = input[0]

        val regex = Regex("[A-Z]+")

        val map = input.drop(2)
            .map { regex.findAll(it) }
            .associate {
                val i = it.iterator()
                i.next().value to Pair(i.next().value, i.next().value)
            }
        val route = Route(map, lr)
        return route.getOut()
    }

    fun part2(input: List<String>): Long {
        val lr = input[0]

        val regex = Regex("\\w+")

        val map = input.drop(2)
            .map { regex.findAll(it) }
            .associate {
                val i = it.iterator()
                i.next().value to Pair(i.next().value, i.next().value)
            }
        val ghostSteps = map.keys
            .filter { it.endsWith('A') }
            .map { Route(map, lr, it) }
            .map { it.getOut().toLong() }
        return findLCMOfListOfNumbers(ghostSteps)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 2)
    val test2Input = readInput("Day08_test2")
    check(part2(test2Input) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}

class Route(val map: Map<String, Pair<String, String>>, val lrSequence: String, var mapStep: String = "AAA") {
    var lrPointer: Int = 0

    fun getOut(): Int {
        var steps = 0

        while (!mapStep.endsWith('Z')) {
            mapStep = takeStep(mapStep)
            steps++
            lrPointer++
        }
        return steps
    }

    private fun takeStep(mapStep: String): String {
        if (lrPointer == lrSequence.length) {
            lrPointer = 0
        }
        val lr = lrSequence[lrPointer]
        val dirs = map[mapStep]!!
        return if (lr == 'L') dirs.first else dirs.second
    }
}
