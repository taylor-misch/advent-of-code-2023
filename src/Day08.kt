fun main() {

    data class Node(
        val location: String,
        val left: String,
        val right: String,
        val isEnd: Boolean = false
    )

    fun parseDirections(input: String): List<String> {
        val directions = mutableListOf<String>()
        for (direction in input) {
            directions.add(direction.toString())
        }
        return directions
    }

    fun parseNode(input: String): Node {
        val location = input.substring(0, 3)
        val left = input.substring(7, 10)
        val right = input.substring(12, 15)
        return Node(location, left, right, input[2].toString() == "Z")
    }

    fun parseNodes(input: List<String>): Map<String, Node> {
        val nodes = mutableMapOf<String, Node>()
        for ((lineIndex, line) in input.withIndex()) {
            if (lineIndex > 1) {
                val node = parseNode(line)
                nodes[node.location] = node
            }
        }
        return nodes
    }

    fun walkPath(firstPosition: String, lastPosition: String, directions: List<String>, nodes: Map<String, Node>): Int {
        var steps = 0;
        var stepIndex = 0;
        var currentNode = nodes.getValue(firstPosition)
        while (currentNode.location != lastPosition) {
            val direction = directions[stepIndex]
            if (direction == "L") {
                currentNode = nodes[currentNode.left]!!
            } else if (direction == "R") {
                currentNode = nodes[currentNode.right]!!
            }
            steps++
            stepIndex++
            if (stepIndex >= directions.size) {
                stepIndex = 0
            }
        }
        println("Steps: $steps")
        return steps

    }


    fun walkPathV2(firstPosition: String, directions: List<String>, nodes: Map<String, Node>): Int {
        var steps = 0;
        var stepIndex = 0;
        var currentNode = nodes.getValue(firstPosition)
        while (!currentNode.isEnd) {
            val direction = directions[stepIndex]
            if (direction == "L") {
                currentNode = nodes[currentNode.left]!!
            } else if (direction == "R") {
                currentNode = nodes[currentNode.right]!!
            }
            steps++
            stepIndex++
            if (stepIndex >= directions.size) {
                stepIndex = 0
            }
        }
        println("Steps: $steps")
        return steps

    }

    fun parseStartingPositions(nodes: Map<String, Node>): List<Node> {
        val startingPositions = mutableListOf<Node>()
        for (node in nodes.values) {
            if (node.location[2].toString() == "A") {
                startingPositions.add(node)
            }
        }
        return startingPositions
    }

    fun areWeThereYet(locationNodes: List<Node>): Boolean {
        for (node in locationNodes) {
            if (!node.isEnd) {
                return false
            }
        }
        return true
    }

    fun walkPaths(directions: List<String>, nodes: Map<String, Node>): Int {
        var steps = 0;
        var stepIndex = 0;
        val currentNodes = parseStartingPositions(nodes).toMutableList()
        println("Start $currentNodes")
        while (!areWeThereYet(currentNodes)) {
            println(currentNodes.map { it.location })
            val direction = directions[stepIndex]
            if (direction == "L") {
                for ((index, node) in currentNodes.withIndex()) {
                    currentNodes[index] = nodes[node.left]!!
                }
            } else if (direction == "R") {
                for ((index, node) in currentNodes.withIndex()) {
                    currentNodes[index] = nodes[node.right]!!
                }
            }
            steps++
            stepIndex++
            if (stepIndex >= directions.size) {
                stepIndex = 0
            }
        }
        println("Steps: $steps")
        return steps
    }

    fun part1(input: List<String>): Int {
        val directions = parseDirections(input[0])
        println("Directions: $directions")
        val nodes = parseNodes(input)
        nodes.forEach(::println)
        val firstPosition = "AAA"
        val lastPosition = "ZZZ"
        return walkPath(firstPosition, lastPosition, directions, nodes)
    }

    fun part2(input: List<String>): Int {
        val directions = parseDirections(input[0])
        println("Directions: $directions")
        val nodes = parseNodes(input)
        nodes.forEach(::println)
//        val startingPositions = parseStartingPositions(nodes)
//        val stepList = mutableListOf<Int>()
//        for (node in startingPositions) {
//            val steps = walkPathV2(node.location, directions, nodes)
//            stepList.add(steps)
//        }
//        return findGCD(stepList)
        return walkPaths(directions, nodes)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
//    check(part1(testInput) == 2)
    check(part2(testInput) == 6)

    val input = readInput("Day08")
//    println(part1(input))
    println(part2(input))
}