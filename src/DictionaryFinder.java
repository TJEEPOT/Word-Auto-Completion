/******************************************************************************
 
 Project     : CMP-5014Y - Word Auto Completion with Tries:
                AutoCompletion.
 
 File        : DictionaryFinder.java
 
 Date        : Friday 06 March 2020
 
 Author      : Martin Siddons
 
 Description : This class fulfills the requirements of part 1. It supplies
 methods that read in a text document into a list of Strings, form a set of
 words that exist in the document and count how many times each word occurs,
 sort the list alphabetically and write those words and frequencies to file.
 
 History     :
 06/03/2020 - v1.0 - Initial setup, copied over readWordsFromCSV and
 saveCollectionToFile.
 07/03/2020 - v1.1 - Eventually settled on a implementation that works well in
 Java. Editing pseudocode to fit.
 ******************************************************************************/

import java.io.*;
import java.util.*;

public class DictionaryFinder {
    HashMap<String, Integer> dict;
    
    // Constructor
    public DictionaryFinder() {
        this.dict = null;
    }
    
    /**
     * Static method to read in files from a CSV file and output an ArrayList of
     * Strings, where each string is a line from the given file. Adapted from
     * code given by AJB.
     *
     * @param file : File to read in.
     * @return ArrayList of Strings read in from file.
     */
    public static ArrayList<String> readWordsFromCSV(String file) {
        ArrayList<String> words = new ArrayList<>();
        try {
            Scanner sc = new Scanner(new File(file));
            sc.useDelimiter("[ ,\r]");
            String str;
            while (sc.hasNext()) {
                str = sc.next();
                str = str.trim();
                str = str.toLowerCase();
                words.add(str);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Error: File not found. Aborting");
        }
        return words;
    }
    
    /**
     * Static method to write a given collection to file.
     * Adapted from code given by AJB.
     *
     * @param c    : Collection to save to file.
     * @param file : Name of file to be created.
     */
    public static void saveCollectionToFile(Collection<?> c, String file) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            for (Object w : c) {
                printWriter.println(w.toString());
            }
            printWriter.close();
        }
        catch (IOException e) {
            System.out.println("Error: Unable to write file. Aborting.");
        }
        System.out.println("\nDictionary saved to file " + file);
    }
    
    /**
     * Take an ArrayList of Strings and process each string into a dictionary.
     *
     * @param in : Arraylist to be formed into a dictionary.
     */
    public void formDictionary(List<String> in){
        this.dict = new HashMap<>(in.size());
        
        // For each string in the ArrayList, check if it exists in the
        // dictionary. If it doesn't, add it with a count of 1. If it does
        // exist, add 1 to the value on that entry.
        for (String s : in) {
            this.dict.put(s, this.dict.getOrDefault(s, 0) + 1);
        }
    }
    
    /**
     * Call the Dictionary previously created with formDictionary and produce a
     * formatted list. Ensure the list is sorted and send to
     * saveCollectionToFile.
     */
    public void saveToFile() {
        List<String> entries = new ArrayList<>();
        
        // Take each entry from the map, format it and add it to the list.
        for (Map.Entry<String, Integer> e : this.dict.entrySet()){
            entries.add(e.getKey() + "," + e.getValue());
        }
        Collections.sort(entries); // Sort the list alphabetically.
        
        // Call saveCollectionToFile to save the sorted, formatted list to file.
        String filename = "output.csv";
        saveCollectionToFile(entries, filename);
    }

    // Test Harness.
    public static void main(String[] args) {
        DictionaryFinder df = new DictionaryFinder();
        
        // 1. read text document into a list of strings
        ArrayList<String> in = readWordsFromCSV("testDocument.csv");
        
        // 2. form a set of words that exist in the document and count the
        // number of times each word occurs in a method called FormDictionary
        df.formDictionary(in);
        Collection<Integer> e = df.dict.values();
        System.out.print("counts for words in dictionary (before sort): ");
        System.out.println(e);
        
        // 3. sort the words alphabetically; and
        // 4. write the words and associated frequency to file.
        df.saveToFile();
    }
}
