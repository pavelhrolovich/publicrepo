package com.pavel.hrolovic

data class TreeElement(val value: Char, private val expectedChild: Int) {
    private val children: MutableMap<Char, TreeElement>

    init {
        children = java.util.HashMap(expectedChild, 1f)
    }

    fun addOrGetChild(character: Char): TreeElement {
        var treeElement = children.get(character)
        if (treeElement == null) {
            treeElement = TreeElement(character, expectedChild)
            children.put(character, treeElement)
        }
        return treeElement
    }

    fun getChildFor(character: Char): TreeElement? {
        return children.get(character)
    }

    fun hasNoChildren(): Boolean {
        return children.isEmpty();
    }

}