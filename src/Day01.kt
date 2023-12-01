fun main() {
    fun part1(input: List<String>): Int {

        val numbers = mutableListOf<Int>()
        for (line in input) {
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
            val number = firstNumber + lastNumber
            numbers.add(number.toInt())
        }

        var runningTotal = 0
        for (number in numbers) {
            runningTotal += number
        }

        return runningTotal
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val input = readInput("Day01")
    part1(input).println()
}
