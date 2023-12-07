import HandType.*

fun main() {
    fun part1(input: List<String>): Int {
        return input
            .map { PokerHand(it, "AKQJT98765432") }
            .sorted()
            .foldIndexed(0) { i, score, hand -> score + ((i + 1) * hand.bet) }
    }

    fun part2(input: List<String>): Int {
        return input
            .map { PokerHand(it, "AKQT98765432J") }
            .sorted()
            .foldIndexed(0) { i, score, hand -> score + ((i + 1) * hand.bet) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println() // 248456823
}

class PokerHand(input: String, val cardOrder: String) : Comparable<PokerHand> {
    val cards: String
    val score: Int
    val bet: Int


    init {
        val (scoreInput, betInput) = input.split(' ')
        cards = scoreInput
        score = score(scoreInput)
        bet = betInput.toInt()
    }

    private fun score(input: String): Int {
        val cardsByType = input
            .groupBy { it }
            .map { (type, cards) -> type to cards.size }

        val handType: HandType =
            if (cardOrder.endsWith('2')) {
                if (cardsByType.any { it.second == 5 }) FIVE_OF_A_KIND
                else if (cardsByType.any() { it.second == 4 }) FOUR_OF_A_KIND
                else if (cardsByType.any { it.second == 3 } && cardsByType.any { it.second == 2 }) FULL_HOUSE
                else if (cardsByType.any { it.second == 3 }) THREE_OF_A_KIND
                else if (cardsByType.filter { it.second == 2 }.size == 2) TWO_PAIR
                else if (cardsByType.any { it.second == 2 }) ONE_PAIR
                else HIGH_CARD
            } else {
                if (isFiveOfAKind(cardsByType)) FIVE_OF_A_KIND
                else if (isFourOfAKind(cardsByType)) FOUR_OF_A_KIND
                else if (isFullHouse(cardsByType)) FULL_HOUSE
                else if (isThreeOfAKind(cardsByType)) THREE_OF_A_KIND
                else if (isTwoPair(cardsByType)) TWO_PAIR
                else if (isOnePair(cardsByType)) ONE_PAIR
                else HIGH_CARD
            }
        return handType.score
    }

    private fun isFiveOfAKind(cardsByType: List<Pair<Char, Int>>): Boolean {
        val nrOfJokers = nrOfJokers()
        return cardsByType.any { it.second == 5 } || cardsByType.any { it.first != 'J' && it.second == 5 - nrOfJokers }
    }

    private fun isFourOfAKind(cardsByType: List<Pair<Char, Int>>): Boolean {
        val nrOfJokers = nrOfJokers()
        return cardsByType.any() { it.second == 4 } || cardsByType.any { it.first != 'J' && it.second == 4 - nrOfJokers }
    }

    private fun isFullHouse(cardsByType: List<Pair<Char, Int>>): Boolean {
        val nrOfJokers = nrOfJokers()
        return if (nrOfJokers == 0) {
            cardsByType.any { it.second == 3 } && cardsByType.any { it.second == 2 }
        } else {
            // More than 1 effective joker isn't possible here
            val doubleCards = cardsByType.filter { it.first != 'J' && it.second == 2 }.size
            val tripleCard = cardsByType.firstOrNull { it.first != 'J' && it.second == 3 }
            return (doubleCards == 2 || tripleCard != null)
        }
    }

    private fun isThreeOfAKind(cardsByType: List<Pair<Char, Int>>): Boolean {
        val nrOfJokers = nrOfJokers()
        return cardsByType.any { it.second == 3 } || cardsByType.any { it.first != 'J' && it.second == 3 - nrOfJokers }
    }

    private fun isTwoPair(cardsByType: List<Pair<Char, Int>>): Boolean {
        val nrOfJokers = nrOfJokers()
        return if (nrOfJokers == 0) {
            cardsByType.filter { it.second == 2 }.size == 2
        } else {
            cardsByType.any { it.second == 2 }
        }
    }

    private fun isOnePair(cardsByType: List<Pair<Char, Int>>): Boolean {
        val nrOfJokers = nrOfJokers()
        return if (nrOfJokers == 0) {
            cardsByType.any { it.second == 2 }
        } else {
            true
        }
    }

    private fun nrOfJokers() = cards.count { it == 'J' }

    override fun compareTo(other: PokerHand): Int {
        return if (this.score == other.score) {
            val a = this.cards.map { cardOrder.reversed().indexOf(it) }
            val b = other.cards.map { cardOrder.reversed().indexOf(it) }
            val z = a.zip(b).firstOrNull { it.first != it.second }
            z?.let { it.first.compareTo(it.second) } ?: 0
        } else if (this.score > other.score) {
            1
        } else {
            -1
        }
    }

}

enum class HandType(val score: Int) {
    FIVE_OF_A_KIND(7),
    FOUR_OF_A_KIND(6),
    FULL_HOUSE(5),
    THREE_OF_A_KIND(4),
    TWO_PAIR(3),
    ONE_PAIR(2),
    HIGH_CARD(1)
}