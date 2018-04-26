package com.pavel.hrolovic

import java.util.*

fun main(args: Array<String>) {
    val carpetSlicing = CarpetSlicer()

    val holes = LinkedList<Hole>()
    holes.add(Hole(90f, 100f))
    findLargestAndPrint(carpetSlicing, holes)

    holes.add(Hole(120f, 130f))
    findLargestAndPrint(carpetSlicing, holes)

    holes.add(Hole(150f, 143.5f))
    findLargestAndPrint(carpetSlicing, holes)
}

private fun findLargestAndPrint(carpetSlicing: CarpetSlicer, holes: LinkedList<Hole>) {
    val calculateLargestSlice = carpetSlicing.calculateLargestSlice(210f, 297.5f, holes)
    print("Largest carpet piece from (210, 297) is: ")
    print(calculateLargestSlice?.width)
    print("x")
    print(calculateLargestSlice?.height)
    println()
}

