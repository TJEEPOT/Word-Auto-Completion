/******************************************************************************
 
 Project     : CMP-5014Y - Word Auto Completion with Tries :
                AutoCompletion.
 
 File        : Trie.java
 
 Date        : Thursday 12 March 2020
 
 Author      : Martin Siddons
 
 Description : Part 2 of the assignment is to make a Trie Data Structure to
 hold strings and write methods to manipulate the trie.
 
 History     : 12/03/2020 - Initial setup
 14/03/2020 - Completed Q1.
 13/05/2020 - Lost track on filling this in, just finished Q2.
 14/06/2020 - Fixed implementation of getAllWords for part 3.
 15/06/2020 - Finalised implementation by clearing up some methods.
 ******************************************************************************/

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

class TrieNode {
    private       char       value;
    private final TrieNode[] offspring;
    private       boolean    isKey;
    private       int        level; // track how deep this node is in the trie
    
    // Constructors
    public TrieNode() {
        this.offspring = new TrieNode[26];
    }
    
    public TrieNode(char c) {
        this.value     = c;
        this.offspring = new TrieNode[26];
    }
    
    public char getValue()      {return value;}
    
    public boolean getKey()     {return this.isKey;}
    
    public int getLevel()       {return this.level;}
    
    public void setKey()        {this.isKey = true;}
    
    public void setLevel(int l) {this.level = l;}
    
    
    /**
     * Check if a given letter exists as a child on this node.
     *
     * @param c : Character to find linked from this trie.
     * @return : The TrieNode representing the given letter, or null if that
     * letter is not an offspring.
     */
    public TrieNode getOffspring(char c) {
        int pos = c - 97; // turn ASCII character value to numeric (so a = 0)
        if (pos < 0) {
            System.out.println("Invalid char num: " + c);
        }
        return this.offspring[pos];
    }
    
    /**
     * Retrieve a list of nodes of which the called-upon node is a parent.
     *
     * @return : List of TrieNodes that are offspring of the given node.
     */
    public LinkedList<TrieNode> getAllOffspring() {
        LinkedList<TrieNode> offspring = new LinkedList<>();
        
        for (TrieNode node : this.offspring) {
            if (node != null) {
                offspring.add(node);
            }
        }
        return offspring;
    }
    
    /**
     * Set a node for the given character, if it doesn't exist.
     *
     * @param c : Character to add to the trie.
     * @return : True if character has been added, false if not.
     */
    public boolean setOffspring(char c) {
        int pos = c - 97; // turn ASCII char to numeric value (where a = 0)
        // Create the node if it doesn't exist.
        if (this.offspring[pos] == null) {
            this.offspring[pos] = new TrieNode(c);
            return true;
        }
        return false;
    }
    
}

public class Trie {
    private TrieNode root;
    
    // Constructor
    public Trie() {this.root = new TrieNode();}
    
    /**
     * Add a given string to the Trie.
     *
     * @param key : String to be added to the Trie.
     * @return : True if string has been added. If the string already exists,
     * return false.
     */
    public boolean add(String key) {
        TrieNode curNode = this.root;
        boolean wasAdded = false;
        
        for (int i = 0; i < key.length(); i++) {
            char curChar = key.charAt(i);
            
            // Check if the current character is in the trie.
            TrieNode next = curNode.getOffspring(curChar);
            
            // If not, add it. Switch to the node for this letter.
            if (next == null) {
                curNode.setOffspring(curChar);
                next = curNode.getOffspring(curChar);
                next.setLevel(i + 1);
                wasAdded = true;
            }
            curNode = next; // move to this node.
        }
        
        // Once at the end of the key String, set the final node to be a key.
        if (curNode != null) {
            curNode.setKey();
        }
        return wasAdded;
    }
    
