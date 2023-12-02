fun main() {

    data class Hint(
        val color: String,
        val colorCount: Int
    )

    data class HintSet(
        val setId: Int,
        val hints: List<Hint>
    )

    data class GameInfo(
        val gameId: Int,
        val hints: List<HintSet>
    )

    fun parseGameInput(input: List<String>): List<GameInfo> {
        val gameInfo = mutableListOf<GameInfo>()
        for (line in input) {
            val gameId = line.substringBefore(":").substringAfter("Game ").toInt()

            val hintSets = mutableListOf<HintSet>()
            val hintSetStrings = line.substringAfter(":").split(";")

            var i = 0
            for (hintSetString in hintSetStrings) {

                val hintStrings = hintSetString.substringAfter(" ").split(",")
                val hints = mutableListOf<Hint>()
                for (hintString in hintStrings) {
                    val colorCount = hintString.trim().substringBefore(" ").toInt()
                    val color = hintString.trim().substringAfter(" ")
                    hints.add(Hint(color, colorCount))
                }
                hintSets.add(HintSet(i, hints))
                i++
            }
            gameInfo.add(GameInfo(gameId, hintSets))
        }
        return gameInfo
    }

    fun part1(input: List<String>): Int {
        val gameInfo = parseGameInput(input)

        var gameIdRunningSum = 0;
        for (game in gameInfo) {
            val mapOfHighestValues = mutableMapOf<String, Int>()
            mapOfHighestValues["red"] = 0
            mapOfHighestValues["green"] = 0
            mapOfHighestValues["blue"] = 0
            for (hintSet in game.hints) {
                for (hint in hintSet.hints) {
                    if (mapOfHighestValues.containsKey(hint.color)) {
                        if (mapOfHighestValues[hint.color]!! < hint.colorCount) {
                            mapOfHighestValues[hint.color] = hint.colorCount
                        }
                    } else {
                        mapOfHighestValues[hint.color] = hint.colorCount
                    }
                }
            }
            if (mapOfHighestValues["red"]!! <= 12 &&
                mapOfHighestValues["green"]!! <= 13 &&
                mapOfHighestValues["blue"]!! <= 14
            ) {
//                println("Game ${game.gameId} is valid")
                gameIdRunningSum += game.gameId
            }

        }
        println("GameIdRunningSum: $gameIdRunningSum")
        return gameIdRunningSum
    }

    fun part2(input: List<String>): Int {
        val gameInfo = parseGameInput(input)

        var gamePowerRunningSum = 0;
        for (game in gameInfo) {
            val mapOfHighestValues = mutableMapOf<String, Int>()
            mapOfHighestValues["red"] = 0
            mapOfHighestValues["green"] = 0
            mapOfHighestValues["blue"] = 0
            for (hintSet in game.hints) {
                for (hint in hintSet.hints) {
                    if (mapOfHighestValues.containsKey(hint.color)) {
                        if (mapOfHighestValues[hint.color]!! < hint.colorCount) {
                            mapOfHighestValues[hint.color] = hint.colorCount
                        }
                    } else {
                        mapOfHighestValues[hint.color] = hint.colorCount
                    }
                }
            }
            gamePowerRunningSum += (mapOfHighestValues["red"]!! * mapOfHighestValues["green"]!! * mapOfHighestValues["blue"]!!)
            println("Game ${game.gameId} has power ${mapOfHighestValues["red"]!! * mapOfHighestValues["green"]!! * mapOfHighestValues["blue"]!!}")

        }
        println("GamePowerRunningSum: $gamePowerRunningSum")
        return gamePowerRunningSum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)


    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}