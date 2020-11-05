package flashcards

import java.io.File
import java.io.FileNotFoundException
import kotlin.random.Random


class SetOfCards {
    private val deck = mutableMapOf<String, String>()
    private var errors = mutableMapOf<String, Int>()
    private var log = ""


    private fun addCard(): Boolean {
        printAndSaveOutput("The card:")
        val term = readAndSaveInput().toLowerCase()
        if (deck.containsKey(term)) {
            printAndSaveOutput("The card \"$term\" already exists.")
            return false
        }
        printAndSaveOutput("The definition of the card:")
        val definition = readAndSaveInput().toLowerCase()
        if (deck.containsValue(definition)) {
            printAndSaveOutput("The definition \"$definition\" already exists.")
            return false
        }
        deck[term] = definition
        printAndSaveOutput("The pair (\"$term\":\"$definition\") has been added.")
        return true
    }

    private fun removeCard(): Boolean {
        printAndSaveOutput("Which card?")
        val term = readAndSaveInput()
        if (deck.containsKey(term)) {
            deck.remove(term)
            printAndSaveOutput("The card has been removed.")
            if (errors.containsKey(term)) errors.remove(term)
        } else {
            printAndSaveOutput("Can't remove \"$term\": there is no such card.")
            return false
        }
        return false
    }

    fun importCards(fileName: String): Boolean {
        try {
            val cards = File(fileName).readLines()
            for (entry in cards) {
                val term = entry.split(":")[0].toLowerCase()
                val definition = entry.split(":")[1].toLowerCase()
                val errorNumber = entry.split(":")[2].toInt()
                deck[term] = definition
                if (errorNumber > 0) errors[term] = errorNumber
            }
            printAndSaveOutput("${cards.size} cards have been loaded.")
        } catch (e: FileNotFoundException) {
            printAndSaveOutput("File not found.")
            return false
        }
        return true
    }

    fun exportCards(fileName: String): Boolean {
        val myFile = File(fileName)
        myFile.writeText("")
        for ((term, definition) in deck) {
            myFile.appendText("$term:$definition:")
            if (errors.containsKey(term)) myFile.appendText("${errors[term].toString()}\n")
            else myFile.appendText("0\n")
        }
        printAndSaveOutput("${deck.size} cards have been saved.")
        return true
    }

    private fun askMe() {
        printAndSaveOutput("How many times to ask?")
        val howMany = readAndSaveInput().toInt()
        val terms = deck.keys.toList()
        repeat(howMany) {
            val random = Random.nextInt(0, terms.size)
            val term = terms[random]
            printAndSaveOutput("Print the definition of \"$term\":")
            val userDefinition = readAndSaveInput().toLowerCase()
            when {
                userDefinition == deck[term] -> {
                    printAndSaveOutput("Correct!")
                }
                deck.containsValue(userDefinition) -> {
                    addError(term)
                    val correctTerm = getKeyFromValue(userDefinition, deck)
                    printAndSaveOutput("Wrong. The right answer is \"${deck[term]}\", but your definition is correct for \"$correctTerm\"")
                }
                else -> {
                    addError(term)
                    printAndSaveOutput("Wrong. The right answer is \"${deck[term]}\".")
                }
            }
        }
    }

    private fun readAndSaveInput(): String {
        val input = readLine()!!
        log += input
        log += "\n"
        return input
    }

    private fun printAndSaveOutput(text: String) {
        log += text
        log += "\n"
        println(text)
    }

    private fun addError(term: String) {
        errors[term.toLowerCase()] = errors.getOrDefault(term.toLowerCase(), 0) + 1
    }

    private fun saveLog(): Boolean {
        printAndSaveOutput("File name:")
        val myFile = File(readAndSaveInput())
        myFile.writeText(log)
        printAndSaveOutput("The log has been saved.")
        return true
    }

    private fun resetStats() {
        errors = mutableMapOf<String, Int>()
        printAndSaveOutput("Card statistics have been reset.")
    }

    private fun showHardest(): Boolean {
        if (errors.isNotEmpty()) {
            var maxErrors = 0
            val termsWithMaxErrors = mutableListOf<String>()
            for ((term, errorNumber) in errors) {
                if (errorNumber > maxErrors) {
                    termsWithMaxErrors.clear()
                    termsWithMaxErrors.add("\"$term\"")
                    maxErrors = errorNumber
                } else if (errorNumber == maxErrors) {
                    termsWithMaxErrors.add("\"$term\"")
                }
            }
            val middleString = if (termsWithMaxErrors.size == 1) " is" else "s are"
            printAndSaveOutput("The hardest card$middleString ${termsWithMaxErrors.joinToString(" ")}, $maxErrors")
        } else {
            printAndSaveOutput("There are no cards with errors.")
            return false
        }
        return true
    }

    fun userMenu() {
        while (true) {
            printAndSaveOutput("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):")
            val action = readAndSaveInput()
            when (action.toLowerCase()) {
                "add" -> addCard()
                "remove" -> removeCard()
                "import" -> {
                    printAndSaveOutput("File name:")
                    importCards(readAndSaveInput())
                }
                "export" -> {
                    printAndSaveOutput("File name:")
                    exportCards(readAndSaveInput())
                }
                "ask" -> askMe()
                "exit" -> {
                    printAndSaveOutput("Bye bye!")
                    break
                }
                "log" -> saveLog()
                "hardest card" -> showHardest()
                "reset stats" -> resetStats()
            }
        }
    }
}

fun getKeyFromValue(value: String, dict: MutableMap<String, String>): String {
    for (term in dict.keys) {
        if (dict[term] == value) {
            return term
        }
    }
    return ""
}

fun getProperties(mainArgs: Array<String>): List<String?> {
    var inputPath: String? = null
    var outputPath: String? = null
    var argIndex = 0
    while (argIndex < mainArgs.size) {
        when (mainArgs[argIndex]) {
            "-import" -> inputPath = mainArgs[argIndex + 1]
            "-export" -> outputPath = mainArgs[argIndex + 1]
        }
        argIndex++
    }
    return listOf(inputPath, outputPath)
}

fun main(args: Array<String>) {
    val flashDeck = SetOfCards()
    val paths = getProperties(args)
    val inputPath = paths.first()
    val outputPath = paths.last()

    if (inputPath != null) flashDeck.importCards(inputPath)
    flashDeck.userMenu()
    if (outputPath != null) flashDeck.exportCards(outputPath)
}
