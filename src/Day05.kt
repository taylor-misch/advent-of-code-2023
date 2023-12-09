fun main() {

    data class Transformation(
        val destination: Long,
        val source: Long,
        val range: Long,
    )

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

    fun isTransformed(transformation: Transformation, value: Long): Boolean {
        return value in transformation.source..(transformation.source + transformation.range)
    }

    fun getListOfSeedValuesV1(input: String): List<Long> {
        return input.split(":")[1].trim().split(" ").map { it.toLong() }
    }

    fun getListOfSeedValuesV2(input: String): List<Long> {
        val seeds = mutableListOf<Long>()
        val inputList = input.split(":")[1].trim().split(" ").map { it.toLong() }
        var index = 0
        while (index < inputList.size) {
            val value = inputList[index]
            val range = inputList[index + 1]
            for (i in 0 until range) {
                seeds.add(value + i)
            }
            index += 2
        }
        return seeds
    }

    fun getListOfSeedValuesV3(input: List<Long>): List<Long> {
        val seeds = mutableListOf<Long>()
        var index = 0
        while (index < input.size) {
            val value = input[index]
            val range = input[index + 1]
            for (i in 0 until range) {
                seeds.add(value + i)
            }
            index += 2
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

//    fun part2(input: List<String>): Long {
//        val seedValues = getListOfSeedValuesV2(input[0])
//        println(seedValues)
//        return findMinimum(input, seedValues)
//    }

    fun part2(input: List<String>): Long {
        val seedValues = getListOfSeedValuesV3(mutableListOf(1347397244L, 12212989L))
//        println(seedValues)
        return findMinimum(input, seedValues)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
//    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}