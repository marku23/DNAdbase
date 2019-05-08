/**
 * The class for the DNAHashTable, which implements a generic hash table of
 * DNARecord objects
 * 
 * @author marku23
 * @author ccox17
 * @version 5.7.19
 *
 */
public class DNAHashTable implements HashTable<DNARecord> {
    // Variables...............................................................
    /**
     * Universal bucket size; does not change
     */
    public static final int bucketSize = 32;
    private DNARecord[] table;
    // private int numBuckets;

    // Constructors............................................................

    /**
     * Creates a new hashtable of the specified size.
     * 
     * @param size
     *            the number of entries to be stored in the hash table.
     */

    public DNAHashTable(int size) {
        table = new DNARecord[size];
        // numBuckets = size / bucketSize;
    }

    // Methods.................................................................

    /**
     * Finds the bucket that the DNARecord param should be inserted into, and
     * then inserts it into that bucket if the bucket has an empty space in it.
     * 
     * @param key
     *            - the ID of the sequence we are storing
     * @param value
     *            - the DNARecord object representing the sequence we are
     *            storing
     */
    @Override
    public boolean insert(String key, DNARecord value) {
        int destination = (int) sfold(key, table.length);
        boolean inserted = false;
        if (table[destination] == null
                || table[destination].getIDLength() < 0) {
            table[destination] = value;
            inserted = true;
        }
        else {
            int bucketStart = ((int) destination / bucketSize) * bucketSize;
            for (int i = destination; i < bucketStart + bucketSize; i++) {
                if (table[i] == null
                        || table[i].getIDLength() < 0 && !inserted) {
                    table[i] = value;
                    inserted = true;
                    break;
                }
            }
            for (int i = bucketStart; i < destination; i++) {
                if ((table[i] == null || table[i].getIDLength() < 0)
                        && !inserted) {
                    table[i] = value;
                    inserted = true;
                    // break;
                }
            }
        }
        return inserted;
    }

    /**
     * Returns the location that the key would be hashed to so that the remove
     * can take place in DataProcessor
     * 
     * @param key
     *            - the ID of the DNARecord we are searching for
     * @return the index that key would hash to
     */
    @Override
    public int remove(String key) {
        return (int) sfold(key, table.length);
    }

    /**
     * Searches the hash table for a record with the given ID and returns it, if
     * one exists
     * 
     * @param key
     *            - the ID of the record we are searching for
     * @return a DNARecord object with the given ID; null if one did not exist
     *         in the table
     */
    @Override
    public int search(String key) {
        return (int) sfold(key, table.length);
    }

    /**
     * Gets the handle of the DNARecord stored at the given offset in the table
     * 
     * @param offset
     *            - the index of the table we are checking
     * @return the record stored at that index
     */
    public DNARecord getHandleAtOffset(int offset) {
        return table[offset];
    }

    /**
     * Returns the table; used in testing
     * 
     * @return the table of DNARecord objects
     */
    public DNARecord[] getTable() {
        return table;
    }

    /**
     * Makes the entry in the table at the given index into a tombstone by
     * setting its ID length to -1
     * 
     * @param index
     *            - the index of the record we are making a tombstone
     */
    public void makeTombstone(int index) {
        table[index].setIDLength(-1);
    }

    /**
     * A function to calculate the index that a DNARecord should be placed in
     * within the hash table
     * 
     * @param s
     *            - the ID of the sequence to be inserted
     * @param M
     *            - the size of the hash table
     * @return a long representing the index that the record should occupy
     */
    long sfold(String s, int m) {
        int intLength = s.length() / 4;
        long sum = 0;
        for (int j = 0; j < intLength; j++) {
            char[] c = s.substring(j * 4, (j * 4) + 4).toCharArray();
            long mult = 1;
            for (int k = 0; k < c.length; k++) {
                sum += c[k] * mult;
                mult *= 256;
            }
        }

        char[] c = s.substring(intLength * 4).toCharArray();
        long mult = 1;
        for (int k = 0; k < c.length; k++) {
            sum += c[k] * mult;
            mult *= 256;
        }

        sum = (sum * sum) >> 8;
        return (Math.abs(sum) % m);
    }

}
