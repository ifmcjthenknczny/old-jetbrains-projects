package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun inverseColour(inputPath: String, outputPath: String) {
    val photo = ImageIO.read(File(inputPath))
    val width = photo.getWidth()
    val height = photo.getHeight()
    for (i in 0 until width) {
        for (j in 0 until height) {
            val c = Color(photo.getRGB(i, j))
            val r = c.red
            val g = c.green
            val b = c.blue
            // val invertedColor = Color(255 - r, 255 - g, 255 - b)
            val newColor: Int = 65536 * (255 - r) + 256 * (255 - g) + (255 - b)
            photo.setRGB(i, j, newColor)
        }
    }
    ImageIO.write(photo, "png", File(outputPath))
}

fun createImageFile(width: Int, height: Int, outputFileName: String) {
    val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val g = bufferedImage.graphics
    g.color = Color.RED
    g.drawLine(0, 0, width - 1, height - 1)
    g.drawLine(0, height - 1, width - 1, 0)
    ImageIO.write(bufferedImage, "png", File(outputFileName))
}

fun rectangleMenu() {
    println("Enter rectangle width:")
    val width = readLine()!!.toInt()
    println("Enter rectangle height:")
    val height = readLine()!!.toInt()
    println("Enter output image name:")
    val filename = readLine()!!
    createImageFile(width, height, filename)
}

fun main(args: Array<String>) {
    val inputPath: String = args[1]
    val outputPath: String = args[3]
    inverseColour(inputPath, outputPath)
}