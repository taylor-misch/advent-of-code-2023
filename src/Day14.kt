fun main() {

    fun printCharArray(input: Array<CharArray>) {
        input.forEach { println(it) }
    }

    fun parseInputToArrayOfCharArray(input: List<String>): Array<CharArray> {
//        print("\nInitial input: \n")
        val parsedInput = input.map { it.toCharArray() }.toTypedArray()
//        printCharArray(parsedInput)
        return parsedInput
    }

    fun calculateLoad(input: Array<CharArray>): Int {
        var load = 0
        input.forEachIndexed { rowIndex, row ->
            row.forEach { char ->
                if (char == 'O') {
                    load += 1 * (input.size - rowIndex)
                }
            }
        }
//        println("Load: $load")
        return load
    }

    fun rollRocksUp(input: Array<CharArray>): Array<CharArray> {
//        println("\nRolling rocks up")
        input.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, char ->
                if (char == 'O') {
                    var offset = rowIndex
                    while (offset > 0 && input[offset - 1][colIndex] == '.') {
                        offset -= 1
                    }
                    input[rowIndex][colIndex] = '.'
                    input[offset][colIndex] = 'O'
                }
            }
        }
        return input
    }

    fun rollRocksDown(input: Array<CharArray>): Array<CharArray> {
//        println("\nRolling rocks down")
        for (rowIndex in input.size - 1 downTo 0) {
            for (colIndex in 0 until input[0].size) {
                if (input[rowIndex][colIndex] == 'O') {
                    var offset = rowIndex
                    while (offset < input.size - 1 && input[offset + 1][colIndex] == '.') {
                        offset += 1
                    }
                    input[rowIndex][colIndex] = '.'
                    input[offset][colIndex] = 'O'
                }
            }
        }
        return input
    }

    fun rollRocksLeft(input: Array<CharArray>): Array<CharArray> {
//        println("\nRolling rocks left")
        input.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, char ->
                if (char == 'O') {
                    var offset = colIndex
                    while (offset > 0 && input[rowIndex][offset - 1] == '.') {
                        offset -= 1
                    }
                    input[rowIndex][colIndex] = '.'
                    input[rowIndex][offset] = 'O'
                }
            }
        }
        return input
    }

    fun rollRocksRight(input: Array<CharArray>): Array<CharArray> {
//        println("\nRolling rocks right")
        for (rowIndex in input.indices) {
            for (colIndex in input[0].size - 1 downTo 0) {
                if (input[rowIndex][colIndex] == 'O') {
                    var offset = colIndex
                    while (offset < input[0].size - 1 && input[rowIndex][offset + 1] == '.') {
                        offset += 1
                    }
                    input[rowIndex][colIndex] = '.'
                    input[rowIndex][offset] = 'O'
                }
            }
        }
        return input
    }

    fun rotate90DegreesClockwise(matrix: Array<CharArray>): Array<CharArray> {
        val numRows = matrix.size
        val numCols = matrix[0].size
        val rotatedMatrix = Array(numCols) { CharArray(numRows) { ' ' } }
        for (i in 0 until numRows) {
            for (j in 0 until numCols) {
                rotatedMatrix[j][numRows - 1 - i] = matrix[i][j]
            }
        }
        return rotatedMatrix
    }

    fun part1(input: List<String>): Int {
        val parsedInput = parseInputToArrayOfCharArray(input)
        var output = parsedInput
        output = rollRocksUp(output)
//        output = rollRocksLeft(output)
//        output = rollRocksDown(output)
//        output = rollRocksRight(output)
//        printCharArray(output)
        val calculatedLoad = calculateLoad(output)
        return calculatedLoad
    }

    fun calculate(targetValue: Int, step: Int, startValue: Int, numberUnderConsideration: Int): Int {
        var value = startValue
        while (value < targetValue) {
            value += step
            if (value == targetValue) {
                println("Found $numberUnderConsideration")
                return numberUnderConsideration
            }
        }
        return 0
    }


    // This one I had to fudge a bit because I couldn't come up with the math to make a performant solutions
    // I saw that there was a pattern of the calculated loads repeating, so I just ran the simulation for 10k iterations
    // and mapped the most commonly recurring numbers. Once I had those numbers I saw that the number of values that occurred
    // more than a few times was equal to the number of cycles that between re-occurrences of the same number (after a
    // "breaking in period" where all kinds of random numbers would pop up). From there it was just a matter of seeing
    // which cycle would be the one that would be the 1 billionth iteration. I'm sure there's a more elegant solution,
    // but I got it on the first try once I figured out the math so I'll take it!
    fun part2(input: List<String>): Int {
        val parsedInput = parseInputToArrayOfCharArray(input)
        var output = parsedInput
        val results = mutableMapOf<Int, Pair<Int, Int>>()
        for (i in 0 until 10000) {
            output = rollRocksUp(output)
            output = rollRocksLeft(output)
            output = rollRocksDown(output)
            output = rollRocksRight(output)
            calculateLoad(output).let { results[it] = Pair(results.getOrDefault(it, Pair(0, 0)).first + 1, i) }
        }

        val countOfNumbersUnderConsideration = results.count { it.value.first > 5 }
        println(countOfNumbersUnderConsideration)

        results.forEach { (key, value) ->
            run {
                if (value.first > 1) {
                    println("Load $key: ${value.first} times, last at ${value.second}")
                    val result = calculate(1000000000, countOfNumbersUnderConsideration, value.second + 1, key)
                    if (result != 0) {
                        return result
                    }
                }
            }
        }

        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
//    check(part1(testInput) == 136)
//    check(part2(testInput) == 64)

    val input = readInput("Day14")
//    println(part1(input))
    println(part2(input))
}