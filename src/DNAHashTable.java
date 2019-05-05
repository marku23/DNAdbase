
public class DNAHashTable implements HashTable<DNARecord> {
    // Variables...............................................................
    public static final int bucketSize = 32;
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
        table = new DNARecord[size];
        numBuckets = size / bucketSize;
    }


    // Methods.................................................................
    
    
    @Override
    public boolean insert(String key, DNARecord value) {
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
    
    long sfold(String s, int M) {
        int intLength = s.length() / 4;
        long sum = 0;
        for (int j = 0; j < intLength; j++) {
          char c[] = s.substring(j * 4, (j * 4) + 4).toCharArray();
          long mult = 1;
          for (int k = 0; k < c.length; k++) {
            sum += c[k] * mult;
            mult *= 256;
          }
        }

        char c[] = s.substring(intLength * 4).toCharArray();
        long mult = 1;
        for (int k = 0; k < c.length; k++) {
          sum += c[k] * mult;
          mult *= 256;
        }

        sum = (sum * sum) >> 8;
        return(Math.abs(sum) % M);
      }

}
