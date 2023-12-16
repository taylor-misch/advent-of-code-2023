enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

fun main() {

    data class Laser(
        var x: Int,
        var y: Int,
        var direction: Direction
    )

    fun printGrid(array: Array<CharArray>) {
        array.forEach { row ->
            row.forEach { char ->
                print(char)
            }
            println()
        }
    }

    fun parseInputToArray(input: List<String>): Array<CharArray> {
        val array = Array(input.size) { CharArray(input[0].length) }
        for ((index, line) in input.withIndex()) {
            for ((charIndex, char) in line.withIndex()) {
                array[index][charIndex] = char
            }
        }
        return array
    }

    fun updateDirection(
        lasers: MutableList<Laser>,
        grid: Array<CharArray>,
        gridTracker: Array<CharArray>
    ): Pair<MutableList<Laser>, Array<CharArray>> {
        val addLasers = mutableListOf<Laser>()
        lasers.forEachIndexed() { index, laser ->
//            println("Laser $index: ${laser.x}, ${laser.y}")
            if (gridTracker[laser.x][laser.y] != '#') {
                gridTracker[laser.x][laser.y] = '#'
            }
            //Check current position
            when (grid[laser.y][laser.x]) {
                '/' -> {
                    when (laser.direction) {
                        Direction.UP -> {
                            laser.direction = Direction.RIGHT
                        }

                        Direction.DOWN -> {
                            laser.direction = Direction.LEFT
                        }

                        Direction.LEFT -> {
                            laser.direction = Direction.DOWN
                        }

                        Direction.RIGHT -> {
                            laser.direction = Direction.UP
                        }
                    }
                }

                '\\' -> {
                    when (laser.direction) {
                        Direction.UP -> {
                            laser.direction = Direction.LEFT
                        }

                        Direction.DOWN -> {
                            laser.direction = Direction.RIGHT
                        }

                        Direction.LEFT -> {
                            laser.direction = Direction.UP
                        }

                        Direction.RIGHT -> {
                            laser.direction = Direction.DOWN
                        }
                    }
                }

                '|' -> {
                    when (laser.direction) {
                        Direction.UP -> {
                            laser.direction = Direction.UP
                        }

                        Direction.DOWN -> {
                            laser.direction = Direction.DOWN
                        }

                        Direction.LEFT, Direction.RIGHT -> {
                            addLasers.add(Laser(laser.x, laser.y, Direction.UP))
                            laser.direction = Direction.DOWN
                        }
                    }
                }

                '-' -> {
                    when (laser.direction) {
                        Direction.UP, Direction.DOWN -> {
                            addLasers.add(Laser(laser.x, laser.y, Direction.RIGHT))
                            laser.direction = Direction.LEFT
                        }

                        Direction.LEFT -> {
                            laser.direction = Direction.LEFT
                        }

                        Direction.RIGHT -> {
                            laser.direction = Direction.RIGHT
                        }
                    }
                }

            }
        }
        lasers.addAll(addLasers)
        for (laser in lasers) {
            when (laser.direction) {
                Direction.UP -> {
                    laser.y--
                }

                Direction.DOWN -> {
                    laser.y++
                }

                Direction.LEFT -> {
                    laser.x--
                }

                Direction.RIGHT -> {
                    laser.x++
                }
            }
        }
        return Pair(lasers, gridTracker)
    }

    fun removeLasers(lasers: List<Laser>, gridWidth: Int, gridHeight: Int): MutableList<Laser> {
        val lasersToKeep = mutableListOf<Laser>()
        for (laser in lasers) {
            if (laser.x in 0 until gridWidth && laser.y in 0 until gridHeight) {
                lasersToKeep.add(laser)
            }
        }
        return lasersToKeep
    }

    fun scoreGrid(grid: Array<CharArray>): Int {
        var score = 0
        for (row in grid) {
            for (char in row) {
                if (char == '#') {
                    score++
                }
            }
        }
        return score
    }

    fun runSimulation(startingX: Int, startingY: Int, startingDirection: Direction, input: List<String>): Int {
        val grid = parseInputToArray(input)
//        printGrid(grid)
        var lasers = mutableListOf(Laser(startingX, startingY, startingDirection))
        var gridTracker = parseInputToArray(input)
        var energized = 0
        var sameCount = 0
        while (lasers.isNotEmpty() && sameCount < 50) {
            val previouslyEnergized = energized
            val result = updateDirection(lasers, grid, gridTracker)
            lasers = result.first
            gridTracker = result.second
            lasers = removeLasers(lasers, grid[0].size, grid.size)
            energized = scoreGrid(gridTracker)
            if (energized == previouslyEnergized) {
                sameCount++
//                println("Same count: $sameCount")
            } else {
                sameCount = 0
            }
        }
        val result = scoreGrid(gridTracker)
//        println("Result: $result")
        return result
    }

    fun part1(input: List<String>): Int {
        return runSimulation(0, 0, Direction.RIGHT, input)
    }

    fun part2(input: List<String>): Int {
        var bestResult = 0
        println("Running simulation...")
        println("Top")
        for (i in 0 until input[0].length) {
            val result = runSimulation(i, 0, Direction.DOWN, input)
            bestResult = maxOf(bestResult, result)
        }

        println("Bottom")
        for (i in 0 until input[0].length) {
            val result = runSimulation(i, input.size - 1, Direction.UP, input)
            bestResult = maxOf(bestResult, result)
        }

        println("Left")
        for (i in input.indices) {
            val result = runSimulation(0, i, Direction.RIGHT, input)
            bestResult = maxOf(bestResult, result)
        }

        println("Right")
        for (i in input.indices) {
            val result = runSimulation(i, input.size - 1, Direction.UP, input)
            bestResult = maxOf(bestResult, result)
        }
        println("Best result: $bestResult")
        return bestResult
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
//    check(part1(testInput) == 46)
    check(part2(testInput) == 51)

    val input = readInput("Day16")
//    println(part1(input))
    println(part2(input))
}