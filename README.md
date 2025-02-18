# JetBrains projects
> These are my JetBrains Academy projects from Kotlin Developer track, which I started in June 2020. All started and finished projects are here.

## Table of contents
* [General info](#general-info)
* [Screenshots](#screenshots)
* [Technologies used](#technologies-used)
* [Setup](#setup)
* [Features](#features)
* [Status](#status)
* [Inspiration](#inspiration)
* [Contact](#contact)

## General info
> [Simple Search Engine](https://hyperskill.org/projects/89?track=3) (simpleSearchEngine.kt) (difficulty level: Hard)

I've got to know a lot of basic features of Kotlin - developing search engine indexing over imported text file, with three options of searching.

> [Seam Carving](https://hyperskill.org/projects/100?track=3) (seamCarving.kt) (difficulty level: Challenging)

This project is focused on manipulating images with the use of Kotlin. It is based on Node and Photo classes, finds the lowest energy seam (line from the edge to edge on the other side of a picture) using implemented Dijkstra algorithm and deletes a given amount of horizontal and vertical seams, trimming the image. Finishing it needed a lot of research about Kotlin by myself.

> [Flashcards](https://hyperskill.org/projects/83?track=3) (flashcards.kt) (difficulty level: Challenging)

The point of this piece of code is to improve learning abilities by implement the idea behind flashcards (cards that with word on one side and definition on the other side) straight to the computer.

## Screenshots
> [Seam Carving](https://hyperskill.org/projects/100?track=3) (seamCarving.kt)

![Before](./img/trees.png)
![After](./img/trees-reduced.png)

## Technologies used
* Kotlin 1.3.72-release-468 
* JVM: 14.0.1 (Oracle Corporation 14.0.1+7)
* Gradle 6.6.1

## Setup
> [Simple Search Engine](https://hyperskill.org/projects/89?track=3) (simpleSearchEngine.kt)

Main function of this piece of code takes as an argument String: source path to normal text file.

> [Seam Carving](https://hyperskill.org/projects/100?track=3) (seamCarving.kt)

Code can be run from command line with necessary arguments: -in <relative input file path>, -out <relative output file path>, -width <vertical seams to delete> and -height <horizontal seams to delete>.

> [Flashcards](https://hyperskill.org/projects/83?track=3) (flashcards.kt)

Code can be run from command line with optional arguments -import <filename with cards to import> and -export <filename to export cards>. All input is typed by used in console.

## Features
> [Simple Search Engine](https://hyperskill.org/projects/89?track=3) (simpleSearchEngine.kt)

* Read data from text file.
* Mapping single words to lines indexes where they are in text file.
* Simple user menu.
* Search options ALL (result must have all phrases from query), ANY (any word from query), NONE (cannot have words from query).
* Printing results of search.
* Printing all available data.

> [Seam Carving](https://hyperskill.org/projects/100?track=3) (seamCarving.kt)

* Importing and exporting picture.
* Finding energy of pixels as well as horizontal and vertical seams with lowest energy.
* Finding edges of nodes (pixels) to create seam.
* Finding seam of lowest sum of pixels energy to opposite side of image, using Priority Queue Dijkstra algotrithm implementation with empty top and bottom rows and image transposition.
* Trimming the picture by given amount of vertical and horizontal lines.

> [Flashcards](https://hyperskill.org/projects/83?track=3) (flashcards.kt)

* Simple user menu.
* Adding and removing cards from set (repetition and error-sensitive).
* Importing and exporting cards to external file.
* Asking user about definition of random card in set, and showing good definition or answer to given definition if needed.
* Tracking log of all input and output in program with possibility of exporting it.
* Tracking user mistakes, showing hardest card when prompted along with possibility of resetting stats function.
* Optional command line arguments -import (filename to import cards at the beginning of program) and -export (filename to export card to after program ends).

## Status
> [Simple Search Engine](https://hyperskill.org/projects/89?track=3) (simpleSearchEngine.kt)
* Project is: _finished_

> [Seam Carving](https://hyperskill.org/projects/100?track=3) (seamCarving.kt)
* Project is: _finished_

> [Flashcards](https://hyperskill.org/projects/83?track=3) (flashcards.kt)
* Project is: _finished_

## Inspiration
Willingness to get to know another programming language and to find more challenging job, more amount of free time and JetBrains discount until the end of year (should I also mention that last two are result of COVID?).

## Contact
My name on github is (ifmcjthenknczny)[https://github.com/ifmcjthenknczny]. Feel free to contact me.