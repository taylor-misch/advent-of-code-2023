//enum for vertical or horizontal mirror
enum class MirrorType {
    VERTICAL,
    HORIZONTAL,
    NONE
}

fun main() {


    data class MirrorPuzzle(
        var input: List<String>,
        var resultValue: Int = 0,
        var mirrorType: MirrorType = MirrorType.NONE
    )

    fun printPuzzle(puzzle: List<String>) {
        puzzle.forEach { line ->
            println(line)
        }
        println()
    }

    fun printPuzzles(puzzles: List<MirrorPuzzle>) {
        puzzles.forEach() { puzzle ->
            printPuzzle(puzzle.input)

        }
    }

    fun parsePuzzles(input: List<String>): List<MirrorPuzzle> {
        val emptyLines = mutableListOf<Int>()
        for ((index, line) in input.withIndex()) {
            if (line.isEmpty()) {
                emptyLines.add(index)
            }
        }
        emptyLines.add(input.size)
//        println(emptyLines)
        val puzzleList = mutableListOf<MirrorPuzzle>()
        var previousLineNumber = 0
        emptyLines.forEach {
            val puzzle = input.subList(previousLineNumber, it)
            previousLineNumber = it + 1
            puzzleList.add(MirrorPuzzle(puzzle))
        }

//        printPuzzles(puzzleList)
        return puzzleList
    }

    fun lineMirrors(input: String, index: Int): Boolean {
        var left = index
        var right = index + 1
        while (left >= 0 && right < input.length) {
            if (input[left] == input[right]) {
                left--
                right++
            } else {
                return false
            }
        }
        return true
    }

    fun isMirror(input: List<String>, line: Int, index: Int): Boolean {
        if (lineMirrors(input[line], index)) {
            if (line + 1 <= input.size - 1) {
                if (isMirror(input, line + 1, index)) {
                    return true
                } else {
                    return false
                }
            } else {
                return true
            }

        } else {
            return false
        }
    }

    fun lineMirrorsV2(input: String, index: Int, smudgeFound: Boolean): Pair<Boolean, Boolean> {
        var left = index
        var right = index + 1
        var foundSmudge = smudgeFound
        while (left >= 0 && right < input.length) {
//            println("left: ${input[left]}, right: ${input[right]} smudgeFound: $foundSmudge")
            if (input[left] == input[right]) {
                left--
                right++
            } else {
                if (foundSmudge) {
//                    println("Wrong Smudge")
                    return Pair(false, false)
                } else {
//                    println("Smudge found")
                    foundSmudge = true
                    left--
                    right++
                }
            }
        }
        return Pair(true, foundSmudge)
    }

    fun isMirrorV2(input: List<String>, line: Int, index: Int, smudgeFound: Boolean): Pair<Boolean, Boolean> {
//        println("Line: $line, index: $index, smudgeFound: $smudgeFound")
        val (result, foundSmudge) = lineMirrorsV2(input[line], index, smudgeFound)
        if (result) {
            if (line + 1 <= input.size - 1) {
                val (result2, foundSmudge2) = isMirrorV2(input, line + 1, index, foundSmudge)
                if (result2) {
                    return Pair(true, foundSmudge2)
                } else {
                    return Pair(false, false)
                }
            } else {
                return Pair(true, foundSmudge)
            }

        } else {
            return Pair(false, false)
        }
    }

    fun checkForMirror(puzzle: MirrorPuzzle, mirrorType: MirrorType, part1: Boolean): MirrorPuzzle {
//        println("\n\n\nChecking for $mirrorType mirror")
        printPuzzle(puzzle.input)
        var foundSmudge = false
        for (index in 0 until puzzle.input[0].length - 1) {
            if (part1 && isMirror(puzzle.input, 0, index)) {
//                println("$mirrorType mirror found at index $index")
                puzzle.mirrorType = mirrorType
                puzzle.resultValue = index
                return puzzle
            }

            if (!part1) {
                val (result, foundSmudge2) = isMirrorV2(puzzle.input, 0, index, foundSmudge)
                foundSmudge = foundSmudge2
                if (result && foundSmudge) {
//                    println("$mirrorType mirror found at index $index")
                    puzzle.mirrorType = mirrorType
                    puzzle.resultValue = index
                    return puzzle
                }
            }
        }
        return puzzle
    }

    fun calculateResult(puzzles: List<MirrorPuzzle>): Int {
        var result = 0
        puzzles.forEach { puzzle ->
            if (puzzle.mirrorType == MirrorType.VERTICAL) {
                result += puzzle.resultValue + 1
            }
            if (puzzle.mirrorType == MirrorType.HORIZONTAL) {
                result += (puzzle.resultValue + 1) * 100
            }
        }
        println("Result: $result")
        return result
    }

    fun rotateMinus90DegreesClockwise(matrix: Array<Array<Char>>): Array<Array<Char>> {
        val numRows = matrix.size
        val numCols = matrix[0].size

        val rotatedMatrix = Array(numCols) { Array(numRows) { ' ' } }

        for (i in 0 until numRows) {
            for (j in 0 until numCols) {
                rotatedMatrix[numCols - 1 - j][i] = matrix[i][j]
            }
        }

        return rotatedMatrix
    }

    fun parseInputToCharArray(input: List<String>): Array<Array<Char>> {
        val charArray = Array(input.size) { Array(input[0].length) { ' ' } }
        for ((index, line) in input.withIndex()) {
            for ((charIndex, char) in line.withIndex()) {
                charArray[index][charIndex] = char
            }
        }
        return charArray
    }

    fun parseCharArrayToInput(charArray: Array<Array<Char>>): List<String> {
        val input = mutableListOf<String>()
        for (line in charArray) {
            var lineString = ""
            for (char in line) {
                lineString += char
            }
            input.add(lineString)
        }
        return input
    }

    fun findReflections(puzzles: List<MirrorPuzzle>, part1: Boolean): List<MirrorPuzzle> {
        puzzles.forEach { puzzle ->
            checkForMirror(puzzle, MirrorType.VERTICAL, part1)
            if (puzzle.mirrorType == MirrorType.VERTICAL) {
//                println("Vertical mirror found")
            } else {
//                println("No vertical mirror found")
                val charArrayInput = parseInputToCharArray(puzzle.input)
                val rotatedCharArray = rotateMinus90DegreesClockwise(charArrayInput)
                val parsedBackInput = parseCharArrayToInput(rotatedCharArray)
                puzzle.input = parsedBackInput
                checkForMirror(puzzle, MirrorType.HORIZONTAL, part1)
                if (puzzle.mirrorType == MirrorType.HORIZONTAL) {
//                    println("Horizontal mirror found")
                } else {
//                    println("No mirror found")

                }
            }
        }
        return puzzles
    }


    fun part1(input: List<String>): Int {
        val puzzles = parsePuzzles(input)
        val solvedPuzzles = findReflections(puzzles, true)
        return calculateResult(solvedPuzzles)
    }

    fun part2(input: List<String>): Int {
        val puzzles = parsePuzzles(input)
        val solvedPuzzles = findReflections(puzzles, false)
        return calculateResult(solvedPuzzles)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 405)
    check(part2(testInput) == 400)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}