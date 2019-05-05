
/**
 * A class that contains methods to insert,
 * remove, search for, and print dna sequences using the
 * hash file and memory file
 * 
 * @author marku23
 * @author ccox17
 *
 */

import java.io.*;

public class DataProcessor {

    private RandomAccessFile memory;
    private DNAHashTable table;
    private MemoryManager manager;

    /**
     * Default constructor
     * 
     * @param hashFile
     *            - the file we are hashing to
     * @param memory
     *            - the memory file
     */
    public DataProcessor(
        RandomAccessFile memoryFile, DNAHashTable table) {
        memory = memoryFile;
        manager = new MemoryManager();
    }


    /**
     * Inserts a DNA sequence into the hash table and
     * the memory manager
     * 
     * @param ID
     *            - the ID of the sequence
     * @param seq
     *            - the sequence
     * @param length
     *            - the length of the sequence
     */
    public void insert(String ID, String seq, int length) {
        // do things
    }


    /**
     * Removes a sequence with the given ID from the
     * memory manager
     * 
     * @param ID
     *            - the ID of the sequence we are removing
     */
    public void remove(String ID) {
        // do things
    }


    /**
     * Prints a list of all sequence IDs in the database
     */
    public void print() {
        // do things
    }


    /**
     * Prints out the sequence associated with the given
     * ID, if there is one
     * 
     * @param ID
     *            - the ID of the sequence we are searching for
     */
    public void search(String ID) {
        // do things
    }
}
