fun main() {

    fun findColumnsWithoutGalaxy(input: List<String>): List<Int> {
        val columnsWithoutGalaxy = mutableListOf<Int>()
        for (col in input[0].indices) {
            if (input.map { it[col] }.count { it == '#' } == 0) {
                columnsWithoutGalaxy.add(col)
            }
        }
        return columnsWithoutGalaxy
    }

    fun findRowsWithoutGalaxy(input: List<String>): List<Int> {
        val rowsWithoutGalaxy = mutableListOf<Int>()
        for (row in input.indices) {
            if (input[row].count { it == '#' } == 0) {
                rowsWithoutGalaxy.add(row)
            }
        }
        return rowsWithoutGalaxy
    }

    fun duplicateRow(input: List<String>, row: Int): List<String> {
        val copyOfRow = input[row]
        val inputCopy = input.toMutableList()
        inputCopy.add(row, copyOfRow)
        return inputCopy
    }

    fun duplicateColumn(input: List<String>, col: Int): List<String> {
        val inputCopy = input.toMutableList()
        for (row in inputCopy.indices) {
            val copyOfChar = inputCopy[row][col]
            val rowCopy = inputCopy[row].toMutableList()
            rowCopy.add(col, copyOfChar)
            inputCopy[row] = rowCopy.joinToString("")
        }
        return inputCopy
    }


    fun buildUniverse(input: List<String>): List<String> {
        val columnsWithoutGalaxy = findColumnsWithoutGalaxy(input)
        val rowsWithoutGalaxy = findRowsWithoutGalaxy(input)

        println("columns without galaxy: $columnsWithoutGalaxy")
        println("rows without galaxy: $rowsWithoutGalaxy")

        var buildingUniverse = input
        for (col in columnsWithoutGalaxy.reversed()) {
            buildingUniverse = duplicateColumn(buildingUniverse, col)
        }

        for (row in rowsWithoutGalaxy.reversed()) {
            buildingUniverse = duplicateRow(buildingUniverse, row)
        }

        buildingUniverse.forEach(::println)
        return buildingUniverse

    }

    fun findGalaxies(input: List<String>): List<Pair<Int, Int>> {
        val galaxies = mutableListOf<Pair<Int, Int>>()
        for (row in input.indices) {
            for (col in input[row].indices) {
                if (input[row][col] == '#') {
                    galaxies.add(Pair(row, col))
                }
            }
        }
        return galaxies
    }

    fun part1(input: List<String>): Int {
        var expandedUniverse = buildUniverse(input)
        var galaxies = findGalaxies(expandedUniverse)
        var distanceSum = 0
        for ((galaxyIndex, galaxy) in galaxies.withIndex()) {
            for ((otherGalaxyIndex, otherGalaxy) in galaxies.withIndex()) {
                if (galaxyIndex < otherGalaxyIndex) {
                    val distance = Math.abs(galaxy.first - otherGalaxy.first) + Math.abs(galaxy.second - otherGalaxy.second)
                    println("distance between ${galaxyIndex + 1}[$galaxy] and ${otherGalaxyIndex + 1}[$otherGalaxy]: $distance")
                    distanceSum += distance
                }
            }
        }
        println("distance sum: $distanceSum")
        return distanceSum
    }

    fun part2(input: List<String>): Long {
        val increaseFactor = 1000000
        val galaxies = findGalaxies(input)
        val columnsWithoutGalaxy = findColumnsWithoutGalaxy(input)
        val rowsWithoutGalaxy = findRowsWithoutGalaxy(input)
        var distanceSum = 0L
        for ((galaxyIndex, galaxy) in galaxies.withIndex()) {
            for ((otherGalaxyIndex, otherGalaxy) in galaxies.withIndex()) {
                if (galaxyIndex < otherGalaxyIndex) {
                    var additionalRowFactor: Long
                    var rowsCrossed: Int
                    var additionalColFactor: Long
                    var columnsCrossed: Int
                    if (galaxy.first > otherGalaxy.first) {
                        rowsCrossed = rowsWithoutGalaxy.count { it in otherGalaxy.first..galaxy.first }
                        additionalRowFactor = (increaseFactor * rowsCrossed).toLong()
                    } else {
                        rowsCrossed = rowsWithoutGalaxy.count { it in galaxy.first..otherGalaxy.first }
                        additionalRowFactor = (increaseFactor * rowsCrossed).toLong()
                    }
                    if (galaxy.second > otherGalaxy.second) {
                        columnsCrossed = columnsWithoutGalaxy.count { it in otherGalaxy.second..galaxy.second }
                        additionalColFactor = (increaseFactor * columnsCrossed).toLong()
                    } else {
                        columnsCrossed = columnsWithoutGalaxy.count { it in galaxy.second..otherGalaxy.second }
                        additionalColFactor = (increaseFactor * columnsCrossed).toLong()
                    }
                    val newDistance =
                        Math.abs(galaxy.first - otherGalaxy.first) + Math.abs(galaxy.second - otherGalaxy.second) + additionalColFactor + additionalRowFactor - rowsCrossed - columnsCrossed
                    distanceSum += newDistance
                }
            }
        }
        println("distance sum: $distanceSum")
        return distanceSum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
//    check(part1(testInput) == 374)
//    check(part2(testInput) == 1030)

    val input = readInput("Day11")
//    println(part1(input))
    println(part2(input))
}