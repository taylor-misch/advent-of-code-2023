enum class Sign {
    EQUAL,
    MINUS
}

fun main() {

    fun increaseCurrentValue(currentValue: Long, character: Char): Long {
        var result = currentValue
        val asciiValue = character.code
        result += asciiValue
        result *= 17
        result %= 256
        return result
    }

    fun part1(input: List<String>): Long {
        var totalResult = 0L
        val listOfSteps = input.joinToString().split(",")
        for (step in listOfSteps) {
            var currentValue = 0L
            for (character in step) {
                currentValue = increaseCurrentValue(currentValue, character)
                println("currentValue: $currentValue")
            }
            totalResult += currentValue
        }
        println("totalResult: $totalResult")
        return totalResult
    }


    data class Lens(
        val key: String,
        var focalLength: Int,
        var sign: Sign
    )

    fun getBoxId(lens: Lens): Int {
        var currentValue = 0
        for (character in lens.key) {
            currentValue = increaseCurrentValue(currentValue.toLong(), character).toInt()
        }
        return currentValue
    }

    fun parseLens(input: List<String>): List<Lens> {
        val lensInstructions = input.joinToString().split(",")
        val lensList = mutableListOf<Lens>()
        for (lens in lensInstructions) {
            var signIndex: Int
            if (lens.contains('-')) {
                signIndex = lens.indexOf('-')
            } else {
                signIndex = lens.indexOf('=')
            }
            val key = lens.substring(0, signIndex)
            val sign = if (lens.substring(signIndex, signIndex + 1) == "-") Sign.MINUS else Sign.EQUAL
            var focalLength = 0
            if (sign == Sign.EQUAL) {
                focalLength = lens.substring(signIndex + 1).toInt()
            }
            lensList.add(Lens(key, focalLength, sign))
        }
        return lensList
    }

    fun setUpBoxes(lensList: List<Lens>, boxes: MutableMap<Int, List<Lens>>): Map<Int, List<Lens>> {
        for (lens in lensList) {
            val boxId = getBoxId(lens)
            if (boxes.containsKey(boxId)) {
                val currentLens = boxes[boxId]!!.find { it.key == lens.key }
                if (currentLens != null) {
                    if (lens.sign == Sign.EQUAL) {
                        currentLens.focalLength = lens.focalLength
                    } else {
                        boxes[boxId] = boxes[boxId]!!.toMutableList().filter { it.key != lens.key }
                    }
                } else {
                    if (lens.sign == Sign.EQUAL) {
                        val currentList = boxes[boxId]!!.toMutableList()
                        currentList.add(lens)
                        boxes[boxId] = currentList
                    }
                }
            } else {
                boxes[boxId] = listOf(lens)
            }
        }

        return boxes
    }

    fun calculateFocusingPower(boxes: Map<Int, List<Lens>>): Long {
        var focusingPower = 0L
        for (i in 0..255) {
            val boxLens = boxes[i]
            if (boxLens != null) {
                for ((lensIndex, lens) in boxLens.withIndex()) {
                    val boxNumber = i + 1L
                    val lensSlot = lensIndex + 1L
                    val lensPower = boxNumber * lensSlot * lens.focalLength
                    focusingPower += lensPower

                }
            }
        }
        return focusingPower
    }

    fun part2(input: List<String>): Long {
        val boxes = mutableMapOf<Int, List<Lens>>()
        val lensList = parseLens(input)
        val filledBoxes = setUpBoxes(lensList, boxes)
        val focusingPower = calculateFocusingPower(filledBoxes)
        println("Focusing power: $focusingPower")
        return focusingPower
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
//    check(part1(testInput) == 1320L)
    check(part2(testInput) == 145L)

    val input = readInput("Day15")
//    println(part1(input))
    println(part2(input))
}