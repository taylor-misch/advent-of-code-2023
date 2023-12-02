fun main() {

    fun findNumbers(line: String): String {
        var firstNumber = ""
        var lastNumber = ""
        for (character in line) {
            if (character.isDigit()) {
                if (firstNumber == "") {
                    firstNumber = character.toString()
                }
                lastNumber = character.toString()
            }
        }
        return firstNumber + lastNumber
    }

    fun part1(input: List<String>): Int {

        val numbers = mutableListOf<Int>()
        for (line in input) {
            numbers.add(findNumbers(line).toInt())
        }

        var runningTotal = 0
        for (number in numbers) {
            runningTotal += number
        }

        return runningTotal
    }

    data class WordNumber(
        val word: String,
        val number: String
    )

    data class NumberMatch(
        val number: WordNumber,
        val index: Int
    )

    val wordNumbers = listOf(
        WordNumber("one", "o1e"),
        WordNumber("two", "t2o"),
        WordNumber("three", "th3ee"),
        WordNumber("four", "f4ur"),
        WordNumber("five", "fi5e"),
        WordNumber("six", "s6x"),
        WordNumber("seven", "s7ven"),
        WordNumber("eight", "e8ght"),
        WordNumber("nine", "n9ne"),
    )


    fun replaceEarliestWordNumber(line: String): String {
        var numberMatches = mutableListOf<NumberMatch>()
        wordNumbers.forEach { wordNumber ->
            val index = line.indexOf(wordNumber.word)
            if (index != -1) {
                numberMatches.add(NumberMatch(wordNumber, index))
            }
        }
        return if(numberMatches.isEmpty()) {
            line
        }   else {
            numberMatches.sortBy { it.index }
            val updatedLine = line.replace(numberMatches.first().number.word, numberMatches.first().number.number)
            replaceEarliestWordNumber(updatedLine)
        }
    }

    fun part2(input: List<String>): Int {
        val numbers = mutableListOf<Int>()
        for (line in input) {
            val lineValue = replaceEarliestWordNumber(line)
            val number = findNumbers(lineValue).toInt()
            numbers.add(number)
            println("$lineValue : $number")
        }
        var runningTotal = 0
        for (number in numbers) {
            runningTotal += number
        }
        return runningTotal
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part2(testInput) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
