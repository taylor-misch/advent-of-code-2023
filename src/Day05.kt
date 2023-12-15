fun main() {

    data class Set(
        val beginningNumber: Long,
        val endingNumber: Long,
    )

    data class Transformation(
        val destination: Long,
        val source: Long,
        val range: Long,
    )

    data class AlmanacEntry(
        val transforms: List<Transformation>
    ) {
        fun findSource(destinationNumber: Long): Long {
            for (transform in transforms) {
                val destinationOffset = destinationNumber - transform.destination
                if (destinationOffset >= 0 && destinationOffset < transform.range) {
                    return transform.source + destinationOffset
                }
            }
            return destinationNumber
        }
    }


    data class Mapping(
        val seed: Long,
        val soil: Long,
        val fertilizer: Long,
        val water: Long,
        val light: Long,
        val temperature: Long,
        val humidity: Long,
        val location: Long,
    )

    fun printSets(sets: List<Set>) {
        for (set in sets) {
            println("${set.beginningNumber} - ${set.endingNumber}")
        }
    }

    fun isTransformed(transformation: Transformation, value: Long): Boolean {
        return value in transformation.source..(transformation.source + transformation.range)
    }

    fun getListOfSeedValuesV1(input: String): List<Long> {
        return input.split(":")[1].trim().split(" ").map { it.toLong() }
    }

    fun getListOfSeedValuesV2(input: String): List<Set> {
        val seeds = mutableListOf<Set>()
        val inputList = input.split(":")[1].trim().split(" ").map { it.toLong() }

        for (i in 0 until inputList.size step 2) {
            seeds.add(Set(inputList[i], inputList[i] + inputList[i + 1]))
        }
        return seeds
    }

    fun getSectionTransformations(section: String, input: List<String>): List<Transformation> {
        val transformations = mutableListOf<Transformation>()
        var mapLine = false
        for (line in input) {
            if (line.isBlank()) {
                mapLine = false
            }
            if (mapLine) {
                val (destination, source, range) = line.split(" ").map { it.toLong() }
                transformations.add(Transformation(destination, source, range))
            }
            if (line.contains(section)) {
                mapLine = true
            }
        }
        return transformations
    }

    fun getSeedToSoilTransformations(input: List<String>): List<Transformation> {
        return getSectionTransformations("seed-to-soil", input)

    }

    fun getSoilToFertilizerTransformations(input: List<String>): List<Transformation> {
        return getSectionTransformations("soil-to-fertilizer", input)

    }

    fun getFertilizerToWaterTransformations(input: List<String>): List<Transformation> {
        return getSectionTransformations("fertilizer-to-water", input)

    }

    fun getWaterToLightTransformations(input: List<String>): List<Transformation> {
        return getSectionTransformations("water-to-light", input)

    }

    fun getLightToTemperatureTransformations(input: List<String>): List<Transformation> {
        return getSectionTransformations("light-to-temperature", input)

    }

    fun getTemperatureToHumidityTransformations(input: List<String>): List<Transformation> {
        return getSectionTransformations("temperature-to-humidity", input)

    }

    fun getHumidityToLocationTransformations(input: List<String>): List<Transformation> {
        return getSectionTransformations("humidity-to-location", input)
    }

    fun performTransformation(transforms: List<Transformation>, value: Long): Long {
        for (transform in transforms) {
            if (isTransformed(transform, value)) {
                return value + (transform.destination - transform.source)
            }
        }
        return value
    }

    fun findMinimum(input: List<String>, seedValues: List<Long>): Long {
        val mappings = mutableListOf<Mapping>()
        for (seed in seedValues) {
            val soil = performTransformation(getSeedToSoilTransformations(input), seed)
            val fertilizer = performTransformation(getSoilToFertilizerTransformations(input), soil)
            val water = performTransformation(getFertilizerToWaterTransformations(input), fertilizer)
            val light = performTransformation(getWaterToLightTransformations(input), water)
            val temperature = performTransformation(getLightToTemperatureTransformations(input), light)
            val humidity = performTransformation(getTemperatureToHumidityTransformations(input), temperature)
            val location = performTransformation(getHumidityToLocationTransformations(input), humidity)
            val mapping = Mapping(seed, soil, fertilizer, water, light, temperature, humidity, location)
            mappings.add(mapping)
        }
        var minLocation = Long.MAX_VALUE
        for (mapping in mappings) {
            minLocation = minOf(minLocation, mapping.location)
        }
        println("Minimum Location: $minLocation")
        return minLocation
    }

    fun part1(input: List<String>): Long {
        val seedValues = getListOfSeedValuesV1(input[0])
        return findMinimum(input, seedValues)
    }

    fun mapAlmanac(input: List<String>): List<AlmanacEntry> {
        val almanac = mutableListOf<AlmanacEntry>()
        almanac.add(AlmanacEntry(getHumidityToLocationTransformations(input)))
        almanac.add(AlmanacEntry(getTemperatureToHumidityTransformations(input)))
        almanac.add(AlmanacEntry(getLightToTemperatureTransformations(input)))
        almanac.add(AlmanacEntry(getWaterToLightTransformations(input)))
        almanac.add(AlmanacEntry(getFertilizerToWaterTransformations(input)))
        almanac.add(AlmanacEntry(getSoilToFertilizerTransformations(input)))
        almanac.add(AlmanacEntry(getSeedToSoilTransformations(input)))
        return almanac
    }

    fun findSeedForLocation(location: Long, almanac: List<AlmanacEntry>): Long {
        val humidity = almanac[0].findSource(location)
        val temperature = almanac[1].findSource(humidity)
        val light = almanac[2].findSource(temperature)
        val water = almanac[3].findSource(light)
        val fertilizer = almanac[4].findSource(water)
        val soil = almanac[5].findSource(fertilizer)
        val seed = almanac[6].findSource(soil)
        return seed
    }


    fun part2(input: List<String>): Long {
        // work backwards from location to seed - find the lowest seed that produces location
        val seedValues = getListOfSeedValuesV2(input[0])
        val almanac = mapAlmanac(input)
        println("Seed values: $seedValues")
        var possibleLocation = 0L
        var foundLocation = false
        while (!foundLocation) {
            val possibleSeed = findSeedForLocation(possibleLocation, almanac)
            for (seed in seedValues) {
                if (possibleSeed >= seed.beginningNumber &&
                    possibleSeed <= seed.endingNumber
                ) {
                    foundLocation = true
                    break
                }
            }
            if (!foundLocation) {
                possibleLocation++
            }
        }
        println("Location: $possibleLocation")
        return possibleLocation
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
//    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
//    println(part1(input))
    println(part2(input))
}