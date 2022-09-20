package cryptography

import java.awt.Color
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

const val MSB = 0x10000000
const val LSB = 0x00000001

class Steganographer {
    fun hide() {
        try {
            println("Input image file:")
            val inputFileName = readln().trim()
            val inputFile = File(inputFileName)
            val image = ImageIO.read(inputFile)

            println("Output image file:")
            val outputFileName = readln().trim()
            val outputFile = File(outputFileName)

            println("Message to hide:")
            val message = readln()
            val byteMessage = byteArrayOf(*message.encodeToByteArray(), 0, 0, 3)
            val messageBits = byteMessage.map { byte ->
                (0 until Byte.SIZE_BITS).map { (byte.rotateLeft(it).toInt() and MSB) / MSB }
            }.flatten()

            if (image.width * image.height < messageBits.size) {
                println("The input image is not large enough to hold this message.")
                return
            }

            println("Input Image: $inputFileName")
            println("Output Image: $outputFileName")

            messageBits.forEachIndexed { index, bit ->
                val x = index % image.width
                val y = index / image.width
                val color = Color(image.getRGB(x, y))
                val newColor = Color(color.red, color.green, color.blue and 1.inv() or bit)
                image.setRGB(x, y, newColor.rgb)
            }

            ImageIO.write(image, "png", outputFile)
            println("Message saved in $outputFileName image.")
        } catch (e: IOException) {
            println("Can't write output file!")
        } catch (e: Exception) {
            println(e.message)
        }
    }

    fun show() {
        println("Input image file:")
        val inputFileName = readln().trim()
        val inputFile = File(inputFileName)
        val image = ImageIO.read(inputFile)
        val byteMessage = mutableListOf<Byte>()

        for (byte in (0 until image.width * image.height)
            .map { pixelPosition ->
                Color(
                    image.getRGB(
                        pixelPosition % image.width,
                        pixelPosition / image.width
                    )
                ).blue and LSB
            }
            .chunked(Byte.SIZE_BITS)
            .map { byteList -> byteList.reduce { byte, bit -> (byte shl 1) or bit } }) {
            byteMessage.add(byte.toByte())
            if (byteMessage.size >= 3 && byteMessage.takeLast(3) == listOf<Byte>(0, 0, 3)) {
                byteMessage.dropLast(3)
                break
            }
        }

        println("Message: ${byteMessage.toByteArray().toString(Charsets.UTF_8)}")
    }
}