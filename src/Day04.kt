import kotlin.math.pow

fun main() {

    data class Card(
        val cardNumber: Int,
        val winningNumbers: List<Int>,
        val playerNumbers: List<Int>,
        var cardScore: Int = 1,
        var numberOfCards: Int = 1,
        var winningNumbersCount: Int = 0
    )

    fun stringToInt(input: String): Int {
        if (input.isEmpty()) return 0
        return input.toInt()
    }

    fun parseCard(input: String, lineIndex: Int): Card {
        return Card(
            cardNumber = lineIndex + 1,
            winningNumbers = input.split(":")[1].split(" | ")[0].trim().split(" ").map { stringToInt(it) }.filter { it != 0 },
            playerNumbers = input.split(":")[1].split(" | ")[1].trim().split(" ").map { stringToInt(it) }.filter { it != 0 }
        )
    }


    fun part1(input: List<String>): Int {
        val cardList = mutableListOf<Card>()
        for ((lineIndex, line) in input.withIndex()) {
            val card = parseCard(line, lineIndex)
            var winningNumberCount = 0
            for (number in card.playerNumbers) {
                if (card.winningNumbers.contains(number)) {
                    winningNumberCount++
                }
            }
            card.winningNumbersCount = winningNumberCount
            card.cardScore = if (winningNumberCount == 0) {
                0
            } else if (winningNumberCount == 1) {
                1
            } else {
                2.0.pow(winningNumberCount - 1).toInt()
            }
            cardList.add(card)

        }
        var total = 0
        for (card in cardList) {
            println(card)
            total += card.cardScore
        }
        println("Total: $total")
        return total
    }

    fun part2(input: List<String>): Int {
        val cardList = mutableListOf<Card>()
        for ((lineIndex, line) in input.withIndex()) {
            val card = parseCard(line, lineIndex)
            var winningNumberCount = 0
            for (number in card.playerNumbers) {
                if (card.winningNumbers.contains(number)) {
                    winningNumberCount++
                }
            }
            card.winningNumbersCount = winningNumberCount
            card.cardScore = if (winningNumberCount == 0) {
                0
            } else if (winningNumberCount == 1) {
                1
            } else {
                2.0.pow(winningNumberCount - 1).toInt()
            }
            cardList.add(card)
        }

        for ((cardIndex, card) in cardList.withIndex()) {
            var winningNumbers = card.winningNumbersCount
            var currentIndex = cardIndex + 1
            while (currentIndex < cardList.size && winningNumbers > 0) {
                val nextCard = cardList[currentIndex]
                nextCard.numberOfCards += (1 * card.numberOfCards)
                winningNumbers--
                currentIndex++
            }
        }

        var total = 0
        for (card in cardList) {
            println("Card: $card")
            total += card.numberOfCards
        }
        println("Total: $total")
        return total
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}