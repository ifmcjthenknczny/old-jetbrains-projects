package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import kotlin.math.sqrt

data class Node(val x: Int, val y: Int) : Comparable<Node> {
    var distance: Double = Double.MAX_VALUE
    var visited: Boolean = false
    var previousX: Int = 0
    var previousY: Int = 0
    var energy: Double = 0.0
    var edges = listOf<Node>()
    var newx: Int = x
    var newy: Int = y

    override fun compareTo(other: Node): Int {
        return when {
            distance > other.distance -> 1
            distance < other.distance -> -1
            else -> 0
        }
    }

    fun findEdges(width: Int, height: Int) {
        // indexes of nodes for photo with empty rows
        edges = when {
            newx == 0 && newy == 0 -> listOf(Node(newx + 1, newy), Node(newx, newy + 1), Node(newx + 1, newy + 1))
            newx == width - 1 && newy == 0 -> listOf(Node(newx - 1, newy + 1), Node(newx, newy + 1))
            newy == 0 -> listOf(Node(newx + 1, newy), Node(newx - 1, newy + 1), Node(newx, newy + 1), Node(newx + 1, newy + 1))
            newy == height - 1 && newx == width - 1 -> listOf<Node>()
            newy == height - 1 -> listOf(Node(newx + 1, newy))
            newx == 0 -> listOf(Node(newx, newy + 1), Node(newx + 1, newy + 1))
            newx == width - 1 -> listOf(Node(newx - 1, newy + 1), Node(newx, newy + 1))
            else -> listOf(Node(newx - 1, newy + 1), Node(newx, newy + 1), Node(newx + 1, newy + 1))
        }
    }
}

class Photo(var image: BufferedImage) {
    var width = image.width
    var height = image.height
    var transposed = false

    var nodes = MutableList(height) { MutableList<Node>(width) { Node(0, 0) } }

    init {
        for (y in 0 until height) {
            for (x in 0 until width) {
                nodes[y][x] = Node(x, y)
            }
        }
    }

    fun updateEnergy() {
        for (y in 0 until height) {
            for (x in 0 until width) {
                val xPixels = toPixelCoords(x, width)
                val yPixels = toPixelCoords(y, height)
                val energyX = pixelDimensionEnergyX(xPixels, y, image)
                val energyY = pixelDimensionEnergyY(x, yPixels, image)
                nodes[y][x].energy = sqrt(energyX + energyY)
            }
        }
    }

    fun addEmptyRows() {
        var emptyRowTop = MutableList(width) { Node(it, -1) }
        if (transposed) emptyRowTop = MutableList(width) { Node(-1, it) }
        nodes.add(0, emptyRowTop)
        var emptyRowBottom = MutableList(width) { Node(it, height) }
        if (transposed) emptyRowBottom = MutableList(width) { Node(height, it) }
        nodes.add(emptyRowBottom)
        height += 2
        for (y in 0 until height) {
            for (x in 0 until width) {
                nodes[y][x].newx = x
                nodes[y][x].newy = y
            }
        }
    }

    fun transpose() {
        val output = MutableList(width) { MutableList(height) { Node(0, 0) } }
        for (y in 0 until height) {
            for (x in 0 until width) {
                output[x][y] = nodes[y][x]
                output[x][y].newx = nodes[y][x].y
                output[x][y].newy = nodes[y][x].x
            }
        }
        val heightTemp = height
        val widthTemp = width
        width = heightTemp
        height = widthTemp
        nodes = output
        transposed = !transposed
    }

    fun findDistances() {
        // Dijkstra algorithm
        val unprocessedNodes = PriorityQueue<Node>()

        nodes[0][0].distance = 0.0
        unprocessedNodes.add(Node(0, 0))

        while (!unprocessedNodes.isEmpty()) {
            val pushedNode = unprocessedNodes.poll()
            val currentNode = nodes[pushedNode.newy][pushedNode.newx]
            currentNode.findEdges(width, height)
            if (!currentNode.visited) {
                for (neighbour in currentNode.edges) {
                    val currentNeighbour = nodes[neighbour.newy][neighbour.newx] // real list indexes
                    val tempDistance = currentNode.distance + currentNeighbour.energy
                    if (tempDistance < currentNeighbour.distance) {
                        nodes[neighbour.newy][neighbour.newx].distance = tempDistance
                        unprocessedNodes.add(nodes[neighbour.newy][neighbour.newx])
                        nodes[neighbour.newy][neighbour.newx].previousX = pushedNode.newx
                        nodes[neighbour.newy][neighbour.newx].previousY = pushedNode.newy
                    }
                }
                nodes[pushedNode.newy][pushedNode.newx].visited = true
            }
        }
    }

