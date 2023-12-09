fun main() {
    fun reduceToZeros(nodes: List<Node>): List<Node> {
        return if (nodes.all { it.key == 0L }) {
            nodes
        } else {
            val newList = nodes
                .mapIndexed { index, leftNode ->
                    if (index < nodes.size - 1) {
                        val rightNode = nodes[index + 1]
                        Node(rightNode.key - leftNode.key, leftNode, rightNode)
                    } else {
                        null
                    }
                }
                .filterNotNull()
            reduceToZeros(newList)
        }
    }

    fun getZeroNodes(input: List<String>) = input
        .map { line ->
            line.split("\\s+".toRegex())
                .map { it.toLong() }
                .map { Node(it) }
        }
        .map { nodes ->
            reduceToZeros(nodes)
        }

    fun part1(input: List<String>): Long {
        return getZeroNodes(input)
            .sumOf { zeroNodes ->
                val lastNode = zeroNodes.last()
                Node(0, left = lastNode.right).predictNext()
            }
    }

    fun part2(input: List<String>): Long {
        return getZeroNodes(input)
            .sumOf { zeroNodes ->
                val firstNode = zeroNodes.first()
                Node(0, right = firstNode.left).predictPrevious()
            }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114L)
    check(part2(testInput) == 2L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}

class Node(val key: Long, var left: Node? = null, var right: Node? = null) {
    fun predictNext(): Long {
        return if (left == null) {
            key
        } else {
            right = Node(key + left!!.key, left!!.right)
            right!!.predictNext()
        }
    }

    fun predictPrevious(): Long {
        return if (right == null) {
            key
        } else {
            left = Node(right!!.key - key, right = right!!.left)
            left!!.predictPrevious()
        }
    }
}