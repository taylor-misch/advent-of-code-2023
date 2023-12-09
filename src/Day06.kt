fun main() {

    data class Race(
        val raceTime: Long,
        val raceRecord: Long
    )


    fun getRaceInfo(input: List<String>): List<Race> {
        val times = mutableListOf<Long>()
        val records = mutableListOf<Long>()
        val races = mutableListOf<Race>()
        input[0].split(":")[1].trim().split("\\s+".toRegex()).forEach { times.add(it.trim().toLong()) }
        input[1].split(":")[1].trim().split("\\s+".toRegex()).forEach { records.add(it.trim().toLong()) }
        for (i in 0 until times.size) {
            races.add(Race(times[i], records[i]))
        }
        return races
    }

    fun getRaceInfoV2(input: List<String>): Race {
        val time = input[0].split(":")[1].trim().replace("\\s+".toRegex(), "").toLong()
        val record = input[1].split(":")[1].trim().replace("\\s+".toRegex(), "").toLong()
        return Race(time, record)
    }

    fun findNumberOfWaysToBeatRecord(race: Race): Long {
        var numberOfWays = 0L
        for (i in 0 until race.raceTime) {
            if ((race.raceTime - i) * i > race.raceRecord) {
                numberOfWays++
            }
        }
        return numberOfWays
    }

    fun part1(input: List<String>): Long {
        val races = getRaceInfo(input)
        var totalNumberOfWays = 1L
        for (race in races) {
            val numberOfWays = findNumberOfWaysToBeatRecord(race)
            totalNumberOfWays *= numberOfWays
        }
        println("Total number of ways to beat the record: $totalNumberOfWays")
        return totalNumberOfWays
    }

    fun part2(input: List<String>): Long {
        val race = getRaceInfoV2(input)
        val numberOfWays = findNumberOfWaysToBeatRecord(race)
        println("Number of ways to beat the record: $numberOfWays")
        return numberOfWays
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288L)
    check(part2(testInput) == 71503L)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
