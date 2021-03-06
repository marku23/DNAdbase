import java.io.*;
import java.util.LinkedList;

/**
 * A class that contains methods to insert, remove, search for, and print dna
 * sequences using the hash file and memory file
 * 
 * @author marku23
 * @author ccox17
 *
 */
public class DataProcessor {

    private DNAHashTable table;
    private MemoryManager manager;


    /**
     * Default constructor
     * 
     * @param memoryFile
     *            - the file we are writing to
     * @param table
     *            - the table we are hashing to
     */
    public DataProcessor(RandomAccessFile memoryFile, DNAHashTable table) {
        manager = new MemoryManager(memoryFile);
        this.table = table;
    }


    /**
     * Inserts a DNA sequence into the hash table and the memory manager
     * 
     * @param seqID
     *            - the ID of the sequence
     * @param seq
     *            - the sequence
     * @throws IOException
     *             if the file was not found
     */
    public void insert(String seqID, String seq) throws IOException {
        DNARecord thisRecord = manager.insert(seqID, seq);
        if (thisRecord == null) {
            System.out.println("SequenceID " + seqID + " exists");
        }
        else {
            boolean inserted = table.insert(seqID, thisRecord);
            if (!inserted) {
                System.out.println("Bucket full. Sequence " + seqID
                    + " could not be inserted");
            }
        }
    }


    /**
     * Removes a sequence with the given ID from the memory manager
     * 
     * @param seqID
     *            - the ID of the sequence we are removing
     * @throws IOException
     *             if the file was not found
     */
    public void remove(String seqID) throws IOException {
        boolean found = false;
        int destination = table.search(seqID);
        DNARecord thisRecord = table.getHandleAtOffset(destination);
        int bucketStart = (destination / DNAHashTable.bucketSize)
            * DNAHashTable.bucketSize;
        for (int i = bucketStart; i < bucketStart
            + DNAHashTable.bucketSize; i++) {
            thisRecord = table.getHandleAtOffset(i);
            if (thisRecord != null && thisRecord.getIDLength() >= 0 && seqID
                .length() == thisRecord.getIDLength() && seqID.equals(binaryToDNA(
                    manager.getID(thisRecord), seqID.length()))) {
                found = true;
                destination = i;
                break;
            }
        }

        if (found) {
            byte[] sequence = manager.remove(thisRecord, seqID);
            System.out.println("Sequence Removed " + seqID + ":");
            System.out.println(binaryToDNA(sequence, thisRecord
                .getSeqLength()));
            table.makeTombstone(destination);
        }
        else {
            System.out.println("SequenceID " + seqID + " not found");
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
            if (table.getTable()[i] != null
                    && table.getTable()[i].getIDLength() > 0) {
                DNARecord thisRecord = table.getTable()[i];
                byte[] thisID = manager.getID(thisRecord);
                System.out.println(binaryToDNA(thisID, thisRecord.getIDLength())
                        + ": hash slot [" + i + "]");
            }
        }
        System.out.print("Free Block List:");
        LinkedList<FreeBlock> blocks = manager.getFreeBlocks();
        if (blocks.isEmpty()) {
            System.out.println(" none");
        }
        else {
            System.out.println("");
            for (int i = 0; i < blocks.size(); i++) {
                System.out.println("[Block " + (i + 1) + "] " + blocks.get(i)
                    .toString());
            }
        }
    }


    /**
     * Prints out the sequence associated with the given ID, if there is one
     * 
     * @param seqID
     *            - the ID of the sequence we are searching for
     * @throws IOException
     *             if the memory file was not found
     */
    public void search(String seqID) throws IOException {
        int destination = table.search(seqID);
        boolean found = false;
        int bucketStart = ((int)destination / DNAHashTable.bucketSize)
            * DNAHashTable.bucketSize;
        for (int i = bucketStart; i < bucketStart
            + DNAHashTable.bucketSize; i++) {
            DNARecord thisRecord = table.getHandleAtOffset(i);
            if (thisRecord != null && thisRecord.getIDLength() >= 0
                && thisRecord.getIDLength() == seqID.length() && seqID.equals(
                    binaryToDNA(manager.getID(thisRecord), seqID.length()))) {
                System.out.print("Sequence Found: ");
                System.out.println(binaryToDNA(manager.getSequence(thisRecord),
                    thisRecord.getSeqLength()));
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("SequenceID " + seqID + " not found");
        }
    }


    /**
     * Returns the DNA string for a string of 0s and 1s
     * 
     * @param bin
     *            - the string of 0s and 1s
     * @param length
     *            - the expected length of the DNA string
     * @return the corresponding DNA string
     */
    public String binaryToDNA(byte[] bin, int length) {
        StringBuilder builder = new StringBuilder();
        int sequenceLength = length;
        for (int i = 0; i < bin.length; i++) {
            int minVal = Math.min(4, sequenceLength);
            String binaryString = String.format("%8s", Integer.toBinaryString(
                bin[i] & 0xFF)).replace(' ', '0');
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
