package flashcards

import java.io.File
import kotlin.random.Random

class SetOfCards {
    private val deck = mutableMapOf<String, String>()

    fun addCard(): Boolean {
        println("The card:")
        val term = readLine()!!
        if (deck.containsKey(term)) {
            println("The card \"$term\" already exists.")
            return false
        }
        println("The definition of the card:")
        val definition = readLine()!!
        if (deck.containsValue(definition)) {
            println("The definition \"$definition\" already exists.")
            return false
        }
        deck[term] = definition
        println("The pair (\"$term\":\"$definition\") has been added.")
        return true
    }

    fun removeCard(): Boolean {
        println("Which card?")
        val term = readLine()!!
        if (deck.containsKey(term)) {
            deck.remove(term)
            println("The card has been removed.")
        } else {
            println("Can't remove \"$term\": there is no such card.")
            return false
        }
        return false
    }

    fun importCards(): Boolean {
        println("File name:")
        try {
            val cards = File(readLine()!!).readLines()
            for (entry in cards) {
                val term = entry.split(":")[0]
                val definition = entry.split(":")[1]
                deck[term] = definition
                //println("$term:$definition")
            }
            println("${cards.size} cards have been loaded")
        } catch (e: Exception) {
            println("File not found.")
            return false
        }
        return true
    }

    fun exportCards(): Boolean {
        println("File name:")
        val myFile = File(readLine()!!)
        myFile.writeText("")
        for ((term, definition) in deck) {
            myFile.appendText("$term:$definition\n")
            //println("$term:$definition")
        }
        println("${deck.size} cards have been saved")
        return true
    }

    fun askMe() {
        println("How many times to ask?")
        val howMany = readLine()!!.toInt()
        var counter = 0
        val terms = deck.keys.toList()
        while (counter < howMany) {
            val random = Random.nextInt(0, terms.size)
            val term = terms[random]
            println("Print the definition of \"$term\":")
            val userDefinition = readLine()!!
            when {
                userDefinition == deck[term] -> {
                    println("Correct!")
                }
                deck.containsValue(userDefinition) -> {
                    val correctTerm = getKeyFromValue(userDefinition, deck)
                    println("Wrong. The right answer is \"${deck[term]}\", but your definition is correct for \"$correctTerm\"")
                }
                else -> {
                    println("Wrong. The right answer is \"${deck[term]}\".")
                }
            }
            counter += 1
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
    var stop = false
    while (!stop) {
        println("Input the action (add, remove, import, export, ask, exit):")
        val action = readLine()!!
        when (action.toLowerCase()) {
            "add" -> flashDeck.addCard()
            "remove" -> flashDeck.removeCard()
            "import" -> flashDeck.importCards()
            "export" -> flashDeck.exportCards()
            "ask" -> flashDeck.askMe()
            "exit" -> {
                println("Bye bye!")
                stop = true
            }
        }
    }
}