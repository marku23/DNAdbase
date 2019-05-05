
public class DNAHashTable implements HashTable<DNARecord> {
    // Variables...............................................................
    public static final int bucketSize = 32;
    private MemoryManager mem;
    private DNARecord[] table;
    private int numBuckets;


    // Constructors............................................................

    /**
     * Creates a new hashtable of the specified size.
     * 
     * @param size
     *            the number of entries to be stored in the hash table.
     */

    public DNAHashTable(int size) {
        mem = new MemoryManager();
        table = new DNARecord[size];
        numBuckets = size / bucketSize;
    }


    // Methods.................................................................
    
    
    @Override
    public boolean insert(DNARecord entry) {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public boolean remove(String key) {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public DNARecord search(String key) {
        // TODO Auto-generated method stub
        return null;
    }

}