    fun findSeam(): MutableList<Node> {
        val distances = mutableListOf<Double>()
        for (node in nodes.last()) distances.add(node.distance)
        val lastRowMin = distances.minOrNull()
        val minDistanceX = distances.indexOf(lastRowMin)
        var row = height - 1
        var currNode = nodes[row][minDistanceX]
        val seam = mutableListOf<Node>()

        while (row > 1) {
            val prevNode = nodes[currNode.previousY][currNode.previousX]
            if (prevNode.newy < height - 1 && prevNode.newy > 0) {
                seam.add(prevNode)
                row--
            }
            currNode = prevNode
        }
        return seam
    }

    fun seamToPhoto(seam: MutableList<Node>) {
        width = image.width
        height = image.height
        val red = 65536 * 255
        for (pixel in seam) {
            image.setRGB(pixel.x, pixel.y, red)
        }
    }

    fun removeHorizontalSeam(seam: MutableList<Node>) {
        val newImage = BufferedImage(image.width, image.height - 1, BufferedImage.TYPE_INT_RGB)
        var newx = 0
        for (oldx in 0 until image.width) {
            var newy = 0
            for (oldy in 0 until image.height) {
                if (Node(oldx, oldy) in seam) continue
                val color: Int = image.getRGB(oldx, oldy)
                newImage.setRGB(newx, newy, color)
                newy++
            }
            newx++
        }
        image = newImage
    }

    fun removeVerticalSeam(seam: MutableList<Node>) {
        val newImage = BufferedImage(image.width - 1, image.height, BufferedImage.TYPE_INT_RGB)
        var newy = 0
        for (oldy in 0 until image.height) {
            var newx = 0
            for (oldx in 0 until image.width) {
                if (Node(oldx, oldy) in seam) continue
                val color: Int = image.getRGB(oldx, oldy)
                newImage.setRGB(newx, newy, color)
                newx++
            }
            newy++
        }
        image = newImage
    }

    fun returnImage(): BufferedImage {
        return image
    }
}

fun toPixelCoords(coordinate: Int, maximum: Int): List<Int> {
    return when (coordinate) {
        0 -> listOf(0, 2)
        maximum - 1 -> listOf(maximum - 3, maximum - 1)
        else -> listOf(coordinate - 1, coordinate + 1)
    }
}

fun pixelDimensionEnergyX(varX: List<Int>, constY: Int, photo: BufferedImage): Double {
    val r = Color(photo.getRGB(varX[0], constY)).red - Color(photo.getRGB(varX[1], constY)).red
    val g = Color(photo.getRGB(varX[0], constY)).green - Color(photo.getRGB(varX[1], constY)).green
    val b = Color(photo.getRGB(varX[0], constY)).blue - Color(photo.getRGB(varX[1], constY)).blue

    return (r * r + g * g + b * b).toDouble()
}

fun pixelDimensionEnergyY(constX: Int, varY: List<Int>, photo: BufferedImage): Double {
    val r = Color(photo.getRGB(constX, varY[0])).red - Color(photo.getRGB(constX, varY[1])).red
    val g = Color(photo.getRGB(constX, varY[0])).green - Color(photo.getRGB(constX, varY[1])).green
    val b = Color(photo.getRGB(constX, varY[0])).blue - Color(photo.getRGB(constX, varY[1])).blue

    return (r * r + g * g + b * b).toDouble()
}

fun seamCarving(image: BufferedImage, vertical: Boolean): BufferedImage {
    val photoNodes = Photo(image)
    photoNodes.updateEnergy()
    if (!vertical) photoNodes.transpose()
    photoNodes.addEmptyRows()
    photoNodes.findDistances()
    val seam = photoNodes.findSeam()
    if (vertical) photoNodes.removeVerticalSeam(seam)
    else photoNodes.removeHorizontalSeam(seam)
    return photoNodes.returnImage()
}

fun main(args: Array<String>) {
    //val path = System.getProperty("user.dir")
    //println("Working Directory = $path")
    val inputPath: String = args[1]
    val outputPath: String = args[3]
    val verticalSeams = args[5].toInt()
    val horizontalSeams = args[7].toInt()
    var image = ImageIO.read(File(inputPath))

    for (i in 0 until verticalSeams) image = seamCarving(image, true)
    for (j in 0 until horizontalSeams) image = seamCarving(image, false)

    ImageIO.write(image, "png", File(outputPath))
}