    /**
     * Check if a given string exists in the trie.
     *
     * @param key : String to be checked.
     * @return : True if the whole string exists (and is not a prefix or only
     * part of an existing word), false if not.
     */
    public boolean contains(String key) {
        TrieNode curNode = this.root;
        boolean isKey = false;
        
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            TrieNode next = curNode.getOffspring(c);
            if (next == null) {
                return false;
            }
            curNode = next;
            isKey   = curNode.getKey();
        }
        return isKey;
    }
    
    /**
     * Traverse the trie in breadth-first order and return the result.
     *
     * @return : String of characters from reading the trie breadth-first.
     */
    public String outputBreadthFirstSearch() {
        LinkedList<TrieNode> toVisit = new LinkedList<>();
        int stringSize = 1000; // fixed starting size of 2000 bytes
        char[] trieString = new char[stringSize];
        int stringPos = 0;
        toVisit.add(this.root);
        
        while (! toVisit.isEmpty()) { // while the visit list is not empty:
            TrieNode curNode = toVisit.remove(); // get the next node from list
            trieString[stringPos] = curNode.getValue(); // add node to string
            toVisit.addAll(curNode.getAllOffspring()); // get all child nodes
            stringPos++;
            
            // If the string has reached it's limit, increase that by 10 times
            // and copy the old string over to the new one.
            if (stringPos == stringSize) {
                char[] temp = trieString;
                stringSize = stringSize * 10;
                trieString = new char[stringSize];
                for (int i = 0; i < stringSize / 10; i++) {
                    trieString[i] = temp[i];
                }
            }
        }
        return new String(trieString);
    }
    
    /**
     * Traverse the trie in depth-first order and return the result.
     *
     * @return String of characters from reading the trie depth-first
     */
    public String outputDepthFirstSearch() {
        Stack<TrieNode> toVisit = new Stack<>();
        int stringSize = 1000; // starting size for output string
        char[] trieString = new char[stringSize];
        int stringPos = 0;
        toVisit.push(this.root);
        
        while (! toVisit.isEmpty()) { // while the visit stack is not empty:
            TrieNode curNode = toVisit.pop(); // take the node off the stack
            
            // Retrieve all the offspring from the current node and add them to
            // the toVisit stack in reverse order (due to stack being FILO)
            LinkedList<TrieNode> node = curNode.getAllOffspring();
            while (! node.isEmpty()) {
                toVisit.push(node.removeLast());
            }
            trieString[stringPos] = curNode.getValue(); // add node to String
            stringPos++;
            
            // If the string has reached it's limit, increase that by 10x and
            // copy the old string over to the new one.
            if (stringPos == stringSize) {
                char[] temp = trieString;
                stringSize = stringSize * 10;
                trieString = new char[stringSize];
                for (int i = 0; i < temp.length; i++) {
                    trieString[i] = temp[i];
                }
            }
        }
        return new String(trieString);
    }
    
    /**
     * returns a new trie rooted at the prefix, or null if the prefix is not
     * present in this trie.
     *
     * @param prefix : String which denotes the root of the returned trie.
     * @return : Trie structure of all branches of the given trie which branch
     * from the given prefix or empty trie if prefix not found.
     */
    public Trie getSubTrie(String prefix) {
        Trie subTrie = new Trie();
        TrieNode curNode = this.root;
        
        if (prefix.equals("")) {
            return subTrie;
        }
        
        // Find the prefix within the main trie while building the subtrie:
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            
            TrieNode next = curNode.getOffspring(c);
            if (next == null) {
                return subTrie;
            }
            curNode = next;
        }
        subTrie.root = curNode;
        
        return subTrie;
    }
    
    /**
     * Returns all words held in the trie it is called on. This is written to be
     * as time efficient as possible without regard to memory usage.
     *
     * @return List of strings corresponding to each word in the trie. Returns
     * an empty list if there are no words in the trie it's called on.
     */
    List<String> getAllWords() {
        List<String> words = new ArrayList<>(); // words to be returned
        Stack<TrieNode> toVisit = new Stack<>(); // stack of Nodes to visit next
        int maxWordLength = 35; // there's no words over 35 letters in English
        char[] curWord = new char[maxWordLength]; // hold the word being formed
        
        // Add the first node (root) to the toVisit stack.
        LinkedList<TrieNode> offspring = this.root.getAllOffspring();
        while (! offspring.isEmpty()) {
            toVisit.push(offspring.removeLast());
        }
        
        while (! toVisit.isEmpty()) {
            // Retrieve the next node and find it's position in the trie
            TrieNode curNode = toVisit.pop();
            int charPos = curNode.getLevel() + 1; // position of the next char
            curWord[charPos] = curNode.getValue(); // add current letter
            
            // Check if the current node is the end of a word and if so, add
            // the word to our list of words. We need to ensure we only copy the
            // letters that represent this word due to a persistent version of
            // curWord being used, however this is still quicker than making and
            // storing either a deep or shallow copy of curWord in a stack.
            if (curNode.getKey()) {
                StringBuilder foundWord = new StringBuilder();
                for (int i = 0; i <= charPos; i++) {
                    char curChar = curWord[i];
                    if (curChar != '\u0000') {
                        foundWord.append(curChar);
                    }
                }
                words.add(foundWord.toString());
            }
            
            // Retrieve all the offspring from the current node and add them to
            // the toVisit stack in reverse order (due to stack being FILO).
            offspring = curNode.getAllOffspring();
            while (! offspring.isEmpty()) {
                toVisit.push(offspring.removeLast());
            }
        }
        return words;
    }
    
    // Test Harness
    public static void main(String[] args) {
        // Testing add(), should return "true" for all.
        Trie t = new Trie();
        System.out.println(t.add("cheers"));
        System.out.println(t.add("cheese"));
        System.out.println(t.add("chat"));
        System.out.println(t.add("cat"));
        System.out.println(t.add("can")); // REMOVE
        System.out.println(t.add("bat") + "\n");
        
        // Testing contains().
        System.out.println(t.contains("cheese")); // should return "true"
        System.out.println(t.contains("chose")); // should return "false"
        System.out.println(t.contains("ch") + "\n"); // should return "false"
        
        // Testing outputBreadthFirstSearch(), should return "bcaahttaetersse".
        System.out.println(t.outputBreadthFirstSearch() + "\n");
        
        // Testing outputDepthFirstSearch(), should return "batcathateersse".
        System.out.println(t.outputDepthFirstSearch() + "\n");
        
        // Testing getSubTrie, should return "aetersse".
        System.out.println(t.getSubTrie("ch").outputBreadthFirstSearch() +
                "\n");
        
        // Testing getAllWords, should return the list of words from add() test.
        System.out.println(t.getAllWords());
    }
}
