/******************************************************************************
 
 Project     : CMP-5014Y - Word Auto Completion with Tries :
                AutoCompletion.
 
 File        : AutoCompletion.java
 
 Date        : Thursday 14 May 2020
 
 Author      : Martin Siddons
 
 Description : Part 3 of the assignment is use Trie.java and
 DictionaryFinder.java to read in a large dictionary and a list of prefixes then
 output the three most common words on each prefix and save that to a file.
 
 History     : 14/05/2020 - Initial setup
 
 ******************************************************************************/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AutoCompletion {
    
    public static void main(String[] args) {
        // 1. form a dictionary file of words and counts from the file lotr.csv.
        DictionaryFinder lotr = new DictionaryFinder();
        lotr.formDictionary(DictionaryFinder.readWordsFromCSV("lotr.csv"));
        
        // 2. construct a trie from the dictionary using your solution from
        // part 2.
        Trie lotrTrie = new Trie();
        for (String k : lotr.dict.keySet()) {
            lotrTrie.add(k);
        }
        
        // 3. load the prefixs from lotrQueries.csv
        ArrayList<String> prefixs =
                DictionaryFinder.readWordsFromCSV("lotrQueries.csv");
        
        // 4. for each prefix query:
        List<String> toFile = new ArrayList<>();
        for (String s : prefixs) {
            // 4.1. Recover all words matching the prefix from the trie.
            Trie subTrie = lotrTrie.getSubTrie(s);
            List<String> matches = new ArrayList<>(subTrie.getAllWords());
            
            // 4.2. Choose the three most frequent words and display to
            // standard output.
            // 2d array to store word frequency and position in matches list.
            // +1 ensures if we add the prefix, it won't run out of bounds.
            int[][] count = new int[matches.size() + 1][2];
            float totalCount = 0;
            
            if (lotr.dict.containsKey(s)) { // ensure prefix is checked
                matches.add("");
            }
            
            for (int i = 0; i < matches.size(); i++) {
                String str = s + matches.get(i); // complete the word
                int wordCount = lotr.dict.get(str); // get this word's count
                totalCount += wordCount; // inc the word counter
                count[i][0] = wordCount; // store the count
                count[i][1] = i; // store the word's position
            }
            Arrays.sort(count, (a, b) -> Integer.compare(b[0], a[0]));
            StringBuilder topThree = new StringBuilder();
            topThree.append(s);
            
            // output and save top three results
            for (int i = 0; i < matches.size() && i < 3; i++) {
                float prob = count[i][0] / totalCount;
                String word = s + matches.get(count[i][1]);
                System.out.println(word + " (probability " + prob + ")");
                topThree.append(",").append(word).append(",").append(prob);
            }
            toFile.add(topThree.toString());
            System.out.println();
        }
        // 4.3. Write the results to lotrMatches.csv.
        DictionaryFinder.saveCollectionToFile(toFile, "lotrMatches.csv");
    }
}
