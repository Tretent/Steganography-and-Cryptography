package cryptography

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

fun main() {
    while (true) {
        println("Task (hide, show, exit): ")
        when (val userChoice = readln()) {
            "exit" -> {
                println("Bye!")
                return
            }

            "hide" -> hide()

            "show" -> println("Obtaining message from image.")

            else -> println("Wrong task: $userChoice")
        }
    }
}

fun hide() {
    fun isValidFile(file: File): Boolean = file.exists()

    fun setPixelLSBsToOne(file: File): BufferedImage {
        fun setLSBToOne(color: Int): Int = color or 1

        val image = ImageIO.read(file) ?: throw Exception("Image is null")
        for (x in 0 until image.width) {
            for (y in 0 until image.height) {
                val color = Color(image.getRGB(x, y))
                val newColor = Color(setLSBToOne(color.red), setLSBToOne(color.green), setLSBToOne(color.blue))
                image.setRGB(x, y, newColor.rgb)
            }
        }
        return image
    }

    fun writeImage(image: BufferedImage, imageFile: File) = ImageIO.write(image, "png", imageFile)


    println("Input image file: ")
    val inputFileName = readln().trim()
    println("Output image file: ")
    val outputFileName = readln().trim()

    val inputFile = File(inputFileName)
    if (!isValidFile(inputFile)) {
        println("Can't read input file!")
        return
    }

    println("Input Image: $inputFileName")
    println("Output Image: $outputFileName")

    try {
        val outputImage = setPixelLSBsToOne(inputFile)
        val outputFile = File(outputFileName)
        writeImage(outputImage, outputFile)
        println("Image $outputFileName is saved.")
    } catch (e: IOException) {
        println("Can't write output file!")
        return
    } catch (e: Exception) {
        println(
            when (e.message) {
                "Image is null" -> "Invalid image"
                else -> e.message
            }
        )
    }
}

