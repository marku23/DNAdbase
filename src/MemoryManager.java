import java.util.LinkedList;

/**
 * Keeps track of free space in memory and handles requests to memory.
 *  
 * @author Christopher Cox (ccox17)
 * @version 05.05.2019
 */

public class MemoryManager {
    // Variables...............................................................
    private LinkedList<FreeBlock> blocks;
    private LinkedList<String> duplicates;
    
    // Constructors............................................................
    
    /**
     * Creates a new MemoryManager object.  
     */
    
    public MemoryManager() {
        blocks = new LinkedList<FreeBlock>();
        duplicates = new LinkedList<String>();
    }
    
    // Variables...............................................................
    
    public DNARecord insert(String Sequence, String SequenceID) {
        int seqLength = Sequence.length();
        int IDLength = SequenceID.length();
        int memSize = seqLength + IDLength;
    }
}
