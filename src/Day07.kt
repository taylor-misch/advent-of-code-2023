fun main() {

    data class Card(
        val value: Int,
        val label: String
    )

    data class Hand(
        val cardString: String,
        val cards: List<Card>,
        val bid: Int,
        var type: Int = 0
    )


    fun parseHandV1(input: String): Hand {
        val cards = mutableListOf<Card>()
        val cardString = input.split(" ")[0]
        for (card in cardString) {
            val label = card.toString()
            val value = when (label) {
                "T" -> 10
                "J" -> 11
                "Q" -> 12
                "K" -> 13
                "A" -> 14
                else -> label.toInt()
            }
            cards.add(Card(value, label))
        }
        val bid = input.split(" ")[1].trim().toInt()
        return Hand(cardString, cards, bid)
    }

    fun parseHandV2(input: String): Hand {
        val cards = mutableListOf<Card>()
        val cardString = input.split(" ")[0]
        for (card in cardString) {
            val label = card.toString()
            val value = when (label) {
                "T" -> 10
                "Q" -> 12
                "K" -> 13
                "A" -> 14
                "J" -> 1
                else -> label.toInt()
            }
            cards.add(Card(value, label))
        }
        val bid = input.split(" ")[1].trim().toInt()
        return Hand(cardString, cards, bid)
    }

    fun isFiveOfAKind(hand: Hand): Boolean {
        return hand.cards.groupBy { it.value }.values.any { it.size == 5 }
    }

    fun isFourOfAKind(hand: Hand): Boolean {
        return hand.cards.groupBy { it.value }.values.any { it.size == 4 }
    }

    fun isFullHouse(hand: Hand): Boolean {
        return hand.cards.groupBy { it.value }.values.any { it.size == 3 } &&
                hand.cards.groupBy { it.value }.values.any { it.size == 2 }
    }

    fun isThreeOfAKind(hand: Hand): Boolean {
        return hand.cards.groupBy { it.value }.values.any { it.size == 3 }
    }

    fun isTwoPairs(hand: Hand): Boolean {
        return hand.cards.groupBy { it.value }.values.filter { it.size == 2 }.size == 2
    }

    fun isOnePair(hand: Hand): Boolean {
        return hand.cards.groupBy { it.value }.values.any { it.size == 2 }
    }

    fun isHighCard(hand: Hand): Boolean {
        return hand.cards.groupBy { it.value }.values.all { it.size == 1 }
    }

    fun determineType(hand: Hand): Int {
        return when {
            isFiveOfAKind(hand) -> 1
            isFourOfAKind(hand) -> 2
            isFullHouse(hand) -> 3
            isThreeOfAKind(hand) -> 4
            isTwoPairs(hand) -> 5
            isOnePair(hand) -> 6
            isHighCard(hand) -> 7
            else -> 8
        }
    }

    fun tieBreakHands(hand1: Hand, hand2: Hand): Int {
        for (i in 0..4) {
            if (hand1.cards[i].value > hand2.cards[i].value) {
                return 1
            } else if (hand1.cards[i].value < hand2.cards[i].value) {
                return -1
            }
        }
        return 0
    }

    fun compareHands(hand1: Hand, hand2: Hand): Int {
        return when {
            hand1.type < hand2.type -> 1
            hand1.type > hand2.type -> -1
            else -> tieBreakHands(hand1, hand2)
        }
    }

    fun sortHandsV1(input: List<String>): List<Hand> {
        val hands = input.map { parseHandV1(it) }
        for (hand in hands) {
            hand.type = determineType(hand)
        }
        return hands.sortedWith(::compareHands)
    }

    fun jokerReplacement(hand: Hand): Hand {
        val jokerCount = hand.cardString.count { it == 'J' }
//        println("Hand: ${hand.cardString} - $jokerCount jokers")
        if(jokerCount == 0) {
            return hand
        }
        when {
            hand.type == 1 -> { // 5 of a kind
                return hand
            }

            hand.type == 2 -> { // 4 of a kind
                return when {
                    jokerCount == 1 || jokerCount == 4 -> {
                        hand.type = 1
                        hand
                    }
                    else -> {
                        println("Shouldn't get here (4 of a kind)")
                        hand
                    }
                }
            }

            hand.type == 3 -> { // full house
                return when {
                    jokerCount == 2 || jokerCount == 3 -> {
                        hand.type = 1
                        hand
                    }

                    else -> {
                        println("Shouldn't get here (full house)")
                        hand
                    }
                }
            }

            hand.type == 4 -> { // 3 of a kind
                return when {
                    jokerCount == 1 || jokerCount == 3 -> {
                        hand.type = 2
                        hand
                    }

                    else -> {
                        println("Shouldn't get here (3 of a kind)")
                        hand
                    }
                }
            }

            hand.type == 5 -> { // 2 pairs
                return when {
                    jokerCount == 1 -> {
                        hand.type = 3
                        hand
                    }

                    jokerCount == 2 -> {
                        hand.type = 2
                        hand
                    }

                    else -> {
                        println("Shouldn't get here (2 pairs)")
                        hand
                    }
                }
            }

            hand.type == 6 -> { // 1 pair
                return when {
                    jokerCount == 1 || jokerCount == 2 -> {
                        hand.type = 4
                        hand
                    }

                    else -> {
                        println("Shouldn't get here (1 pair)")
                        hand
                    }
                }
            }
            hand.type == 7 -> { // high card
                return when {
                    jokerCount == 1 -> {
                        hand.type = 6
                        hand
                    }

                    else -> {
                        println("Shouldn't get here (high card)")
                        hand
                    }
                }
            }
            else -> {
                println("Shouldn't get here (else)")
                return hand
            }
        }
    }

    fun sortHandsV2(input: List<String>): List<Hand> {
        val hands = input.map { parseHandV2(it) }
        val newHands = mutableListOf<Hand>()
        for (hand in hands) {
            hand.type = determineType(hand)
            newHands.add(jokerReplacement(hand))
        }
        return newHands.sortedWith(::compareHands)
    }

    fun calculateWinnings(hands: List<Hand>): Int {
        var winnings = 0
        for ((rankIndex, hand) in hands.withIndex()) {
            winnings += hand.bid * (rankIndex + 1)
        }
        println("Winnings: $winnings")
        return winnings
    }

    fun part1(input: List<String>): Int {
        val hands = sortHandsV1(input)
//        hands.forEach(::println)
        val winnings = calculateWinnings(hands)
        return winnings
    }



    fun part2(input: List<String>): Int {
        val hands = sortHandsV2(input)
//        hands.forEach(::println)
        val winnings = calculateWinnings(hands)
        return winnings
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}