package com.pavel.hrolovic

import java.util.*

class CarpetSlicer {

    fun calculateLargestSlice(width: Float, height: Float, holes: Queue<Hole>): Slice {
        if (holes.isEmpty()) throw IllegalStateException("Cannot slice carpet without holes")
        val collectedTo = mutableListOf<Slice>()
        val initialHoleList = LinkedList<Hole>(holes)
        slice(0f, 0f, width, height, initialHoleList, collectedTo)

        val sortedByDescending = collectedTo.sortedByDescending { slice -> slice.width * slice.height }
        return findFirstSliceWithoutHole(sortedByDescending, holes)
    }

    private fun findFirstSliceWithoutHole(collectedTo: List<Slice>, holes: Queue<Hole>): Slice {
        for (slice: Slice in collectedTo) {
            if (!sliceHasHole(holes, slice)) {
                return slice
            }
        }
        throw IllegalStateException("Carpet must have space without a hole")
    }

    fun sliceHasHole(holes: Queue<Hole>, slice: Slice): Boolean {
        for (hole: Hole in holes) {
            if (isHoleOnSlice(hole, slice)) return true
        }
        return false;
    }

    fun isHoleOnSlice(hole: Hole, slice: Slice): Boolean {
        if (hole.pointX > slice.pointX && hole.pointY > slice.pointY &&
                (slice.width + slice.pointX) > hole.pointX && (slice.height + slice.pointY) > hole.pointY) {
            return true;
        }
        return false
    }

    fun slice(pointX: Float, pointY: Float, width: Float, height: Float, holes: Queue<Hole>, collectedTo: MutableList<Slice>) {
        val firstHole = holes.poll()
        val slicedByHole = sliceByHole(pointX, pointY, width, height, firstHole)
        slicedByHole.forEach { slice ->
            val holesOnSlice = LinkedList<Hole>()
            holes.forEach { hole ->
                if (isHoleOnSlice(hole, slice)) {
                    holesOnSlice.add(hole)
                }
            }
            if (!holesOnSlice.isEmpty()) {
                slice(slice.pointX, slice.pointY, slice.width, slice.height, holesOnSlice, collectedTo)
            }
            collectedTo.add(slice)
        }
    }

    fun sliceByHole(pointX: Float, pointY: Float, width: Float, height: Float, hole: Hole): List<Slice> {
        return listOf(
                Slice(pointX, pointY, hole.pointX - pointX, height),
                Slice(hole.pointX, pointY, width - hole.pointX + pointX, height),
                Slice(pointX, hole.pointY, width, height + pointY - hole.pointY),
                Slice(pointX, pointY, width, hole.pointY - pointY)
        )
    }


}