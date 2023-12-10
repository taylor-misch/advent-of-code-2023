fun main() {

    data class Reading(
        val readings: List<Int>,
        var nextNumber: Int = 0
    )

    fun parseReading(input: String): List<Int> {
        val readings = mutableListOf<Int>()
        for (reading in input.split(" ")) {
            readings.add(reading.toInt())
        }
        return readings
    }

    fun parseReadings(input: List<String>): List<List<Int>> {
        val readings = mutableListOf<List<Int>>()
        for (line in input) {
            readings.add(parseReading(line))
        }
        return readings
    }

    fun allNumbersAreZero(readings: List<Int>): Boolean {
        for (reading in readings) {
            if (reading != 0) {
                return false
            }
        }
        return true
    }

    fun findDifferences(readings: List<Int>): List<Int> {
        return if (allNumbersAreZero(readings)) {
            readings
        } else {
            val diffList = mutableListOf<Int>()
            var index = 1
            while (index < readings.size) {
                diffList.add(readings[index] - readings[index - 1])
                index++
            }
            val lowerList = findDifferences(diffList)
            diffList.add(lowerList.last() + diffList.last())
            diffList
        }
    }

    fun part1(input: List<String>): Int {
        val readings = parseReadings(input)
        var result = 0;
        for (reading in readings) {
            val lastNumber = findDifferences(reading).last()
            val nextNumber = reading.last() + lastNumber
            println("Next number: $nextNumber")
            result += nextNumber
        }
        println("Result: $result")
        return result
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114)
    check(part2(testInput) == 2)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}