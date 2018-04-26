PROBLEM:

Given a list of key strings and an input string, determine if any of the keys are a prefix of the input string.

Example:

    val prefixTree = PrefixTree()

    prefixTree.addEntry("co")
    prefixTree.addEntry("eng")
    prefixTree.addEntry("ro")
    prefixTree.addEntry("trad")

    println("Lookup (engineering): " + prefixTree.lookup("engineering")) //should return true
    println("Lookup (science): " + prefixTree.lookup("science")) //should return false
    
NOTES: 

The list of key strings is long, e.g. 100,000 of them. Key strings vary in length. All characters are lower-case letters. We want to perform the lookup operation frequently, ignoring case, for lots of different input strings

IMPLEMENTATION NOTES

Implementation provided in Kotlin. To maintain fast-access time and consume as low memory as possible, entry list is stored in a tree, where child element is a next character from prefix.