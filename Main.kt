package cryptography

fun main() {
    while (true) {
        print("Task (hide, show, exit): ")
        when (val userChoice = readln()) {
            "exit" -> {
                println("Bye!")
                return
            }
            "hide" -> println("Hiding message in image.")

            "show" -> println("Obtaining message from image.")

            else -> println("Wrong task: $userChoice")
        }
    }
}

