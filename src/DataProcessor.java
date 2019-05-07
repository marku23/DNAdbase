
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
    public DataProcessor(RandomAccessFile memoryFile, DNAHashTable table) {
        memory = memoryFile;
        manager = new MemoryManager(memoryFile);
        this.table = table;
    }

    /**
     * Inserts a DNA sequence into the hash table and the memory manager
     * 
     * @param ID
     *            - the ID of the sequence
     * @param seq
     *            - the sequence
     * @throws IOException if the file was not found
     */
    public void insert(String ID, String seq) throws IOException {
        DNARecord thisRecord = manager.insert(ID, seq);
        if (thisRecord == null) {
            System.out.println("SequenceID " + ID + " exists");
        }
        else {
            boolean inserted = table.insert(ID, thisRecord);
            if (!inserted) {
                System.out.println("Bucket full. Sequence " + ID
                        + " could not be inserted");
            }
        }
    }

    /**
     * Removes a sequence with the given ID from the memory manager
     * 
     * @param ID
     *            - the ID of the sequence we are removing
     * @throws IOException  if the file was not found
     */
    public void remove(String ID) throws IOException {
        boolean found = false;
        int destination = table.remove(ID);
        DNARecord target = null;
        int bucketStart = (destination / DNAHashTable.bucketSize)
                * DNAHashTable.bucketSize;
        for (int i = bucketStart; i < bucketStart
                + DNAHashTable.bucketSize; i++) {
            DNARecord thisRecord = table.getTable()[i];
            if (ID.getBytes().equals(manager.getID(thisRecord))) {
                table.getTable()[i].setIDLength(-1);
                found = true;
                target = thisRecord;
            }
        }

        if (found) {
            manager.remove(target, ID);
            System.out.println("Sequence removed " + ID + ":");
            System.out.println(manager.getSequence(target));
        }
    }

    /**
     * Prints a list of all sequence IDs in the database
     * 
     * @throws IOException
     *             if the memory file was not found
     */
    public void print() throws IOException {
        System.out.println("Sequence IDs:");
        for (int i = 0; i < table.getTable().length; i++) {
            if (table.getTable()[i] != null) {
                DNARecord thisRecord = table.getTable()[i];
                byte[] thisID = manager.getID(thisRecord);
                System.out.println(
                        thisID + ": hash slot [" + search(binaryToDNA(thisID, thisRecord.getSeqLength())) + "]");
            }
        }
    }

    /**
     * Prints out the sequence associated with the given ID, if there is one
     * 
     * @param ID
     *            - the ID of the sequence we are searching for
     * @throws IOException
     *             if the memory file was not found
     */
    public int search(String ID) throws IOException {
        int destination = table.search(ID);
        boolean found = false;
        int foundIndex = -1;
        int bucketStart = ((int) destination / DNAHashTable.bucketSize)
                * DNAHashTable.bucketSize;
        for (int i = bucketStart; i < bucketStart
                + DNAHashTable.bucketSize; i++) {
            DNARecord thisRecord = table.getTable()[i];
            if (ID.getBytes().equals(manager.getID(thisRecord))) {
                System.out.print("Sequence found: ");
                System.out.println(manager.getSequence(thisRecord));
                found = true;
                foundIndex = i;
            }
        }
        if (!found) {
            System.out.println("Sequence not found");
        }
        return foundIndex;
    }

    /**
     * Returns the DNA string for a string of 0s and 1s
     * 
     * @param bin
     *            - the string of 0s and 1s
     * @return the corresponding DNA string
     */
    public String binaryToDNA(byte[] bin, int length) {
        StringBuilder builder = new StringBuilder();
        int sequenceLength = length;
        for (int i = 0; i < bin.length; i++) {
            int minVal = Math.min(4, sequenceLength);
            String binaryString = String
                    .format("%8s", Integer.toBinaryString(bin[i] & 0xFF))
                    .replace(' ', '0');
            for (int j = 0; j < minVal; j++) {
                int substringIndex = 2 + (j * 2);
                if (substringIndex > (sequenceLength * 2)) {
                    substringIndex = sequenceLength;
                }
                    String temp = binaryString.substring(j * 2, substringIndex);
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
                    default:
                        builder.append('T');
                    }
                }
            
            sequenceLength -= 4;
        }
        return builder.toString();
    }
}
