fun main() {

    val regex = Regex("[^0-9.]")

    data class PartNumber(
        val number: Int,
        val line: Int,
        val startIndex: Int,
        val endIndex: Int
    )

    fun getNumberAndStartAndEndPoints(line: String, lineNumber: Int, startIndex: Int): PartNumber {
        var numberString = ""
        var endIndex = startIndex
        while (endIndex < line.length && line[endIndex].isDigit()) {
            numberString += line[endIndex]
            endIndex++
        }
        return PartNumber(numberString.toInt(), lineNumber, startIndex, endIndex)
    }

    fun isSymbolInProximity(input: List<String>, lineWithNumber: Int, startIndex: Int, endIndex: Int): Boolean {
        var internalStartIndex = startIndex
        if (startIndex != 0) {
            internalStartIndex -= 1
        }
        var internalEndIndex = endIndex
        if (endIndex <= input[lineWithNumber].length - 1) {
            internalEndIndex += 1
        }
        if (lineWithNumber == 0) {
            // only check the current line and the line below
            val currentSymbol = regex.containsMatchIn(input[lineWithNumber].substring(internalStartIndex, internalEndIndex))
            val belowSymbol = regex.containsMatchIn(input[lineWithNumber + 1].substring(internalStartIndex, internalEndIndex))
            return currentSymbol || belowSymbol
        } else if (lineWithNumber == input.size - 1) {
            // only check the current line and the line above
            val aboveSymbol = regex.containsMatchIn(input[lineWithNumber - 1].substring(internalStartIndex, internalEndIndex))
            val currentSymbol = regex.containsMatchIn(input[lineWithNumber].substring(internalStartIndex, internalEndIndex))
            return aboveSymbol || currentSymbol
        } else {
            // check the current line and the line above and below
            val aboveSymbol = regex.containsMatchIn(input[lineWithNumber - 1].substring(internalStartIndex, internalEndIndex))
            val currentSymbol = regex.containsMatchIn(input[lineWithNumber].substring(internalStartIndex, internalEndIndex))
            val belowSymbol = regex.containsMatchIn(input[lineWithNumber + 1].substring(internalStartIndex, internalEndIndex))
            return aboveSymbol || currentSymbol || belowSymbol
        }
    }

    fun getPartNumbers(input: List<String>): List<PartNumber> {
        val partNumbers = mutableListOf<PartNumber>()
        for ((lineIndex, line) in input.withIndex()) {
            var index = 0;
            while (index < line.length) {
                if (line[index].isDigit()) {
                    val partNumber = getNumberAndStartAndEndPoints(line, lineIndex, index)
                    partNumbers.add(partNumber)
                    index = partNumber.endIndex
                } else {
                    index++
                }
            }
        }
        return partNumbers
    }

    fun part1(input: List<String>): Int {
        val partNumbers = getPartNumbers(input)

        var runningTotal = 0
        for (number in partNumbers) {
            val isValid = isSymbolInProximity(input, number.line, number.startIndex, number.endIndex)
            println("Number: ${number.number}, Line: ${number.line}, isValid: $isValid")
            if (isValid) {
                runningTotal += number.number
            }
        }
        println("Running total: $runningTotal")
        return runningTotal
    }


    data class Gear(
        val lineNumber: Int,
        val index: Int,
        val connectedNumbers: List<PartNumber>
    )


    fun findAllGearSymbols(input: List<String>): List<Gear> {
        val gears = mutableListOf<Gear>()
        for ((lineIndex, line) in input.withIndex()) {
            var index = 0;
            while (index < line.length) {
                if (line[index] == '*') {
                    val gear = Gear(lineIndex, index, mutableListOf())
                    gears.add(gear)
                }
                index++
            }
        }
        return gears
    }

    fun findConnectedNumbers(gear: Gear, partNumbers: List<PartNumber>): List<PartNumber> {
        val connectedNumbers = mutableListOf<PartNumber>()
        for (partNumber in partNumbers) {
            if (partNumber.line == gear.lineNumber ||
                partNumber.line == gear.lineNumber - 1 ||
                partNumber.line == gear.lineNumber + 1
            ) {
                if (partNumber.startIndex-1 <= gear.index &&
                    partNumber.endIndex >= gear.index
                ) {
                    connectedNumbers.add(partNumber)
                }
            }
        }
        return connectedNumbers;
    }

    fun part2(input: List<String>): Int {
        val gears = findAllGearSymbols(input)
        val partNumbers = getPartNumbers(input)
        val completedGears = mutableListOf<Gear>()
        for (gear in gears) {
            val connectedNumbers = findConnectedNumbers(gear, partNumbers)
            completedGears.add(Gear(gear.lineNumber, gear.index, connectedNumbers))
        }

        var runningTotal = 0
        for (gear in completedGears) {
            println("Line: ${gear.lineNumber}, connectedNumbers: ${gear.connectedNumbers}")
            if (gear.connectedNumbers.size == 2) {
                runningTotal += (gear.connectedNumbers[0].number * gear.connectedNumbers[1].number)
            }
        }
        println("Running total: $runningTotal")
        return runningTotal
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}