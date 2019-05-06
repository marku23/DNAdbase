
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
        manager = new MemoryManager(memoryFile);
        this.table = table;
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
        DNARecord thisRecord = new DNARecord(WHAT, ID.length(), WHAT, length);
        if (manager.insert(ID, thisRecord)) {//i'm assuming it will return true if it gets inserted
            table.insert(ID, thisRecord);
        }
    }


    /**
     * Removes a sequence with the given ID from the
     * memory manager
     * 
     * @param ID
     *            - the ID of the sequence we are removing
     */
    public void remove(String ID) {
        boolean found = false;
        int destination = table.remove(ID);
        String binString = "";
        int bucketStart = (destination / DNAHashTable.bucketSize) * DNAHashTable.bucketSize;
        for (int i = bucketStart; i < bucketStart + DNAHashTable.bucketSize; i++)
        {
            DNARecord thisRecord = table.getTable()[i];
            if (ID.equals(getID(thisRecord))) {
                table.getTable()[i].setIDLength(-1);
                found = true;
            }
        }
        
        if (found) {
            manager.remove(ID);
            System.out.println("Sequence removed " + ID + ":");
            System.out.println(binaryToDNA(binString));
        }
    }


    /**
     * Prints a list of all sequence IDs in the database
     * @throws IOException if the memory file was not found
     */
    public void print() throws IOException {
        System.out.println("Sequence IDs:");
        for (int i = 0; i < table.getTable().length; i++)
        {
            if (table.getTable()[i] != null)
            {
                DNARecord thisRecord = table.getTable()[i];
                String thisID = getID(thisRecord);
                System.out.println(thisID + ": hash slot [" + search(thisID) + "]");
            }
        }
    }


    /**
     * Prints out the sequence associated with the given
     * ID, if there is one
     * 
     * @param ID
     *            - the ID of the sequence we are searching for
     * @throws IOException if the memory file was not found
     */
    public int search(String ID) throws IOException {
        int destination = table.search(ID);
        boolean found = false;
        int foundIndex = -1;
        int bucketStart = ((int)destination / DNAHashTable.bucketSize) * DNAHashTable.bucketSize;
        for (int i = bucketStart; i < bucketStart + DNAHashTable.bucketSize; i++)
        {
            DNARecord thisRecord = table.getTable()[i];
            if (ID.equals(getID(thisRecord))) {
                System.out.print("Sequence found: ");
                System.out.println(getSequence(thisRecord));
                found = true;
                foundIndex = i;
            }
        }
        if (!found) { System.out.println("Sequence not found"); }
        return foundIndex;
     }
        
    /**
     * Returns the DNA string for a string of 0s and 1s
     * @param bin - the string of 0s and 1s
     * @return the corresponding DNA string
     */
    public String binaryToDNA(String bin)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < bin.length() - 2; i += 2)
        {
            String temp = bin.substring(i, i + 2);
            switch (temp) {
            case ("00"):
                builder.append('A');
                break;
            case ("01"):
                builder.append('C');
                break;
            case ("10"):
                builder.append('G');
                break;
            case("11"):
                builder.append('T');
                break;
            default:
                builder.append('A');
            }
        }
        return builder.toString();
    }
    
    /**
     * Returns the String ID of this Record
     * @param record - the record object we are checking
     * @return - the string representing its ID
     * @throws IOException if the memory file was not found
     */
    public String getID(DNARecord record) throws IOException
    {
        byte[] binBytes = new byte[1024];
        memory.read(binBytes, record.getIDOffset(), record.getIDLength()); //this part might be wrong?
        String binString = new String(binBytes);
        return binaryToDNA(binString);
    }
    
    /**
     * Returns the String sequence of this Record
     * @param record - the record object we are checking
     * @return - the string representing its sequence
     * @throws IOException if the memory file was not found
     */
    public String getSequence(DNARecord record) throws IOException
    {
        byte[] binBytes = new byte[1024];
        memory.read(binBytes, record.getSeqOffset(), record.getSeqLength()); //this part might be wrong?
        String binString = new String(binBytes);
        return binaryToDNA(binString);
    }
}
