package search
import java.util.Scanner
import java.io.File

fun printResults(foundCandidates: MutableSet<Int>, people: List<String>) {
    if (foundCandidates.size == 0) {
        println("No matching people found.")
    } else {
        val sizeOfMutableSet = foundCandidates.size
        println("$sizeOfMutableSet persons found:")
        for (value in foundCandidates) {
            println(people[value])
        }
    }
}

fun search(dict: MutableMap<String,MutableList<Int>>, people: List<String>, query: String, strategy: String) {
    val splittedQuery = query.split(" ")
    var foundCandidates = mutableSetOf<Int>()
    val setToSearchBy: MutableSet<Int> = dict.values.flatten().toMutableSet()

    when (strategy) {
        "ANY" -> {
            for (word in splittedQuery) {
                if (dict.containsKey(word)) {
                    foundCandidates.addAll(dict[word]!!)
                }
            }
        }
        "ALL" -> {
            foundCandidates = dict.values.flatten().toMutableSet()
            for (word in splittedQuery) {
                if (dict.containsKey(word)) {
                    for (n in setToSearchBy) {
                        if (!dict[word]!!.contains(n)) {
                            foundCandidates.remove(n)
                        }
                    }
                }
            }
        }
        "NONE" -> {
            foundCandidates = dict.values.flatten().toMutableSet()
            for (word in splittedQuery) {
                    for (n in setToSearchBy) {
                        if (dict[word]!!.contains(n)) {
                            foundCandidates.remove(n)
                        }
                    }
                }
            }
        }

    printResults(foundCandidates, people)
}

fun printAll(people: List<String>) {
    for (person in people) {
        println(person)
    }
}

fun mapData(people: List<String>): MutableMap<String,MutableList<Int>> {
    var dict = mutableMapOf<String, MutableList<Int>>()
    var entry_no = 0
    for (entry in people) {
        val splittedEntry = entry.split(" ")
        for (word in splittedEntry) {
            if (dict.containsKey(word.toLowerCase())) {
                dict[word.toLowerCase()]!!.add(entry_no)
            } else {
                dict[word.toLowerCase()] = mutableListOf(entry_no)
            }
        }
        entry_no += 1
    }
    return dict
}

fun main(data: Array<String>) {
    val scan = Scanner(System.`in`)
    val people = File(data[1]).readLines()
    val dict = mapData(people)

    mainLoop@ while (scan.hasNext()) {
        println("\n=== Menu ===")
        println("1. Find a person")
        println("2. Print all people")
        println("0. Exit")
        val inp = scan.nextLine()
        when (inp.toInt()) {
            0 -> {
                println("Bye!")
                break@mainLoop
            }
            1 -> {
                println("Select a matching strategy: ALL, ANY, NONE")
                val strategy = scan.nextLine()!!.toUpperCase()
                println("Enter a name or email to search all suitable people.")
                val query = scan.nextLine()!!.toLowerCase()
                search(dict, people, query, strategy)
            }
            2 -> printAll(people)
            else -> {
                println("Incorrect option! Try again.")
            }
        }
    }
}