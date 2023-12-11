fun main() {
    fun parsePipeMaze(input: List<String>): Array<CharArray> {
        return input.map { it.toCharArray() }.toTypedArray()
    }

    fun findStart(pipeMaze: Array<CharArray>): Pair<Int, Int> {
        for (row in pipeMaze.indices) {
            for (col in pipeMaze[row].indices) {
                if (pipeMaze[row][col] == 'S') {
                    return Pair(row, col)
                }
            }
        }
        return Pair(0, 0)
    }

    fun walkMaze(pipeMaze: Array<CharArray>, start: Pair<Int, Int>, size: Int): Int {
        var currentPosition = start
        var pathLength = 0
        var longestPath = 0
        var lastMove = "S"
        var currentPipe = 'S'
        while (true) {
            println(pathLength)
            var haveMoved = false
            // down
            if (!haveMoved && currentPosition.first < size - 1 && lastMove != "U" && !Regex("[-LJ]").matches(currentPipe.toString())) {
                val validChars = Regex("[|JLS]")
                val downMove = pipeMaze[currentPosition.first + 1][currentPosition.second]
                if (validChars.matches(downMove.toString())) {
                    currentPosition = Pair(currentPosition.first + 1, currentPosition.second)
                    pathLength++
                    lastMove = "D"
                    haveMoved = true
                    currentPipe = downMove
                }
            }
            // left
            if (!haveMoved && currentPosition.second > 0 && lastMove != "R" && !Regex("[|FL]").matches(currentPipe.toString())) {
                val validChars = Regex("[LFS-]")
                val leftMove = pipeMaze[currentPosition.first][currentPosition.second - 1]
                if (validChars.matches(leftMove.toString())) {
                    currentPosition = Pair(currentPosition.first, currentPosition.second - 1)
                    pathLength++
                    lastMove = "L"
                    haveMoved = true
                    currentPipe = leftMove

                }
            }
            // right
            if (!haveMoved && currentPosition.second < size - 1 && lastMove != "L" && !Regex("[|7J]").matches(currentPipe.toString())) {
                val validChars = Regex("[7JS-]")
                val rightMove = pipeMaze[currentPosition.first][currentPosition.second + 1]
                if (validChars.matches(rightMove.toString())) {
                    currentPosition = Pair(currentPosition.first, currentPosition.second + 1)
                    pathLength++
                    lastMove = "R"
                    haveMoved = true
                    currentPipe = rightMove

                }
            }
            // up
            if (!haveMoved && currentPosition.first > 0 && lastMove != "D" && !Regex("[-F7]").matches(currentPipe.toString())) {
                val validChars = Regex("[|F7S]")
                val upMove = pipeMaze[currentPosition.first - 1][currentPosition.second]
                if (validChars.matches(upMove.toString())) {
                    currentPosition = Pair(currentPosition.first - 1, currentPosition.second)
                    pathLength++
                    lastMove = "U"
                    haveMoved = true
                    currentPipe = upMove
                }
            }

            if (currentPosition == start) {
                println("Made it back to start")
                break
            }

            longestPath = maxOf(longestPath, pathLength)
        }

        return longestPath
    }

    fun part1(input: List<String>): Int {
        val pipeMaze = parsePipeMaze(input)
        pipeMaze.forEach(::println)
        val start = findStart(pipeMaze)
        println(start)
        val longestPath = walkMaze(pipeMaze, start, input.size)
        println(Math.ceil(longestPath / 2.0).toInt())
        return Math.ceil(longestPath / 2.0).toInt()
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 8)
//    check(part2(testInput) == 1)

    val input = readInput("Day10")
    println(part1(input))
//    println(part2(input))
}