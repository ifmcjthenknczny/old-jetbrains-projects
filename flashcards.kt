package flashcards

import java.io.File
import kotlin.random.Random


class SetOfCards {
    private val deck = mutableMapOf<String, String>()
    private var errors = mutableMapOf<String, Int>()
    private var log = ""


    private fun addCard(): Boolean {
        printAndSaveOutput("The card:")
        val term = readAndSaveInput()
        if (deck.containsKey(term)) {
            printAndSaveOutput("The card \"$term\" already exists.")
            return false
        }
        printAndSaveOutput("The definition of the card:")
        val definition = readAndSaveInput()
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

    private fun importCards(): Boolean {
        printAndSaveOutput("File name:")
        try {
            val cards = File(readAndSaveInput()).readLines()
            for (entry in cards) {
                val term = entry.split(":")[0]
                val definition = entry.split(":")[1]
                val errorNumber = entry.split(":")[2].toInt()
                deck[term] = definition
                if (errorNumber > 0) errors[term] = errorNumber
            }
            printAndSaveOutput("${cards.size} cards have been loaded")
        } catch (e: Exception) {
            printAndSaveOutput("File not found.")
            return false
        }
        return true
    }

    private fun exportCards(): Boolean {
        printAndSaveOutput("File name:")
        val myFile = File(readAndSaveInput())
        myFile.writeText("")
        for ((term, definition) in deck) {
            myFile.appendText("$term:$definition:")
            if (errors.containsKey(term)) myFile.appendText("${errors[term].toString()}\n")
            else myFile.appendText("0\n")
        }
        printAndSaveOutput("${deck.size} cards have been saved")
        return true
    }

    private fun askMe() {
        printAndSaveOutput("How many times to ask?")
        val howMany = readAndSaveInput().toInt()
        var counter = 0
        val terms = deck.keys.toList()
        while (counter < howMany) {
            val random = Random.nextInt(0, terms.size)
            val term = terms[random]
            printAndSaveOutput("Print the definition of \"$term\":")
            val userDefinition = readAndSaveInput()
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
            counter += 1
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
        errors[term] = errors.getOrDefault(term, 0) + 1
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
                "import" -> importCards()
                "export" -> exportCards()
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

fun main() {
    val flashDeck = SetOfCards()
    flashDeck.userMenu()
}