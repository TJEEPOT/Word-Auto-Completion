# What It Does #
This project is the basis for word auto-complete system in three parts. The first part is implemented in the DictionaryFinder class, which reads a text file and forms a list of strings, forms a set of words that exist in the document and performs a frequency count on them, sorts the list of words alphabetically and writes the words and frequencies to another text file.

The second part defines a trie data structure in the trie class. This class has methods to load and retrieve words as keys to and from the trie in different ways.

Finally, the third class, AutoCompletion, loads a list of queries (partially completed words) from file and for each one it computes the three most likely words it could form, based on the given dictionary, and outputs them to standard output.

# What I Learned #
This project allowed me to use more advanced **data structures** such as  **Trie** and understand them in more detail by creating them from scratch.The project required working at a lower level so that the **pseudocode** I wrote could more accurately match the implementation. I learned more about **tree** structures, such as how to add and find keys and how to implement efficient **Breadth First Search** and **Depth First Search** algorithms.

# Usage Notes #
Compile the project with your favorite compiler. The program requires several csv files to run correctly. In my implementation, these are given as the following:
* lotr.csv - The source text to use. In this case we are using an excerpt from The Lord of the Rings - The Fellowship of the Ring.
* lotrQueries.csv - The list of queries to make on the source text.

The program outputs one file - lotrMatches.txt, which is the list of three possible words and their probabilities for each individual query. 
There are no command line arguments for this program.
