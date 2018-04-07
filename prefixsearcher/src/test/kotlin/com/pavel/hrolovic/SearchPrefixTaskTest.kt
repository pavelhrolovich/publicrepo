package com.pavel.hrolovic

import org.junit.Before

import org.junit.Assert.*
import org.junit.Test

class SearchPrefixTaskTest {
    private lateinit var prefixTree: SearchPrefixTask

    @Before
    fun setUp() {
        prefixTree = SearchPrefixTask()
    }

    @Test
    fun shouldReturnFalseForEmptyPrefixList() {
        assertFalse(prefixTree.lookup("anyWord"))
    }

    @Test
    fun shouldLookupPrefix() {
        prefixTree.addEntry("co")
        prefixTree.addEntry("eng")
        prefixTree.addEntry("ro")
        prefixTree.addEntry("trad")
        prefixTree.addEntry("t")

        assertTrue(prefixTree.lookup("engineering"));
        assertTrue(prefixTree.lookup("ENGINEERING"));
        assertTrue(prefixTree.lookup("english"));
        assertTrue(prefixTree.lookup("eng"));
    }

    @Test
    fun shouldNotLookupPrefix() {
        assertFalse(prefixTree.lookup("ro"));
        assertFalse(prefixTree.lookup("science"));
        assertFalse(prefixTree.lookup("troditional"));
        assertFalse(prefixTree.lookup("caperation"));
    }

    @Test(expected = IllegalArgumentException::class)
    fun shouldValidateStringLength() {
        prefixTree.addEntry("")
    }

    @Test
    fun shouldAssignSecondChar() {
        val rootElement = TreeElement('a', 5)
        prefixTree.traverseAndCreateTree(rootElement, "ab", 1);

        assertEquals('a', rootElement.value);
        assertEquals('b', rootElement.getChildFor('b')?.value)
        assertTrue(rootElement.getChildFor('b')!!.hasNoChildren())
    }

    @Test
    fun shouldNotReplaceCharacters() {
        val rootElement = TreeElement('a', 5)
        val bChild = rootElement.addOrGetChild('b')
        prefixTree.traverseAndCreateTree(rootElement, "abcd", 1);

        assertEquals('a', rootElement.value);
        assertSame(bChild, rootElement.getChildFor('b'))
        assertEquals('c', rootElement.getChildFor('b')!!.getChildFor('c')!!.value)
        assertEquals('d', rootElement.getChildFor('b')!!.getChildFor('c')!!.getChildFor('d')!!.value)


        prefixTree.traverseAndCreateTree(rootElement, "abfd", 1);
        assertEquals('a', rootElement.value);
        assertSame(bChild, rootElement.getChildFor('b'))
        assertEquals('c', rootElement.getChildFor('b')!!.getChildFor('c')!!.value)
        assertEquals('f', rootElement.getChildFor('b')!!.getChildFor('f')!!.value)
        assertEquals('d', rootElement.getChildFor('b')!!.getChildFor('f')!!.getChildFor('d')!!.value)

    }
}