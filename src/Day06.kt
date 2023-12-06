fun main() {
    fun findBetterRaces(race: Race): Int {
        return (1..(race.time))
            .map { ms ->
                // distance
                (race.time - ms) * ms
            }.count { distance ->
                distance > race.distance
            }
    }

    fun part1(input: List<String>): Int {
        val times = input[0].split(':')[1].trim().split("\\s+".toRegex()).map { it.toLong() }
        val distances = input[1].split(':')[1].trim().split("\\s+".toRegex()).map { it.toLong() }
        val races = times.zip(distances) { time, distance -> Race(time, distance) }

        return races
            .map { race ->
                findBetterRaces(race)
            }
            .fold(1) { acc, i -> acc * i }
            .toInt()
    }

    fun part2(input: List<String>): Int {
        val time = input[0].split(':')[1].trim().replace(" ", "").toLong()
        val distance = input[1].split(':')[1].trim().replace(" ", "").toLong()
        val race = Race(time, distance)
        return findBetterRaces(race)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}

class Race(val time: Long, val distance: Long)
