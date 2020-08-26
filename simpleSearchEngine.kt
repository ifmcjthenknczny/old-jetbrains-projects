// https://hyperskill.org/projects/89/stages/498/implement

package search
import java.util.Scanner
import java.io.File

fun search(dict: MutableMap<String,MutableList<Int>>, people: List<String>, query: String): Boolean {

    if (dict.containsKey(query)) {
        val foundNumber = dict[query]!!.size
        println("$foundNumber persons found:")
        for (value in dict[query]!!) {
            println(people[value])
        }
        return true
    } else {
        println("No matching people found.")
        return false
    }
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

    /*println("Enter the number of people:")
    val howManyPeople = scan.nextLine().toInt()
    val people: Array<String?> = arrayOfNulls(howManyPeople)

    println("Enter all people:")
    for (i in people.indices) people[i] = scan.nextLine()!!

    println("\nEnter number of search queries:")
    val howManyQueries = scan.nextLine().toInt()

    repeat (howManyQueries) {

    }*/

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
                println("Enter a name or email to search all suitable people.")
                val query = scan.nextLine()!!.toLowerCase()
                search(dict, people, query)
            }
            2 -> printAll(people)
            else -> {
                println("Incorrect option! Try again.")
            }
        }
    }
}