package cryptography

fun main() {
    val steganographer = Steganographer()
    while (true) {
        println("Task (hide, show, exit): ")
        when (val userChoice = readln()) {
            "exit" -> {
                println("Bye!")
                return
            }

            "hide" -> steganographer.hide()
            "show" -> steganographer.show()
            else -> println("Wrong task: $userChoice")
        }
    }
}