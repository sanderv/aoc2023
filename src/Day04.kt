fun main() {
    fun processCard(card: Card, cards: Map<Int, Card>) {
        (1..card.numberCorrect).forEach { cards[card.cardNumber + it]!!.copies += card.copies }
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { Card(it).score }
    }


    fun part2(input: List<String>): Int {
        val cards = input
            .map { Card(it) }
            .associateBy { it.cardNumber }
        cards
            .map { card -> processCard(card.value, cards) }
        return cards.map { it.value }.sumOf { it.copies }
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

class Card(input: String) {
    val numberCorrect: Int
    val score: Int
    val cardNumber: Int
    var copies = 1

    init {
        val (card, content) = input.split(':')
        cardNumber = card.split("\\s+".toRegex())[1].toInt()
        val (winners, mine) = content.split('|')
        val winningNumbers = getNumbers(winners)
        val myNumbers = getNumbers(mine)
        numberCorrect = winningNumbers.intersect(myNumbers).size
        score = Math.pow(2.0, (numberCorrect - 1).toDouble()).toInt()
    }

    private fun getNumbers(value: String): Set<Int> = value
        .trim()
        .split("\\s+".toRegex())
        .map { it.toInt() }
        .toSet()
}