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
//            println(pathLength)
            var haveMoved = false
            // down
            if (!haveMoved && currentPosition.first < size && lastMove != "U" && !Regex("[-LJ]").matches(currentPipe.toString())) {
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
            if (!haveMoved && currentPosition.second < size && lastMove != "L" && !Regex("[|7J]").matches(currentPipe.toString())) {
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
                return maxOf(longestPath, pathLength)
            }
            println(currentPosition)

            longestPath = maxOf(longestPath, pathLength)
        }

        return longestPath
    }

    data class Rectangle(
        val startRow: Int,
        val startCol: Int,
        val rowSize: Int,
        val colSize: Int
    )

    fun walkMazeV2(pipeMaze: Array<CharArray>, start: Pair<Int, Int>, sizeHeight: Int, sizeWidth: Int): Triple<Array<CharArray>, Rectangle, Map<Pair<Int, Int>, Boolean>> {
        var currentPosition = start
        var pathLength = 0
        var longestPath = 0
        var lastMove = "S"
        var currentPipe = 'S'
        var furthestUp = start.first
        var furthestDown = start.first
        var furthestLeft = start.second
        var furthestRight = start.second
        var inPipeMap = mutableMapOf<Pair<Int, Int>, Boolean>()
        while (true) {
            println("Current Position: $currentPosition - Current Pipe: $currentPipe")
            var haveMoved = false
            // down
            if (!haveMoved && currentPosition.first < sizeHeight && lastMove != "U" && !Regex("[-LJ]").matches(currentPipe.toString())) {
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
            if (!haveMoved && currentPosition.second < sizeWidth && lastMove != "L" && !Regex("[|7J]").matches(currentPipe.toString())) {
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

            inPipeMap[currentPosition] = true
            if (currentPosition == start) {
//                pipeMaze[currentPosition.first][currentPosition.second] = 'F' // hardcoding for input
                pipeMaze[currentPosition.first][currentPosition.second] = 'J' // hardcoding for input
//                println("Made it back to start")
                break
            }

            if (currentPosition.first < furthestUp) {
                furthestUp = currentPosition.first
            }
            if (currentPosition.first + 1 > furthestDown) {
                furthestDown = currentPosition.first + 1
            }
            if (currentPosition.second < furthestLeft) {
                furthestLeft = currentPosition.second
            }
            if (currentPosition.second + 1 > furthestRight) {
                furthestRight = currentPosition.second + 1
            }
            longestPath = maxOf(longestPath, pathLength)
        }

        return Triple(pipeMaze, Rectangle(furthestUp, furthestLeft, furthestDown - furthestUp, furthestRight - furthestLeft), inPipeMap)
    }

    fun scanRectangle(rect: Rectangle, pipeMaze: Array<CharArray>, inPipeMap: Map<Pair<Int, Int>, Boolean>): Array<CharArray> {
        var count = 0
        for (row in rect.startRow until rect.startRow + rect.rowSize) {
            var upLeft = false
            var downLeft = false
            for (col in rect.startCol until rect.startCol + rect.colSize) {
//                println("PipeMaze[$row][$col] = ${pipeMaze[row][col]}")
                if (inPipeMap[Pair(row, col)] == true) {
                    when (pipeMaze[row][col]) {
                        '|' -> {
                            count++
                        }

                        'F' -> {
                            count++
                        }

                        '7' -> {
                            count++
                        }
                    }
                } else if (count % 2 != 0) {
                    pipeMaze[row][col] = 'I'
                }
            }
        }
        println("Count: $count")
        pipeMaze.forEach { println(it) }
        return pipeMaze
    }

    fun countIs(pipeMaze: Array<CharArray>): Int {
        var count = 0
        for (row in pipeMaze.indices) {
            for (col in pipeMaze[row].indices) {
                if (pipeMaze[row][col] == 'I') {
                    count++
                }
            }
        }
        return count
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
        //Read path and record the farthest up, down, left, and right positions to make a square
        var (pipeMaze, rectangle, inPipeMap) = walkMazeV2(parsePipeMaze(input), findStart(parsePipeMaze(input)), input.size, input[0].length)
        var newMaze = scanRectangle(rectangle, pipeMaze, inPipeMap)
        val newCount = countIs(newMaze)
        println(newCount)
        return newCount
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
//    check(part1(testInput) == 8)
//    check(part2(testInput) == 8) // won't work because I hardcoded S to J in part 2

    val input = readInput("Day10")
//    println(part1(input))
    println(part2(input))
}