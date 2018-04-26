package com.pavel.hrolovic

import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CarpetSlicerTest {
    lateinit var slicer: CarpetSlicer

    @Before
    fun setUp() {
        slicer = CarpetSlicer()
    }

    @Test
    fun shouldReturn4SlicesFor1Hole() {
        val holes = LinkedList<Hole>()

        holes.add(Hole(20f, 30f))
        val collectedTo = mutableListOf<Slice>()
        slicer.slice(0f, 0f, 100f, 100f, holes, collectedTo);
        assertEquals(4, collectedTo.size)
    }

    @Test
    fun shouldReturnSlicesFor2Holes() {
        val holes = LinkedList<Hole>()

        holes.add(Hole(50f, 50f))
        holes.add(Hole(70f, 55f))

        val collectedTo = mutableListOf<Slice>()
        slicer.slice(0f, 0f, 100f, 100f, holes, collectedTo);

        for (slice in collectedTo) {
            println(slice)
        }
        assertEquals(12, collectedTo.size)
    }

    @Test
    fun shouldReturnSlicesFor4Holes() {
        val holes = LinkedList<Hole>()

        holes.add(Hole(64f, 62f))
        holes.add(Hole(2f, 1f))
        holes.add(Hole(10f, 15f))
        holes.add(Hole(20f, 25f))


        val collectedTo = mutableListOf<Slice>()
        slicer.slice(0f, 0f, 100f, 100f, holes, collectedTo);
        assertEquals(60, collectedTo.size)
    }

    @Test
    fun shouldReturnSlicesFor3Holes() {
        val holes = LinkedList<Hole>()

        holes.add(Hole(2f, 1f))
        holes.add(Hole(10f, 15f))
        holes.add(Hole(80f, 45f))

        val collectedTo = mutableListOf<Slice>()
        slicer.slice(0f, 0f, 100f, 50f, holes, collectedTo);
        assertEquals(28, collectedTo.size)
    }

    @Test
    fun shouldReturnFalseIfHoleOutsideOfSlice() {
        val slice = Slice(20f, 20f, 50f, 50f)
        val holes = LinkedList<Hole>()

        holes.add(Hole(0f, 1f))
        holes.add(Hole(10f, 15f))
        assertFalse { slicer.sliceHasHole(holes, slice) }

        holes.clear();

        holes.add(Hole(80f, 75f))
        holes.add(Hole(71f, 78f))
        assertFalse { slicer.sliceHasHole(holes, slice) }
    }

    @Test
    fun shouldReturnTrueIfHoleInsideOfSlice() {
        val slice = Slice(20f, 20f, 50f, 50f)
        val holes = LinkedList<Hole>()

        holes.add(Hole(21f, 21f))
        assertTrue { slicer.sliceHasHole(holes, slice) }

        holes.clear();

        holes.add(Hole(59f, 59f))
        assertTrue { slicer.sliceHasHole(holes, slice) }
    }

    @Test
    fun shouldCalculateLargerPieceForOneHoles() {
        val holes = LinkedList<Hole>()

        holes.add(Hole(2f, 1f))

        val calculateLargestSlice = slicer.calculateLargestSlice(100f, 100f, holes)
        assertEquals(0f, calculateLargestSlice.pointX)
        assertEquals(1f, calculateLargestSlice.pointY)
        assertEquals(100f, calculateLargestSlice.width)
        assertEquals(99f, calculateLargestSlice.height)
    }

    @Test
    fun shouldCalculateLargerPieceForFourHoles() {
        val holes = LinkedList<Hole>()

        holes.add(Hole(2f, 1f))
        holes.add(Hole(10f, 15f))
        holes.add(Hole(80f, 75f))
        holes.add(Hole(71f, 78f))

        val calculateLargestSlice = slicer.calculateLargestSlice(100f, 100f, holes)
        assertEquals(10f, calculateLargestSlice.pointX)
        assertEquals(0f, calculateLargestSlice.pointY)
        assertEquals(90f, calculateLargestSlice.width)
        assertEquals(75f, calculateLargestSlice.height)
    }

    @Test
    fun shouldReturnTrueIfHoleNotOnSlice() {
        val slice = Slice(2f, 0f, 10f, 100f)
        val hole = Hole(10f, 15f)
        assertTrue {
            slicer.isHoleOnSlice(hole, slice)
        }
    }

    @Test
    fun shouldReturnTrueIfHoleNotOnSliceOnOneDimension() {
        val slice = Slice(50f, 0f, 50f, 60f)
        val hole = Hole(50f, 50f)
        assertFalse {
            slicer.isHoleOnSlice(hole, slice)
        }
    }

    @Test
    fun shouldReturnFalseIfHoleNotOnSlice() {
        val slice = Slice(2f, 2f, 10f, 100f)
        val hole = Hole(1f, 2f)
        assertFalse {
            slicer.isHoleOnSlice(hole, slice)
        }

        val anotherHole = Hole(59f, 101f)
        assertFalse {
            slicer.isHoleOnSlice(anotherHole, slice)
        }
    }

    @Test
    fun shouldSliceByPoint() {
        val slideByPoint = slicer.sliceByHole(0f, 0f, 100f, 100f, Hole(10f, 10f))
        assertEquals(4, slideByPoint.size)

        val first = slideByPoint.get(0)
        assertEquals(0f, first.pointX)
        assertEquals(0f, first.pointY)
        assertEquals(10f, first.width)
        assertEquals(100f, first.height)

        val second = slideByPoint.get(1)
        assertEquals(10f, second.pointX)
        assertEquals(0f, second.pointY)
        assertEquals(90f, second.width)
        assertEquals(100f, second.height)

        val third = slideByPoint.get(2)
        assertEquals(0f, third.pointX)
        assertEquals(10f, third.pointY)
        assertEquals(100f, third.width)
        assertEquals(90f, third.height)

        val fourth = slideByPoint.get(3)
        assertEquals(0f, fourth.pointX)
        assertEquals(0f, fourth.pointY)
        assertEquals(100f, fourth.width)
        assertEquals(10f, fourth.height)
    }

    @Test
    fun shouldUseRelativeDimensions() {
        val slideByPoint = slicer.sliceByHole(2f, 1f, 100f, 100f, Hole(10f, 11f))
        assertEquals(4, slideByPoint.size)

        val first = slideByPoint.get(0)
        assertEquals(2f, first.pointX)
        assertEquals(1f, first.pointY)
        assertEquals(8f, first.width)
        assertEquals(100f, first.height)

        val second = slideByPoint.get(1)
        assertEquals(10f, second.pointX)
        assertEquals(1f, second.pointY)
        assertEquals(92f, second.width)
        assertEquals(100f, second.height)

        val third = slideByPoint.get(2)
        assertEquals(2f, third.pointX)
        assertEquals(11f, third.pointY)
        assertEquals(100f, third.width)
        assertEquals(90f, third.height)

        val fourth = slideByPoint.get(3)
        assertEquals(2f, fourth.pointX)
        assertEquals(1f, fourth.pointY)
        assertEquals(100f, fourth.width)
        assertEquals(10f, fourth.height)
    }
}