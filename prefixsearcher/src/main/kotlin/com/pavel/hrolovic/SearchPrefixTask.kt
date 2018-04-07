package com.pavel.hrolovic

class SearchPrefixTask {
    private val roots: MutableMap<Char, TreeElement> = mutableMapOf()

    fun addEntry(prefix: String) {
        if (prefix.isEmpty()) {
            throw IllegalArgumentException("Prefix cannot be empty")
        }
        val lowerCasePrefix = prefix.toLowerCase()

        val firstChar = lowerCasePrefix[0]
        val root = roots.getOrPut(firstChar) { TreeElement(firstChar, 15) }
        traverseAndCreateTree(root, prefix, 1)
    }

    fun traverseAndCreateTree(rootElement: TreeElement, input: String, index: Int) {
        if (input.length > index) {
            val character = input.get(index);
            val nextElement = rootElement.addOrGetChild(character);
            traverseAndCreateTree(nextElement, input, index + 1);
        }
    }

    fun lookup(word: String): Boolean {
        val wordLowerCase = word.toLowerCase()
        val firstCharacter = wordLowerCase.get(0)
        val rootElementForWord = roots.get(firstCharacter) ?: return false
        val findNextCharacter = findNextCharacter(rootElementForWord, wordLowerCase, 1)
        return findNextCharacter.hasNoChildren()
    }

    private fun findNextCharacter(currentChild: TreeElement, wordLowerCase: String, currentIndex: Int): TreeElement {
        if (wordLowerCase.length <= currentIndex) {
            return currentChild
        }
        val childFor = currentChild.getChildFor(wordLowerCase.get(currentIndex)) ?: return  currentChild
        return findNextCharacter(childFor, wordLowerCase, currentIndex + 1)
    }

}